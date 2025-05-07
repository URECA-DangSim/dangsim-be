package com.dangsim.pg.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PortOneTokenResponse {
    private TokenResponse response;

    @Getter
    public static class TokenResponse {
        private String access_token;
        private long now;
        private long expired_at;
    }
}
