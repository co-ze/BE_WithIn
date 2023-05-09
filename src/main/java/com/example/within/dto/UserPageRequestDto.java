package com.example.within.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserPageRequestDto {

    private String username;
    private MultipartFile img;

}
