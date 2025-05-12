package com.dangsim.jwt.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
	TOKEN_IS_EMPTY("JWT TOKEN이 비어있습니다.", "JWTTOKEN_001"),
	INVALID_TOKEN("유효하지 않은 JWT TOKEN 입니다.", "JTWTOKEN_002");

	private final String message;
	private final String code;
}
