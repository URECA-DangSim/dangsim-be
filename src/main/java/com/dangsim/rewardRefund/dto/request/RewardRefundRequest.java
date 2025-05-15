package com.dangsim.rewardRefund.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RewardRefundRequest {
	private BigDecimal amount;
	private String bankName;
	private String bankAccount;
	private String holderName;
}
