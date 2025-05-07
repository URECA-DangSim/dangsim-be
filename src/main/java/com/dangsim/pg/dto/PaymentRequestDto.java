package com.dangsim.pg.dto;

import com.dangsim.pg.entity.PaymentGatewayStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// 프론트의 요청을 담을 DTO

@Getter
@Setter
public class PaymentRequestDto {
    private String userNickname;
    private String taskTitle;
    private Integer amount;          // 요청 금액
    private String impUid;        // 아임포트 결제 고유번호
    private String merchantUid;   // 주문 고유번호
//    private String userEmail; // impUid로 확인 가능??

    private String pgTid;
    private String currency;
    private String method;
//    private String fail_reason; // 가상 계좌에서만 필요
    private String cardCompany;
    private String cardNumberMasked;
    private Integer installment; // 할부개월수

    PaymentGatewayStatus paymentGatewayStatus;

    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime cancelledAt;
}
