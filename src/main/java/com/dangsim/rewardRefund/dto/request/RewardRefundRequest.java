package com.dangsim.rewardRefund.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RewardRefundRequest {
    private BigDecimal amount;
    private String bankName;
    private String bankAccount;
    private String holderName;
}
