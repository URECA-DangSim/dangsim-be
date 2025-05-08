package com.dangsim.pg.controller;

import com.dangsim.pg.dto.PaymentDetailDto;
import com.dangsim.pg.dto.PaymentRequestDto;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.service.PaymentGatewayService;
import com.dangsim.pg.service.PaymentVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
//@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService paymentGatewayService;
    private final PaymentVerifyService paymentVerifyService;

    // 사용자의 요청을 받고 어떤 일을 해야 하는가

    // 결제 완료 후 검증 및 DB에 저장
    @PostMapping("api/payments/completion")
    public ResponseEntity<?> completePayment(@RequestBody PaymentRequestDto dto) { // 프론트에서 전달 받는 값
        // 1. 포트원에서 실제로 결제된 금액 (imp_uid로 부터 받은 금액)
//            int paidAmount = paymentVerifyService.verifyPayment(dto.getImpUid());
        BigDecimal paidAmount = paymentVerifyService.verifyPaymentDetail(dto.getImpUid()).getAmount();

        // 2. 1의 값과 프론트에서 사용자가 입력한 결제 기댓값과 비교
//            if (paidAmount != dto.getAmount()) {
        if (!Objects.equals(paidAmount, dto.getAmount())) {
            return ResponseEntity.badRequest().body("결제 금액 위조 의심 (검증 실패)");
        }

        // 3. 검증 후 imp_uid를 통해 다른 entity 값 조회
//        PaymentDetailDto paymentDetailDto = PaymentVerifyService.verifyPaymentDetail(dto.getImpUid());
//        paymentGatewayService.savePayment(paymentDetailDto);
        PaymentGateway paymentGateway = paymentVerifyService.verifyPaymentDetail(dto.getImpUid());
        PaymentGateway savedPayment = paymentGatewayService.savePayment(paymentGateway);
        System.out.println("savedPayment = " + savedPayment);
//        return ResponseEntity.ok("결제 검증 및 저장 완료");
        return ResponseEntity.ok("결제 검증 및 저장 완료 (ID: " + savedPayment.getId() + ")");
    }

    // imp_uid로 결제 상세 조회
    @GetMapping("payments/{impUid}")
    public ResponseEntity<?> getPaymentByImpUid(@PathVariable String impUid) {
        try {
            // PaymentGateway 객체를 반환받음 (DB저장용 Entity라면 DTO로 변환해서 보내는 게 더 좋음)
            var paymentDetail = paymentVerifyService.verifyPaymentDetail(impUid);
            return ResponseEntity.ok(paymentDetail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("결제 정보 조회 실패: " + e.getMessage());
        }
    }
}