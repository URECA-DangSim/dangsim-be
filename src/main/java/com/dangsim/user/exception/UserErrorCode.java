package com.dangsim.user.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	USER_NOT_FOUND("존재하지 않는 사용자입니다.", "U_004");

	private final String message;
	private final String code;
}
