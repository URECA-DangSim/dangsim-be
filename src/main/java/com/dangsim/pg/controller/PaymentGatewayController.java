package com.dangsim.pg.controller;

import com.dangsim.pg.dto.PaymentRequestDto;
import com.dangsim.pg.service.PaymentGatewayService;
import com.dangsim.pg.service.PaymentVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService paymentGatewayService;
    private final PaymentVerifyService paymentVerifyService;

    @PostMapping("/completion") // 결제 완료 후 받는 값
    public ResponseEntity<?> completePayment(@RequestBody PaymentRequestDto dto) { // 프론트에서
        // 1. 포트원 서버에 imp_uid로 결제된 금액 조회
        int paidAmount = paymentVerifyService.verifyPayment(dto.getImpUid());
        String userNickname = dto.getUserNickname();
        String taskTitle = dto.getTaskTitle();

        System.out.println("userNickname = " + userNickname);
        System.out.println("taskTitle = " + taskTitle);

        // 2. 프론트에서 받은 금액과 비교 (dto : 프론트에서 받은 금액)
        if (paidAmount != dto.getAmount()) {
            return ResponseEntity.badRequest().body("결제 금액 위조 의심 (검증 실패)");
        }

        // 3. 결제 정상 완료 → DB 저장
        // 서비스 호출
        paymentGatewayService.savePayment(dto, userNickname, taskTitle);


        return ResponseEntity.ok("결제 검증 및 저장 완료");
    }
}
