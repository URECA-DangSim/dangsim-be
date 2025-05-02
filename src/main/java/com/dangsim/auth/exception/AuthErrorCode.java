package com.dangsim.auth.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthErrorCode implements ErrorCode {

	private final String message;
	private final String code;
}
