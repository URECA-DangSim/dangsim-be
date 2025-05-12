package com.dangsim.pg.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayErrorCode implements ErrorCode {
	
	PAYMENT_VERIFICATION_FAILED("결제 검증에 실패했습니다.", "PG001"),
	PAYMENT_AMOUNT_MISMATCH("결제 금액이 일치하지 않습니다.", "PG002"),
	PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다.", "PG003"),
	JSON_CONVERT_FAILED("요청 데이터를 JSON으로 변환하는 데 실패했습니다.", "PG004"),
	TOKEN_ACCESS_FAILED("포트원으로 부터 토큰을 받지 못했습니다.", "PG005");

	private final String message;
	private final String code;
};