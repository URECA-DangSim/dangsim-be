package com.dangsim.pg.dto;

import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

// imp_uid 기반 조회값

@Getter
@NoArgsConstructor
public class PaymentDetailDto {
    private String pgTid; // pg사 거래번호
    private String currency;
    private String pay_method; // 결제수단 구분 코드
    private String cardCompany;
    private String cardNumberMasked;
    private BigDecimal amount;
    private Integer installment;
    PaymentGatewayStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime cancelledAt;

    @Builder(access = PRIVATE)
    // 250507 : method 다음 failReason
    private PaymentDetailDto(String pgTid, String currency, String pay_method, String cardCompany,
                             String cardNumberMasked, BigDecimal amount, Integer installment, PaymentGatewayStatus status,
                             LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime cancelledAt) {
        this.pgTid = pgTid;
        this.currency = currency;
        this.pay_method = pay_method;
        this.cardCompany = cardCompany;
        this.cardNumberMasked = cardNumberMasked;
        this.amount = amount;
        this.installment = (installment != null) ? installment : 0; // installment이 null일 경우 0으로 설정
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.cancelledAt = cancelledAt;
    }

    public static PaymentDetailDto of(PaymentGateway paymentGateway) {
        return PaymentDetailDto.builder()
                .pgTid(paymentGateway.getPgTid())
                .currency(paymentGateway.getCurrency())
                .pay_method(paymentGateway.getMethod())
                .cardCompany(paymentGateway.getCardCompany())
                .cardNumberMasked(paymentGateway.getCardNumberMasked())
                .amount(paymentGateway.getAmount())
                .installment(paymentGateway.getInstallment())
                .status(PaymentGatewayStatus.valueOf(paymentGateway.getStatus().name())) // enum이니까 .name() 호출
                .requestedAt(paymentGateway.getRequestedAt())
                .approvedAt(paymentGateway.getApprovedAt())
                .cancelledAt(paymentGateway.getCancelledAt())
                .build();
    }
}