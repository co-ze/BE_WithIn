package com.example.within.controller;

import com.example.within.Security.UserDetailsImpl;
import com.example.within.dto.UserRequestDto;
import com.example.within.dto.UserResponseDto;
import com.example.within.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/within/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequestDto userRequestDto){
        return userService.signUp(userRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody UserRequestDto userRequestDto, HttpServletResponse httpServletResponse){
        return userService.login(userRequestDto, httpServletResponse);
    }

    @GetMapping("/members")
    public List<UserResponseDto> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getUserList(userDetails.getUser());
    }
}
