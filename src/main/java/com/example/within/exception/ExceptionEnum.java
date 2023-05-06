package com.example.within.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    USERS_DUPLICATION("중복된 username 입니다.", HttpStatus.BAD_REQUEST.value()),
    USER_NOT_FOUND("등록된 아이디가 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()),
    INVALID_INPUT("관리자 암호를 잘못 입력하셨습니다.",HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    TOKEN_NOT_FOUND("유효하지 않은 토큰입니다.", HttpStatus.NOT_FOUND.value()),
    NOT_AUTHORIZATION("권한이 없습니다.", HttpStatus.BAD_REQUEST.value()),
    NOT_ALLOWED_AUTHORIZATIONS("작성자만 삭제,수정 할 수 있습니다.", HttpStatus.FORBIDDEN.value()),
    BOARD_NOT_FOUND("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()),
    COMMENT_NOT_FOUND("해당 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND.value()),
    INTERNAL_SERVER_ERROR("서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());


    private final String message;
    private final int status;

    ExceptionEnum(String message, int status){
        this.message = message;
        this.status = status;
    }
}