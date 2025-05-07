package com.dangsim.pg.service;

import com.dangsim.pg.dto.PaymentGatewayMapper;
import com.dangsim.pg.dto.PaymentRequestDto;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import com.dangsim.pg.repository.PaymentGatewayRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// 실제 Db에 저장

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    private final PaymentGatewayRepository paymentGatewayRepository;

//    public PaymentGateway savePayment(PaymentRequestDto dto) {
//        PaymentGateway paymentGateway = PaymentGateway.builder()
//                .userNickname(dto.getUserNickname())
//                .taskTitle(dto.getTaskTitle())
//                .amount(dto.getAmount())
//                .impUid(dto.getImpUid())
//                .merchantUid(dto.getMerchantUid())
//                .status(PaymentGatewayStatus.PG_SUCCESS) // 검증 성공 시
//                .build();
//        return paymentGatewayRepository.save(paymentGateway);
//    }

    @Transactional
    public void savePayment(PaymentRequestDto dto, String userNickname, String taskTitle) {
        // DTO -> Entity 변환
        PaymentGateway paymentGateway = PaymentGatewayMapper.toEntity(dto, userNickname, taskTitle);

        // 저장
        paymentGatewayRepository.save(paymentGateway);
    }

    // 결제 상태 업데이트
//    @Transactional
//    public void updatePaymentStatus(String impUid, PaymentGatewayStatus status) {
//        PaymentGateway paymentGateway = paymentGatewayRepository
//                .findByImpUid(impUid)
//                .orElseThrow(() -> new RuntimeException("결제 내역을 찾을 수 없습니다"));
//
//        paymentGateway.setStatus(status); // 상태 변경
//        paymentGatewayRepository.save(paymentGateway); // 저장
//    }


}