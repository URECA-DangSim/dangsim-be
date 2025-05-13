package com.dangsim.rewardRefund.service;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.fixture.UserFixture;
import com.dangsim.rewardRefund.dto.request.RewardRefundRequest;
import com.dangsim.rewardRefund.entity.RewardRefundEntity;
import com.dangsim.rewardRefund.repository.RewardRefundRepository;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.dangsim.rewardRefund.exception.RewardRefundErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RewardRefundServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RewardRefundRepository rewardRefundRepository;

    @InjectMocks
    RewardRefundService rewardRefundService;

    @DisplayName("환급 요청 시 존재하지 않는 사용자이면 예외 발생")
    @Test
    void throwExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        BigDecimal requestAmount = BigDecimal.valueOf(1000);
        final String BANK_NAME = "은행";
        final String ACCOUNT_NUMBER = "123-456";
        final String ACCOUNT_HOLDER = "홍길동";

        RewardRefundRequest request = new RewardRefundRequest(requestAmount, BANK_NAME, ACCOUNT_NUMBER, ACCOUNT_HOLDER);

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> rewardRefundService.requestRefund(userId, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(REFUND_USER_NOT_FOUND.getMessage());
    }

    @DisplayName("환급 요청 시 요청 금액이 0 이하이면 예외 발생")
    @Test
    void throwExceptionWhenAmountIsZeroOrLess() {
        // given
        Long userId = 1L;
        BigDecimal requestAmount = BigDecimal.valueOf(0);
        final String BANK_NAME = "은행";
        final String ACCOUNT_NUMBER = "123-456";
        final String ACCOUNT_HOLDER = "홍길동";
        User user = UserFixture.user(Role.USER, BigDecimal.valueOf(5000));

        RewardRefundRequest request = new RewardRefundRequest(requestAmount, BANK_NAME, ACCOUNT_NUMBER, ACCOUNT_HOLDER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> rewardRefundService.requestRefund(userId, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(REFUND_AMOUNT_ZERO.getMessage());
    }

    @DisplayName("환급 요청 시 금액이 보유 리워드보다 크면 예외 발생")
    @Test
    void throwExceptionWhenAmountExceedsReward() {
        // given
        Long userId = 1L;
        BigDecimal requestAmount = BigDecimal.valueOf(5000);
        final String BANK_NAME = "은행";
        final String ACCOUNT_NUMBER = "123-456";
        final String ACCOUNT_HOLDER = "홍길동";

        User user = UserFixture.user(Role.USER, BigDecimal.valueOf(3000));
        RewardRefundRequest request = new RewardRefundRequest(requestAmount, BANK_NAME, ACCOUNT_NUMBER, ACCOUNT_HOLDER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> rewardRefundService.requestRefund(userId, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(REFUND_AMOUNT_EXCEEDS_BALANCE.getMessage());
    }

    @DisplayName("정상적인 환급 요청 시 리워드가 감소되고 요청 정보가 저장된다.")
    @Test
    void successRefundRequest() {
        // given
        Long userId = 1L;
        BigDecimal initialReward = BigDecimal.valueOf(10000);
        BigDecimal requestAmount = BigDecimal.valueOf(5000);
        final String BANK_NAME = "은행";
        final String ACCOUNT_NUMBER = "123-456";
        final String ACCOUNT_HOLDER = "홍길동";

        User user = UserFixture.user(Role.USER, initialReward);
        RewardRefundRequest request = new RewardRefundRequest(requestAmount, BANK_NAME, ACCOUNT_NUMBER, ACCOUNT_HOLDER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(rewardRefundRepository.save(any(RewardRefundEntity.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        rewardRefundService.requestRefund(userId, request);

        // then
        assertThat(user.getReward()).isEqualByComparingTo(initialReward.subtract(requestAmount));
        then(rewardRefundRepository).should(times(1)).save(any(RewardRefundEntity.class));
    }
}
