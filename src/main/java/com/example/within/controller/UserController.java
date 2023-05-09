package com.example.within.controller;

import com.example.within.security.UserDetailsImpl;
import com.example.within.dto.UserPageRequestDto;
import com.example.within.dto.UserRequestDto;
import com.example.within.dto.UserResponseDto;
import com.example.within.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/within")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDto userRequestDto){
        return userService.signUp(userRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody UserRequestDto userRequestDto, HttpServletResponse httpServletResponse){
        return userService.login(userRequestDto, httpServletResponse);
    }

    @GetMapping("/members")
    public Page<UserResponseDto> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        return userService.getUserList(userDetails.getUser(), pageable);
    }

    @GetMapping("/members/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getUserInfo(userId, userDetails.getUser());
    }

    @PutMapping(value = "/members/{userId}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateUserInfo(@PathVariable Long userId, @RequestBody UserPageRequestDto userPageRequestDto, @RequestParam("imageFile") MultipartFile imageFile, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
         return userService.updateUserInfo(userId, userPageRequestDto, userDetails.getUser(), imageFile);
    }
}
