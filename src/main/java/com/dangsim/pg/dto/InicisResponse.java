package com.dangsim.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InicisResponse {

    private int code; // 응답코드 - 0이면 정상적인 조회, 아니면 Msg 확인 필요
    private String message;
    private Response response;

    @Getter
    public static class Response {
        private String imp_uid; // 포트원 결제 고유 uid
        private String merchant_uid; // 주문번호
        private String pay_method;
        private String pg_provider; // pg사 구분코드
        private String pg_tid; // pg사 거래 번호
        private String pg_id; // pg사 mid
        private int amount;
        private String currency; // 통화 구분
        private String apply_num; // 신용카드 승인번호
        private String buyer_name;
        private String card_code; // 카드사 코드 번호
        private String card_name; // 카드사명 - card company
        private String card_quota; // 할부개월 수
        private String card_number; // 마스킹 카드 번호
        private String card_type; // 신용카드 0, 체크카드 : 1
        private String status; // 결제상태 구분 코드

        private String started_at;
        private String paid_at;
        private String canceled_at;
        private String failed_at;
    }
}
