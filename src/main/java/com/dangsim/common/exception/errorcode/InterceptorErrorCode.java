package com.dangsim.common.exception.errorcode;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InterceptorErrorCode implements ErrorCode {

	WRONG_HEADER("권한 부여 헤더가 누락되었거나 잘못되었습니다.", "INTERCEPTOR_001");
	private final String message;
	private final String code;
}
