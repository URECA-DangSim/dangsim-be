package com.dangsim.pg.dto;

import lombok.Getter;

import java.math.BigDecimal;

// 프론트에서 전달 받은 값을 처리하는 중간다리 역할

@Getter
public class PaymentResponse {
    private BigDecimal amount;          // 요청 금액 : 사용자가 프론트 화면에서 입력한 정보
    private String impUid;        // 아임포트 결제 고유번호
    private String merchantUid;   // 주문 고유번호
}