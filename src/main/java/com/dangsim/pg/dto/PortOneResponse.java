package com.dangsim.pg.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PortOneResponse {
    private BigDecimal amount;
    private String impUid;        // 아임포트 결제 고유번호
    private String merchantUid;   // 주문 고유번호
}