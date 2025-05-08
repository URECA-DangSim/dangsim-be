package com.dangsim.pg.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayStatus {

	PG_FAIL("결제 실패"),
	PG_SUCCESS("결제 성공"),
	PG_PENDING("결제 대기"),
	PG_CANCELLED("결제 취소");

	private final String status;
}
