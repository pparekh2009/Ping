package com.priyanshparekh.ping.auth;

import com.priyanshparekh.ping.error.exception.UsernameExistsException;
import com.priyanshparekh.ping.user.User;
import com.priyanshparekh.ping.auth.dto.UserLoginRequest;
import com.priyanshparekh.ping.auth.dto.UserLoginResponse;
import com.priyanshparekh.ping.auth.dto.UserSignUpRequest;
import com.priyanshparekh.ping.auth.dto.UserSignUpResponse;
import com.priyanshparekh.ping.security.JwtService;
import com.priyanshparekh.ping.security.PingUserDetails;
import com.priyanshparekh.ping.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserSignUpResponse signUp(UserSignUpRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(_ -> {
            throw new UsernameExistsException();
        });

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        PingUserDetails userDetails = (PingUserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);

        return new UserSignUpResponse(savedUser.getId(), jwtToken);
    }

    public UserLoginResponse login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        PingUserDetails userDetails = (PingUserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);
        log.info("authService: login: jwtToken: {}", jwtToken);

        return new UserLoginResponse(userDetails.getUser().getId(), jwtToken);
    }

}
