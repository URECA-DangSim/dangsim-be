package com.dangsim.auth.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	INVALID_TOKEN("유효하지 않은 토큰입니다.", "AUTH_001"),
	EXPIRED_TOKEN("만료된 토큰입니다.", "AUTH_002"),
	UNAUTHORIZED_ACCESS("인가되지 않은 접근입니다.", "AUTH_003");

	private final String message;
	private final String code;
}
