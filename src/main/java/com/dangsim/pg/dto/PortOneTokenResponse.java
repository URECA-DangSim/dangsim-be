package com.dangsim.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortOneTokenResponse {
    private int code;
    private String message;
    private TokenResponse response;

    @Getter
    public static class TokenResponse {
        private String access_token;
        private long now;
        private long expired_at;
    }
}
