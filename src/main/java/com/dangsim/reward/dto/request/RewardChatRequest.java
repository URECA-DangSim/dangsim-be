package com.dangsim.reward.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RewardChatRequest { // 프론트 채팅방이 전달하는 id
    private Long chatId;
}
