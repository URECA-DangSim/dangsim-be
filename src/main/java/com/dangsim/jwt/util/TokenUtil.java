package com.dangsim.jwt.util;

import static lombok.AccessLevel.*;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class TokenUtil {
	private static final String BEARER_PREFIX = "Bearer ";

	public static String extractToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
	}

}
