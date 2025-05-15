package com.dangsim.reward.dto.response;

import com.dangsim.reward.entity.Reward;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class RewardChatResponse {
    private Long performerId;
    private BigDecimal beforeReward;
    private BigDecimal amount;
    private BigDecimal afterReward;
    private LocalDateTime completedAt;

    public static RewardChatResponse from(Reward statement) {
        return RewardChatResponse.builder()
                .performerId(statement.getUser().getId())
                .beforeReward(statement.getBeforeReward())
                .amount(statement.getAmount())
                .afterReward(statement.getBeforeReward().add(statement.getAmount()))
                .completedAt(statement.getCompletedAt())
                .build();
    }
}
