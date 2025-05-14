package com.dangsim.rewardRefund.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardRefundErrorCode implements ErrorCode {

	REFUND_AMOUNT_ZERO("환급 금액은 1원 이상이어야 합니다.", "RR001"),
	REFUND_USER_NOT_FOUND("사용자를 찾을 수 없습니다.", "RR002"),
	REFUND_INVALID_ACCOUNT_NUMBER("유효하지 않은 계좌번호 입니다.", "RR003"),
	REFUND_AMOUNT_EXCEEDS_BALANCE("보유한 리워드보다 많은 금액을 환급할 수 없습니다.", "RR004"),
	REFUND_REQUEST_NOT_FOUND("환급 요청을 찾을 수 없습니다.", "RR005"),
	REFUND_ALREADY_PROCESSED("이미 처리된 환급요청 입니다.", "RR006");

	private final String message;
	private final String code;
};