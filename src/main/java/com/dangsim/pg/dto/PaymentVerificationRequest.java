package com.dangsim.pg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentVerificationRequest {

    private String impUid;
    private String merchantUid;
    private Integer amount; // 결제 금액
    private Long taskId; // 게시글 id
}
