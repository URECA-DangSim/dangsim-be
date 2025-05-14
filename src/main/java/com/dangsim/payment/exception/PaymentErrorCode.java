package com.dangsim.payment.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

	NOT_FOUND_PAYMENT("해당하는 결제를 찾을 수 없습니다.", "PAYMENT_001"),
	PERFORMER_ALREADY_EXISTS("수행자가 이미 존재합니다.", "PAYMENT_002"),
	NOT_MATCH_PAYMENT_STATUS("매칭할 수 없는 결제 상태입니다.", "PAYMENT_003"),
	;

	private final String message;
	private final String code;
}
