package com.dangsim.token.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

	REFRESH_TOKEN_NOT_FOUND("RefreshToken 없음. 다시 로그인하세요.", "TOKEN_001"),
	REFRESH_TOKEN_MISMATCH("RefreshToken 불일치. 다시 로그인 필요.", "TOKEN_002"),
	REFRESH_TOKEN_EXPIRED("RefreshToken 만료. 다시 로그인하세요.", "TOKEN_003");

	private final String message;
	private final String code;
}
