package com.example.within.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {


    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소를 입력해주세요.")
    @NotNull(message = "E-mail을 입력해주세요.")
    private String email;

    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자에서 15자 사이로만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z\\p{Punct}0-9]*$", message = "비밀번호는 대문자,소문자,숫자,특수문자만 가능합니다.")
//    ^ : 문자열 시작 $ : 문자열 끝
//    [a-zA-Z\\p{Punct}] : 알파벳 대소문자와 특수문자를 허용
//    \\p{Punct}는 Unicode punctuation character를 나타내며, 이에는 대부분의 특수문자가 포함
//    *로 이 문자열이 0개 이상인지 여부, 공백을 포함하지 않음
    @NotNull(message = "비밀번호를 입력해주세요")
    private String password;

    private boolean admin = false;
    private String adminKey = "";
}