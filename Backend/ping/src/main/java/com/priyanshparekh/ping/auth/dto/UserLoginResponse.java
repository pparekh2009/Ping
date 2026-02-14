package com.priyanshparekh.ping.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UserLoginResponse {

    private Long userId;
    private String accessToken;

}
