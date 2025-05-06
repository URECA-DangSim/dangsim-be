package com.dangsim.auth.dto.request;

public record ReissueRequest(
	Long userId,
	String refreshToken
) {
}
