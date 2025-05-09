package com.dangsim.pg.controller;

import com.dangsim.pg.dto.PaymentResponse;
import com.dangsim.pg.dto.PortOneResponse;
import com.dangsim.pg.repository.PaymentGatewayRepository;
import com.dangsim.pg.service.PaymentGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService paymentGatewayService;
    private final PaymentGatewayRepository paymentGatewayRepository;

    @PostMapping("/api/payments/completion")
    public ResponseEntity<PaymentResponse> completePayment(@RequestBody PortOneResponse paymentResponseDto) {

        // imp_uid를 보내주면 서비스에서 imp_uid 값을 기반으로 조회 및 처리
        paymentGatewayService.verifyPaymentDetail(paymentResponseDto.getAmount(), paymentResponseDto.getImpUid());

        return ResponseEntity.ok()
                .body(new PaymentResponse(true, "결제 및 검증 성공"));
    }
}