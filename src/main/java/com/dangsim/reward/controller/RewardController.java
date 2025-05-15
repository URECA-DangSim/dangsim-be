package com.dangsim.reward.controller;

import com.dangsim.reward.dto.response.RewardChatResponse;
import com.dangsim.reward.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    // "심부름 수행 버튼 클릭시"
    @PostMapping("/api/reward/chat/{chatId}")
    public RewardChatResponse rewardToPerformer(@PathVariable Long chatId) {

        rewardService.updateRewardByTaskCompleteBtn(chatId);

        return rewardService.rewardByChatId(chatId);
    }
}