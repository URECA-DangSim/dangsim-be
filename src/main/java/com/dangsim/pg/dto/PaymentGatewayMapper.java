//package com.dangsim.pg.dto;
//
//import com.dangsim.pg.entity.PaymentGateway;
//import com.dangsim.pg.entity.PaymentGatewayStatus;
//import java.math.BigDecimal;
//
//// eto -> entity
//
//public class PaymentGatewayMapper {
//
//    public static PaymentGateway toEntity(PaymentDetailDto dto) {
//        if (dto.getAmount() == null) {
//            throw new NullPointerException("결제 금액(amount)이 null입니다.");
//        }
//
//        BigDecimal amount = BigDecimal.valueOf(dto.getAmount());
//
//        return PaymentGateway.of(
//                .pgTid(dto.getPgTid())
//                .currency(dto.getCurrency())
//                .payMethod(dto.getPayMethod())
//                .cardName(dto.getCardName())
//                .cardNumber(dto.getCardNumber())
//                .amount(dto.getAmount())
//                .cardQuota(dto.getCardQuota())
//                .status(dto.getStatus())
//                .requestedAt(dto.getRequestedAt())
//                .paidAt(dto.getPaidAt())
//                .cancelledAt(dto.getCancelledAt()
//                );
//    }
//
//        return PaymentGateway.of(
//                dto.getPgTid(),
//                dto.getCurrency(),
//                dto.getMethod(),
////                dto.getFailReason(), // 가상 계좌
//                dto.getCardCompany(),
//                dto.getCardNumberMasked(),
//                amount,
//                dto.getInstallment(),
//                PaymentGatewayStatus.PG_PENDING, // 기본 상태(ready?)
//                dto.getRequestedAt(),
//                dto.getApprovedAt(),
//                dto.getCancelledAt()
//        );
//    }
//}
