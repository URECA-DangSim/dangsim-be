package com.dangsim.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

	PAYMENT_WAITING("결제 전"), // ready
	PAYMENT_FAILED("결제 실패"), // failed
	PAYMENT_SUCCESSES("결제 성공"), // paid
	PAYMENT_CALCULATED("정산"),
	PAYMENT_REFUNDED("환불");

//	ready,
//	paid,
//	cancelled,
//	failed;

	private final String status;

}
