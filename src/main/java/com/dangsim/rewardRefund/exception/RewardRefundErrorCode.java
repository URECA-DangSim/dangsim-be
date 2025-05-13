package com.dangsim.rewardRefund.exception;

import com.dangsim.common.exception.runtime.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardRefundErrorCode implements ErrorCode {

    REFUND_AMOUNT_ZERO("요청한 환급 금액이 0원입니다", "RR001"),
    REFUND_USER_NOT_FOUND("회원 정보를 찾을 수 없습니다.", "RR002"),
    REFUND_INVALID_ACCOUNT_NUMBER("유효하지 않은 계좌번호 입니다.", "RR003"),
    REFUND_AMOUNT_EXCEEDS_BALANCE("보유 리워드를 초과할 수 없습니다.", "RR004");

    private final String message;
    private final String code;
};