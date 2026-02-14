package com.priyanshparekh.ping.auth;

import com.priyanshparekh.ping.auth.dto.UserLoginRequest;
import com.priyanshparekh.ping.auth.dto.UserLoginResponse;
import com.priyanshparekh.ping.auth.dto.UserSignUpRequest;
import com.priyanshparekh.ping.auth.dto.UserSignUpResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<@NonNull UserSignUpResponse> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        UserSignUpResponse signUpUser = authService.signUp(userSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpUser);
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse loginUser = authService.login(userLoginRequest);
        return ResponseEntity.ok(loginUser);
    }

}
