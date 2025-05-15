package com.dangsim.rewardRefund.service;

import static com.dangsim.rewardRefund.exception.RewardRefundErrorCode.*;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.rewardRefund.dto.request.RewardRefundRequest;
import com.dangsim.rewardRefund.entity.RewardRefundEntity;
import com.dangsim.rewardRefund.entity.RewardRefundStatus;
import com.dangsim.rewardRefund.repository.RewardRefundRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardRefundService {

	private final UserRepository userRepository;
	private final RewardRefundRepository rewardRequestRepository;

	@Transactional
	public void requestRefund(Long userId, RewardRefundRequest requestDto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(REFUND_USER_NOT_FOUND));

		BigDecimal requestAmount = requestDto.getAmount();

		if (requestAmount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new BaseException(REFUND_AMOUNT_ZERO);
		}

		if (requestAmount.compareTo(user.getReward()) > 0) {
			throw new BaseException(REFUND_AMOUNT_EXCEEDS_BALANCE);
		}

		RewardRefundEntity rewardRequest = RewardRefundEntity.of(
			user,
			requestAmount,
			requestDto.getBankName(),
			requestDto.getBankAccount(),
			requestDto.getHolderName(),
			RewardRefundStatus.SUCCESS
		);

		rewardRequestRepository.save(rewardRequest);
		user.updateReward(user.getReward().subtract(requestAmount));
	}
}