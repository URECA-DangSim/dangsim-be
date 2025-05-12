package com.dangsim.pg.service;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.payment.entity.Payment;
import com.dangsim.payment.repository.PaymentRepository;
import com.dangsim.pg.dto.InicisResponse;
import com.dangsim.pg.dto.PortOneTokenResponse;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import com.dangsim.pg.exception.PaymentGatewayErrorCode;
import com.dangsim.pg.repository.PaymentGatewayRepository;
import com.dangsim.task.entity.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dangsim.common.util.DateTimeFormatUtils.parseDateTimePG;

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    @Value("${portone.imp_key}")
    private String apiKey;

    @Value("${portone.imp_secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;

    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken"; // 포트원 토큰 발급 API URL : 여기에 post 요청

        Map<String, String> body = new HashMap<>(); // 서버에 보낼 데이터를 담을 Map
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        // http 요청 body에 json을 보내기 위해 Java 객체를 JSON 문자열로 변환
        // portone api는 json 형태를 요구하기 때문에 body를 json 형태로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new BaseException(PaymentGatewayErrorCode.JSON_CONVERT_FAILED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PortOneTokenResponse> response = restTemplate.postForEntity(url, entity, PortOneTokenResponse.class);
        // response : http 응답 전체를 감싸는 객체

        PortOneTokenResponse responseBody = response.getBody();
        if (responseBody == null || responseBody.getCode() != 0) {
            throw new BaseException(PaymentGatewayErrorCode.PAYMENT_VERIFICATION_FAILED);
        }

        return response.getBody().getResponse().getAccess_token();
    }

    @Transactional
    public void verifyPaymentDetail(String impUid) {
        String token = getAccessToken();

        String PORTONE_PAYMENT_LOOKUP_URL = "https://api.iamport.kr/payments/";
        String url = PORTONE_PAYMENT_LOOKUP_URL + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<InicisResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, InicisResponse.class);

        InicisResponse.Response paymentData = response.getBody().getResponse();

        BigDecimal portOneAmount = BigDecimal.valueOf(paymentData.getAmount());

//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new BaseException(PaymentGatewayErrorCode.PAYMENT_NOT_FOUND));

        Payment payment = paymentRepository.findByMerchantUid(paymentData.getMerchant_uid())
                .orElseThrow(() -> new BaseException(PaymentGatewayErrorCode.PAYMENT_NOT_FOUND));

        PaymentGateway paymentGateway = PaymentGateway.of(
                paymentData.getImp_uid(),
                paymentData.getMerchant_uid(),
                paymentData.getPay_method(),
                paymentData.getPg_provider(),
                paymentData.getPg_tid(),
                paymentData.getPg_id(),
                BigDecimal.valueOf(paymentData.getAmount()),
                paymentData.getCurrency(),
                paymentData.getApply_num(),
                paymentData.getBuyer_name(),
                paymentData.getCard_code(),
                paymentData.getCard_name(),
                paymentData.getCard_quota(),
                paymentData.getCard_number(),
                PaymentGatewayStatus.valueOf(paymentData.getStatus().toUpperCase()),
                paymentData.getCard_type(),
                parseDateTimePG(paymentData.getStarted_at()),
                parseDateTimePG(paymentData.getPaid_at()),
                paymentData.getCanceled_at() == null ? null : parseDateTimePG(paymentData.getCanceled_at()),
                parseDateTimePG(paymentData.getFailed_at()),
                payment
        );

        // 검증 후 엔티티 생성,만약 검증이 안되면 Transaction 롤백
        validatePaymentAmount(paymentGateway, portOneAmount);

        paymentGatewayRepository.save(paymentGateway);
    }

    public void validatePaymentAmount(PaymentGateway paymentGateway, BigDecimal portOneAmount) {
        if (paymentGateway == null) {
            throw new IllegalArgumentException("PaymentGateway 정보가 없습니다.");
        }

        Payment payment = paymentGateway.getPayment();
        if (payment == null) {
            throw new IllegalArgumentException("PaymentGateway에 연결된 Payment 정보가 없습니다.");
        }

        Task task = payment.getTask();
        if (task == null) {
            throw new IllegalArgumentException("Payment에 연결된 Task 정보가 없습니다.");
        }

        BigDecimal taskReward = task.getReward();
        if (taskReward == null || portOneAmount == null) {
            throw new IllegalArgumentException("Task 리워드 값 또는 결제 금액이 null입니다.");
        }

        if (taskReward.compareTo(portOneAmount) != 0) {
            throw new IllegalArgumentException("Task 리워드 금액과 결제 금액이 일치하지 않습니다.");
        }
    }
}