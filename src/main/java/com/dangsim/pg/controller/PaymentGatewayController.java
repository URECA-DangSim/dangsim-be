package com.dangsim.pg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.pg.dto.PaymentResponse;
import com.dangsim.pg.dto.PortOneResponse;
import com.dangsim.pg.service.PaymentGatewayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayController {

	private final PaymentGatewayService paymentGatewayService;

	@PostMapping("/api/payments/validation")
	public ResponseEntity<PaymentResponse> completePayment(@RequestBody PortOneResponse portOneResponseDto) {

		paymentGatewayService.verifyPaymentDetail(portOneResponseDto.getImpUid(), portOneResponseDto.getMerchantUid());

		// 결제 성공 시 결제 및 테스크 상태 업데이트
		paymentGatewayService.updatePaymentAndTaskStatus(portOneResponseDto.getMerchantUid());

		return ResponseEntity.ok()
			.body(new PaymentResponse(true, "결제 및 검증 성공", portOneResponseDto.getTaskId()));
	}
}