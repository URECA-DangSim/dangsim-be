package com.dangsim.pg.dto;

import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import java.math.BigDecimal;

public class PaymentGatewayMapper {

    public static PaymentGateway toEntity(PaymentRequestDto dto, String userNickname, String taskTitle) {
        if (dto.getAmount() == null) {
            throw new NullPointerException("결제 금액(amount)이 null입니다.");
        }

        BigDecimal amount = BigDecimal.valueOf(dto.getAmount());

        return PaymentGateway.of(
                userNickname,
                taskTitle,
                dto.getMerchantUid(),
                dto.getImpUid(),
                dto.getPgTid(),
                dto.getCurrency(),
                dto.getMethod(),
//                dto.getFailReason(), // 가상 계좌
                dto.getCardCompany(),
                dto.getCardNumberMasked(),
                amount,
                dto.getInstallment(),
                PaymentGatewayStatus.PG_PENDING, // 기본 상태(ready?)
                dto.getRequestedAt(),
                dto.getApprovedAt(),
                dto.getCancelledAt()
        );
    }
}
