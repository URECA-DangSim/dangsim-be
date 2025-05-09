package com.dangsim.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.entity.User;
import com.dangsim.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 닉네임 중복 검사
	 */
	@GetMapping("/user/check-nickname")
	public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
		boolean isDuplicated = userService.isNicknameDuplicated(nickname);
		return ResponseEntity.ok(Map.of("isDuplicated", isDuplicated));
	}

	/**
	 * 추가 정보 입력
	 */
	@PostMapping("/user/extra-info")
	public ResponseEntity<ExtraInfoResponse> inputExtraInfo(@RequestBody ExtraInfoRequest request,
		@AuthenticationPrincipal User user) {
		ExtraInfoResponse response = userService.updateUserExtraInfo(user, request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 사용자 프로필 조회
	 */
	@GetMapping("/user/profile")
	public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal User user) {
		UserProfileResponse response = userService.getMemberProfile(user);
		return ResponseEntity.ok(response);
	}

}
