package com.dangsim.rewardRefund.service;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.rewardRefund.dto.request.RewardRefundRequest;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RewardRefundServiceTest {

    @Test
    void 환급_요청_성공() {
        // given
        Long userId = 1L;
        BigDecimal userReward = BigDecimal.valueOf(10000);
        BigDecimal requestAmount = BigDecimal.valueOf(5000);

        User user = new User();
        user.setId(userId);
        user.setReward(userReward);

        RewardRefundRequest dto = new RewardRefundRequest();
        dto.setAmount(requestAmount.longValue());
        dto.setBankName("신한");
        dto.setBankAccount("12345678");
        dto.setHolderName("홍길동");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        rewardRefundService.requestRefund(userId, dto);

        // then
        assertThat(user.getReward()).isEqualTo(BigDecimal.valueOf(5000));
    }

    @Test
    void 존재하지_않는_사용자() {
        // given
        Long userId = 1L;
        RewardRefundRequestDto dto = new RewardRefundRequestDto();
        dto.setAmount(1000L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> {
            rewardRefundService.requestRefund(userId, dto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(REFUND_USER_NOT_FOUND);
    }

    @Test
    void 환급_요청_금액이_보유금액_초과() {
        // given
        Long userId = 1L;
        BigDecimal userReward = BigDecimal.valueOf(1000);

        User user = new User();
        user.setId(userId);
        user.setReward(userReward);

        RewardRefundRequestDto dto = new RewardRefundRequestDto();
        dto.setAmount(2000L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> {
            rewardRefundService.requestRefund(userId, dto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(REFUND_AMOUNT_EXCEEDS_BALANCE);
    }

    @Test
    void 환급_요청_금액이_0원() {
        // given
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setReward(BigDecimal.valueOf(10000));

        RewardRefundRequestDto dto = new RewardRefundRequestDto();
        dto.setAmount(0L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        BaseException exception = assertThrows(BaseException.class, () -> {
            rewardRefundService.requestRefund(userId, dto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(REFUND_AMOUNT_ZERO);
    }
}
