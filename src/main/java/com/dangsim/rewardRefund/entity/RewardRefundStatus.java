package com.dangsim.rewardRefund.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardRefundStatus {

	PENDING("대기중"),
	SUCCESS("완료");

	private final String rewardRefundStatus;
}
