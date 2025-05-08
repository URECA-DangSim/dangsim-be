package com.dangsim.token.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
	REFRESH_TOKEN_NOT_FOUND("TOKEN_001", "RefreshToken 없음. 다시 로그인하세요."),
	REFRESH_TOKEN_MISMATCH("TOKEN_002", "RefreshToken 불일치. 다시 로그인 필요."),
	REFRESH_TOKEN_EXPIRED("TOKEN_003", "RefreshToken 만료. 다시 로그인하세요.");

	private final String message;
	private final String code;
}
