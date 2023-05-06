package com.example.within.service;

import com.example.within.dto.UserPageRequestDto;
import com.example.within.dto.UserStatusResponseDto;
import com.example.within.dto.UserRequestDto;
import com.example.within.dto.UserResponseDto;
import com.example.within.entity.User;
import com.example.within.entity.UserRoleEnum;
import com.example.within.exception.ErrorException;
import com.example.within.exception.ErrorResponseDto;
import com.example.within.exception.ExceptionEnum;
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
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
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

        String token = jwtUtil.createToken(users.getEmail(), users.getRole());
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
    public ResponseEntity<?> updateUserInfo(Long userId, UserPageRequestDto userPageRequestDto, User users){


        return ResponseEntity.ok(userRepository.selectUser(userId));
    }
}
