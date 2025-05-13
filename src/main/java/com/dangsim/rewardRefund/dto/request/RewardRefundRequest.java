package com.dangsim.rewardRefund.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RewardRefundRequest {
    private int amount;
    private String bankName;
    private String bankAccount;
    private String holderName;
}
