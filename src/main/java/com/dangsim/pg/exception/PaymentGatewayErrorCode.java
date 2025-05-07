package com.dangsim.pg.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
// todo : enum 타입으로 바꾸기
public class PaymentGatewayErrorCode implements ErrorCode {

	private final String message;
	private final String code;
}
