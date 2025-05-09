package com.dangsim.pg.controller;

import com.dangsim.pg.dto.PaymentResponse;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.repository.PaymentGatewayRepository;
import com.dangsim.pg.service.PaymentGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService paymentGatewayService;
    private final PaymentGatewayRepository paymentGatewayRepository;

    @PostMapping("/completion")
    public ResponseEntity<?> completePayment(@RequestBody PaymentResponse paymentResponseDto) {

        // imp_uid를 보내주면 서비스에서 imp_uid 값을 기반으로 조회 및 처리
        BigDecimal amount = paymentResponseDto.getAmount();
        PaymentGateway paymentGateway = paymentGatewayService.verifyPaymentDetail(amount, paymentResponseDto.getImpUid()); // todo 값을 받을 필요가 있나?

        return ResponseEntity.ok().body("성공"); // 검증 후 DB 저장
    }
}