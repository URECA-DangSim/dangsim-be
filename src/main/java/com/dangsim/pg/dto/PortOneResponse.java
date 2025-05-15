package com.dangsim.pg.dto;

import lombok.Getter;

@Getter
public class PortOneResponse {
	private String impUid;        // 아임포트 결제 고유번호
	private String merchantUid;   // 주문 고유번호
	private Long taskId;
	private String buyer_name;
}