package com.dangsim.auth.dto.response;

public record AuthTokenResponse(
	String accessToken,
	String refreshToken,
	String role
) {
}
