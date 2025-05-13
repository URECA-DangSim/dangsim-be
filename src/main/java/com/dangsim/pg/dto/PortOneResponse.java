package com.dangsim.pg.dto;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class PortOneResponse {
	private BigDecimal amount;
	private String impUid;        // 아임포트 결제 고유번호
	private String merchantUid;   // 주문 고유번호
}