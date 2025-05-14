package com.dangsim.rewardRefund.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RewardRefundResponse {
    private boolean success;
    private String message;
}