package com.dangsim.pg.service;

import com.dangsim.pg.dto.PortOneTokenResponse;
import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentVerifyService {

    @Value("${portone.imp_key}")
    private String apiKey;

    @Value("${portone.imp_secret}")
    private String apiSecret;

//    @PostConstruct
//    public void init() {
//        System.out.println("apiKey = " + apiKey);
//        System.out.println("apiSecret = " + apiSecret);
//    }

    @PostConstruct
    public void init() {
        System.out.println("== PortOneService 초기화 ==");
        System.out.println("apiKey: " + apiKey);
        System.out.println("apiSecret: " + apiSecret);
    }


    //    private static final RestTemplate restTemplate = new RestTemplate();
    private final RestTemplate restTemplate;


    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        Map<String, String> body = new HashMap<>();
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        ObjectMapper objectMapper = new ObjectMapper();
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

        System.out.println("response.getBody().getAccess_token() = " + response.getBody().getResponse().getAccess_token());
        System.out.println("response.getBody().getNow() = " + response.getBody().getResponse().getNow());
        System.out.println("response.getBody().getExpired_at() = " + response.getBody().getResponse().getExpired_at());

        // response.code != 0 ~~이렇게 해!! 예외를 던지던 뭐를 할게 예외처를 해줘야한다.
        // 0 : 으로 받아야 정상적으로 받은거

        return response.getBody().getResponse().getAccess_token();
//        return response.getBody().getAccess_token();
    }


    // imp_uid 기반 결제 금액 반환
    public int verifyPayment(String impUid) {
        String token = getAccessToken();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("response");

        return (int) responseData.get("amount"); // 결제된 실제 금액 (by imp_uid)
    }

    public PaymentGateway verifyPaymentDetail(String impUid) {
        String token =  getAccessToken();
        System.out.println("토큰 발급 성공!!!!!!!");
        System.out.println("token = " + token);
        
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

//        System.out.println("headers.get(\"Authorization\") = " + headers.get("Authorization"));
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        for(String s : response.getBody().split(",")) {
//            System.out.println("s = " + s);
//        }

        // 사용자 결제 금액이랑 포트원으로부터 받은 amount 값을 검증하면 된다.


        Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("response");

//        PaymentGateway paymentGateway = PaymentGateway.of(
        return PaymentGateway.of(
            (String) responseData.get("merchant_uid"),
            (String) responseData.get("imp_uid"),
            (String) responseData.get("pg_tid"),
            (String) responseData.get("currency"),
            (String) responseData.get("pay_method"),
            (String) responseData.get("card_name"),
            (String) responseData.get("card_number"),
            new BigDecimal(responseData.get("amount").toString()),
            (Integer) responseData.get("card_quota"),
            PaymentGatewayStatus.valueOf((String) responseData.get("status")),
            toLocalDateTime(responseData.get("requested_at")),
            toLocalDateTime(responseData.get("paid_at")),
            toLocalDateTime(responseData.get("cancelled_at"))
        );

        // 그리고 DTO로 변환해서 반환
//        return PaymentDetailDto.of(paymentGateway);
    }

    // 포트원 응답에서 오는 timestamp를 LocalDateTime 타입으로 변환
    private static LocalDateTime toLocalDateTime(Object timestamp) {
        if (timestamp == null) {
            return null;
        }
        long epochSecond = ((Number) timestamp).longValue();
        return LocalDateTime.ofEpochSecond(epochSecond, 0, java.time.ZoneOffset.UTC);
    }
}