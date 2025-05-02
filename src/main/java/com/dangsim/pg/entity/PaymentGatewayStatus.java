package com.dangsim.pg.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayStatus {

	PG_FAIL("결제 실패"),
	PG_SUCCESS("결제 성공");

	private final String status;
}
