package com.example.within.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.within.dto.*;
import com.example.within.entity.User;
import com.example.within.entity.UserRoleEnum;
import com.example.within.exception.ErrorException;
import com.example.within.exception.ErrorResponseDto;
import com.example.within.exception.ExceptionEnum;
import com.example.within.repository.UserRepository;
import com.example.within.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AmazonS3 amazonS3Client;


    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<?> signUp(UserRequestDto userRequestDto) {
        String username = userRequestDto.getUsername();
        String email = userRequestDto.getEmail();
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        Optional<User> check = userRepository.findByEmail(email);
        if (check.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ExceptionEnum.USERS_DUPLICATION));
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminKey().equals(ADMIN_TOKEN)) {
                throw new ErrorException(ExceptionEnum.TOKEN_NOT_FOUND);
            }
            userRoleEnum = UserRoleEnum.ADMIN;
        }
        User users = new User(username, email, password, userRoleEnum);
        userRepository.save(users);
        return ResponseEntity.ok(new UserStatusResponseDto(users.getUsername(), "회원가입 성공"));
    }

    @Transactional
    public ResponseEntity<?> login(UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        String email = userRequestDto.getEmail();
        String password = userRequestDto.getPassword();

        User users = userRepository.findByEmail(email).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(password, users.getPassword())) {
            throw new ErrorException(ExceptionEnum.INVALID_PASSWORD);
        }

        String token = jwtUtil.createToken(users.getId(), users.getUsername(), users.getEmail(), users.getRole());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(new UserStatusResponseDto(users.getUsername(), "로그인 성공"));
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getUserList(User users, Pageable pageable){
        return userRepository.selectAllUser(pageable);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserInfo(Long userId, User users) {
        User userNow = userRepository.findById(userId).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );

        if (StringUtils.pathEquals(users.getEmail(), userNow.getEmail())) {
            Optional<UserPageResponseDto> optionalUser = userRepository.selectUser(userId);
//            UserRoleEnum userRoleEnum = users.getRole();
            if (optionalUser.isPresent()) {
                return ResponseEntity.ok(optionalUser);
            } else {
                throw new ErrorException(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
            }
        } else {
            throw new ErrorException(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
        }
    }

    @Transactional
    public ResponseEntity<?> updateUserInfo(Long userId,
                                            UserPageRequestDto userPageRequestDto,
                                            User user,
                                            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        // userId로 업데이트할 User 개체를 db에서 가져옴
        User userUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ExceptionEnum.USER_NOT_FOUND));

        // 동일 사용자인지 확인
        if (!userUpdate.getId().equals(user.getId())) {
            throw new ErrorException(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
        }

        //User 클래스의 username 속성을 userPageRequestDto의 username값으로 설정
        userUpdate.setUsername(userPageRequestDto.getUsername());

        if (imageFile != null) {
            // Resize and compress the image file
            BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
            BufferedImage resizedImage = Scalr.resize(bufferedImage, 300);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "png", baos);
            baos.flush();
            byte[] compressedBytes = compressBytes(baos.toByteArray(), 0.7f, 3); // compress the image to have a file size of up to 3MB with 70% compression quality
            baos.close();

            // Upload the compressed image to Amazon S3
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String fileExtension = StringUtils.getFilenameExtension(fileName);
            String newFileName = UUID.randomUUID() + "." + fileExtension;
            String bucketName = "within-s3-bucket";
            String s3Key = "user-images/" + newFileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(compressedBytes.length);
            metadata.setContentType("image/png");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedBytes);
            amazonS3Client.putObject(bucketName, s3Key, inputStream, metadata);

            // Set the user's image path to the newly uploaded file name
            userUpdate.setImg(s3Key);
        }

        // Save the updated user to the repository
        userRepository.save(userUpdate);

        // Return the updated user object
        return ResponseEntity.ok(userUpdate);
    }
    private byte[] compressBytes(byte[] bytes, float quality, int maxSizeMB) throws IOException {
        float compressionRatio = 1.0f;
        int maxSizeBytes = maxSizeMB * 1024 * 1024;
        while (bytes.length * compressionRatio > maxSizeBytes) {
            compressionRatio -= 0.05f;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(quality * compressionRatio);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(bufferedImage, null, null), writeParam);
        writer.dispose();
        ios.flush();
        byte[] compressedBytes = baos.toByteArray();
        baos.close();
        return compressedBytes;
    }
}
