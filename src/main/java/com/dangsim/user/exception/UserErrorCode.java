package com.dangsim.user.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	NICKNAME_REQUIRED("닉네임은 필수 입력 항목입니다.", "U_001"),
	NICKNAME_LENGTH_INVALID("닉네임은 최소 2자리 이상 최대 12자 이하입니다.", "U_002"),
	NICKNAME_DUPLICATED("이미 사용 중인 닉네임입니다.", "U_003"),
	USER_NOT_FOUND("존재하지 않는 사용자입니다.", "U_004");

	private final String message;
	private final String code;
}
