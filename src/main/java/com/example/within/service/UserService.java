package com.example.within.service;

import com.example.within.dto.*;
import com.example.within.entity.User;
import com.example.within.entity.UserRoleEnum;
import com.example.within.exception.ErrorException;
import com.example.within.exception.ErrorResponseDto;
import com.example.within.exception.ExceptionEnum;
import com.example.within.repository.EmotionRepository;
import com.example.within.repository.UserRepository;
import com.example.within.util.FileUploadUtil;
import com.example.within.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        String token = jwtUtil.createToken(users.getUsername(), users.getEmail(), users.getRole());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(new UserStatusResponseDto(users.getUsername(), "로그인 성공"));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUserList(User users){
        return userRepository.selectAllUser();
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
                                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // MultipartFile imageFile input으로 사용

        // userId로 업데이트할 User 개체를 db에서 가져옴
        User userUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ExceptionEnum.USER_NOT_FOUND));

        // 동일 사용자인지 확인
        if (!userUpdate.getId().equals(user.getId())) {
            throw new ErrorException(ExceptionEnum.NOT_ALLOWED_AUTHORIZATIONS);
        }

        //User 클래스의 username 속석을 userPageRequestDto의 username값으로 설정
        userUpdate.setUsername(userPageRequestDto.getUsername());

        // Save the image file to disk
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String fileExtension = StringUtils.getFilenameExtension(fileName); // StringUtils 클래스를 다시 사용하여서 정리된 파일 이름에서 파일 확장자 추출
        String newFileName = UUID.randomUUID() + "." + fileExtension; // 업로드된 파일에 대한 고유한 새 파일 이름 생성. 임의의 UUID 생성 후 끝에 fileExtension 추가
        String uploadDir = "./user-images/"; // directory 설정
        FileUploadUtil.saveFile(uploadDir, newFileName, imageFile); //파일 저장

        // Set the user's image path to the newly uploaded file name
        userUpdate.setImg(newFileName);
        userUpdate.setUsername(userPageRequestDto.getUsername());
        userRepository.save(userUpdate);

        // Return the updated user object
        return ResponseEntity.ok(userUpdate);
        // StringUtils 클래스를 사용하여 업로드 된 파일의 파일 이름을 정리 및 불필요한 문자나 피알 경로 정보 제거
    }
}
