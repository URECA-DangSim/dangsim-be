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

    @PostMapping("/api/payments/completion")
    public ResponseEntity<PaymentResponse> completePayment(@RequestBody PortOneResponse paymentResponseDto) {

        paymentGatewayService.verifyPaymentDetail(paymentResponseDto.getImpUid());

        return ResponseEntity.ok()
                .body(new PaymentResponse(true, "결제 및 검증 성공"));
    }
}