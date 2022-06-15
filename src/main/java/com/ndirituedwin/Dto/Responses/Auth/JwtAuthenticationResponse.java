package com.ndirituedwin.Dto.Responses.Auth;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class JwtAuthenticationResponse {


    private String accessToken;
    private String refreshToken;
    private String tokenType="Bearer";
    private String username;
    private Instant expirationTime;
    private String message;
}
