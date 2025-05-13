package com.dangsim.rewardRefund.service;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.rewardRefund.dto.request.RewardRefundRequest;
import com.dangsim.rewardRefund.entity.RewardRefundStatus;
import com.dangsim.rewardRefund.entity.RewardRefundEntity;
import com.dangsim.rewardRefund.repository.RewardRefundRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.dangsim.rewardRefund.exception.RewardRefundErrorCode.*;


@Service
@RequiredArgsConstructor
public class RewardRefundService {

    private final UserRepository userRepository;
    private final RewardRefundRepository rewardRequestRepository;

    @Transactional
    public void requestRefund(Long userId, RewardRefundRequest requestDto) { // 환급 요청

        // 로그인한 사용자의 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(REFUND_USER_NOT_FOUND));

        BigDecimal requestAmount = BigDecimal.valueOf(requestDto.getAmount());

        // 요청 금액이 0 이하인 경우
        if (requestAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BaseException(REFUND_AMOUNT_ZERO);
        }

        // 요청 금액이 보유 리워드보다 크면 예외
        if (requestAmount.compareTo(user.getReward()) > 0) {
            throw new BaseException(REFUND_AMOUNT_EXCEEDS_BALANCE);
        }

        // 리워드 환급 요청 엔티티 생성
        RewardRefundEntity rewardRequest = RewardRefundEntity.of(
                user, // 현재 사용중인 user id (fk)
                requestAmount,
                requestDto.getBankName(),
                requestDto.getBankAccount(),
                requestDto.getHolderName(),
                RewardRefundStatus.SUCCESS
        );

        rewardRequestRepository.save(rewardRequest);

        // User 리워드 업데이트
        user.updateReward(user.getReward().subtract(requestAmount));
    }
}