package com.dangsim.pg.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayStatus {

	READY("결제 전"),
	PAID("결제 완료"),
	CANCELLED("결제 취소"),
	FAILED("결제 실패");

	private final String pgStatus;

}
