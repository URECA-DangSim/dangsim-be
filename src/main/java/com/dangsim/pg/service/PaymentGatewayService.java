package com.dangsim.pg.service;

import com.dangsim.pg.dto.InicisResponse;
import com.dangsim.pg.dto.PortOneTokenResponse;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import com.dangsim.pg.repository.PaymentGatewayRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dangsim.common.util.DateTimeFormatUtils.parseDateTime;

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    @Value("${portone.imp_key}")
    private String apiKey;

    @Value("${portone.imp_secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
//        System.out.println("apiKey = " + apiKey);
//        System.out.println("apiSecret = " + apiSecret);
    }

    private final RestTemplate restTemplate;

    private final PaymentGatewayRepository paymentGatewayRepository;

    // 1. token 요청
    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        ObjectMapper objectMapper = new ObjectMapper(); // json 형태로 직렬화
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PortOneTokenResponse> response = restTemplate.postForEntity(url, entity, PortOneTokenResponse.class);
        // response : http 응답 전체를 감싸는 객체

        // TODO 만약 access_token을 요청했는데 반환값이 code != 0 일 때 예외 처리

        return response.getBody().getResponse().getAccess_token();
    }

    @Transactional
    public PaymentGateway verifyPaymentDetail(BigDecimal clientAmount, String impUid) {
        String token = getAccessToken();

//        System.out.println("token = " + token);

        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<InicisResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, InicisResponse.class); // 포트원에 요청 후 응답받음

        InicisResponse.Response paymentData = response.getBody().getResponse();

        // 포트원 서버에서 받은 결제 금액
        BigDecimal portOneAmount = BigDecimal.valueOf(paymentData.getAmount());

        // 1. 프론트에서 받은 금액과 서버에서 조회한 금액 비교
        if (clientAmount.compareTo(portOneAmount) != 0) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }
        // 여기서 우선 200 return

        // 2. 금액이 같으면 PaymentGateway 엔티티 생성
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
//                Integer.valueOf(paymentData.getCard_quota()),
                paymentData.getCard_quota(),
                paymentData.getCard_number(),
                PaymentGatewayStatus.valueOf(paymentData.getStatus().toUpperCase()),
                paymentData.getCard_type(),
                parseDateTime(paymentData.getStarted_at()),
                parseDateTime(paymentData.getPaid_at()),
                parseDateTime(paymentData.getCanceled_at()),
                parseDateTime(paymentData.getFailed_at())
        );
        return paymentGatewayRepository.save(paymentGateway);
//        PaymentGateway savedPayment = paymentGatewayRepository.save(paymentGateway);
//        paymentGatewayRepository.flush();  // 추가
//        return savedPayment;
    }
}