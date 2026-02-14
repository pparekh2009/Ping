package com.priyanshparekh.ping.auth.dto;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UserSignUpRequest {

    private String name;
    private String email;
    private String password;

}
