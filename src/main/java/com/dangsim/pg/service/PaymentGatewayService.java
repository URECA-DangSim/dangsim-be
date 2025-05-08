package com.dangsim.pg.service;

import com.dangsim.pg.dto.PaymentDetailDto;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.repository.PaymentGatewayRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 실제 Db에 저장 (dto -> entity)

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    private final PaymentGatewayRepository paymentGatewayRepository;

    public PaymentGateway savePayment(PaymentGateway paymentGateway) {
        return paymentGatewayRepository.save(paymentGateway);
    }
}
