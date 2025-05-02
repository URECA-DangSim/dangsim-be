package com.dangsim.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

	PAYMENT_WAITING("결제 전"),
	PAYMENT_FAILED("결제 실패"),
	PAYMENT_SUCCESSES("결제 성공"),
	PAYMENT_CALCULATED("정산"),
	PAYMENT_REFUNDED("환불");

	private final String status;

}
