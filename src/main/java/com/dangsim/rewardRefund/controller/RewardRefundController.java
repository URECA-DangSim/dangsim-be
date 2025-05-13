package com.dangsim.rewardRefund.controller;

import com.dangsim.rewardRefund.dto.request.RewardRefundRequest;
import com.dangsim.rewardRefund.dto.response.RewardRefundResponse;
import com.dangsim.rewardRefund.service.RewardRefundService;
import com.dangsim.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RewardRefundController {

    private final RewardRefundService rewardRefundServiceService;

    @PostMapping("api/users/user/reward") // Http post 요청을 받을 엔드포인트
    public ResponseEntity<RewardRefundResponse> requestReward(@RequestBody RewardRefundRequest requestDto, @AuthenticationPrincipal User user) {
        // 프론트에서 넘어온 정보를 RewardRefundRequestDto로 데이터 전달, @Auth ~ 를 통해 현재 로그인된 사용자에 대해서만 동작하도록

        rewardRefundServiceService.requestRefund(user.getId(), requestDto);

        return ResponseEntity.ok().body(new RewardRefundResponse(true, "환급 완료"));
    }

}
