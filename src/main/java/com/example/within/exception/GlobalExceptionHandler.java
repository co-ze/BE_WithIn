package com.example.within.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleErrorException(ErrorException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage(), e.getExceptionEnum().getStatus());
        return ResponseEntity.status(e.getExceptionEnum().getStatus()).body(errorResponseDto);
    }
}
