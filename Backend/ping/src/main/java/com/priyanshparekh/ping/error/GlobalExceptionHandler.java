package com.priyanshparekh.ping.error;

import com.priyanshparekh.ping.error.exception.UsernameExistsException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = UsernameExistsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUsernameExistsException(UsernameExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .code(HttpStatus.CONFLICT.value())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(exception = BadCredentialsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.info("globalExceptionHandler: handleBadCredentialsException: message: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .code(HttpStatus.BAD_REQUEST.value())
                                .message("Invalid email or password")
                                .build()
                );
    }

}
