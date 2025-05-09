package com.dangsim.pg.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentGatewayStatus {
	READY,
	PAID,
	CANCELLED,
	FAILED;
}
