package com.priyanshparekh.ping.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.priyanshparekh.ping.auth.dto.UserLoginRequest;
import com.priyanshparekh.ping.auth.dto.UserSignUpRequest;
import com.priyanshparekh.ping.user.User;
import com.priyanshparekh.ping.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    ObjectMapper objectMapper = new ObjectMapper();


    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }


    // sign up success with valid details
    @Test
    public void signup_returnSignupResponse_signupSuccess() throws Exception {
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .password("password")
                .build();

        String userSignUpRequestJson = objectMapper.writeValueAsString(userSignUpRequest);
        log.info("authIntegrationTests: signup_returnSignupResponse_signupSuccess: userSignUpRequestJson: {}", userSignUpRequestJson);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .header("Content-Type", "application/json")
                        .content(userSignUpRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    // sign up fails with missing details

    // sign up fails with existing user
    @Test
    public void signup_throwsUsernameExistsException_signupError() throws Exception {
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .name("John Doe")
                .email("john_doe@gmail.com")
                .password("password")
                .build();

        String userSignUpRequestJson = objectMapper.writeValueAsString(userSignUpRequest);

        userRepository.save(
                User.builder()
                        .name(userSignUpRequest.getName())
                        .email(userSignUpRequest.getEmail())
                        .password(userSignUpRequest.getPassword())
                        .build()
        );

        mockMvc.perform(post("/api/v1/auth/signup")
                        .header("Content-Type", "application/json")
                        .content(userSignUpRequestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409));
    }


    // login success with valid details
    @Test
    public void login_returnLoginResponse_loginSuccess() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("john_doe@gmail.com")
                .password("password")
                .build();

        String userLoginRequestJson = objectMapper.writeValueAsString(userLoginRequest);

        userRepository.save(
                User.builder()
                        .name("John Doe")
                        .email(userLoginRequest.getEmail())
                        .password(passwordEncoder.encode(userLoginRequest.getPassword()))
                        .build()
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    // login fails with invalid details
    @Test
    public void login_throwBadCredentials_loginError() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("john_doe@gmail.com")
                .password("123456")
                .build();

        String userLoginRequestJson = objectMapper.writeValueAsString(userLoginRequest);

        userRepository.save(
                User.builder()
                        .name("John Doe")
                        .email(userLoginRequest.getEmail())
                        .password(passwordEncoder.encode("password"))
                        .build()
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .header("Content-Type", "application/json")
                        .content(userLoginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }


    // login fails with non-existing user
    @Test
    public void login_throwBadCredentialsException_loginError() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("john_doe@gmail.com")
                .password("password")
                .build();

        String userLoginRequestJson = objectMapper.writeValueAsString(userLoginRequest);

        mockMvc.perform(post("/api/v1/auth/login")
                        .header("Content-Type", "application/json")
                        .content(userLoginRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }
    // login fails with missing details

}
