package com.dangsim.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.CheckNicknameResponse;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.dto.response.UserTaskResponse;
import com.dangsim.user.entity.User;
import com.dangsim.user.service.UserService;

import jakarta.validation.Valid;
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
	public ResponseEntity<CheckNicknameResponse> checkNickname(@RequestParam String nickname) {
		CheckNicknameResponse response = userService.isNicknameDuplicated(nickname);
		return ResponseEntity.ok(response);
	}

	/**
	 * 추가 정보 입력
	 */
	@PostMapping("/user/extra-info")
	public ResponseEntity<ExtraInfoResponse> inputExtraInfo(@Valid @RequestBody ExtraInfoRequest request,
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

	/**
	 * 사용자 프로필 수정
	 */
	@PutMapping("/user/extra-info")
	public ResponseEntity<ExtraInfoResponse> updateExtraInfo(@RequestBody ExtraInfoRequest request,
		@AuthenticationPrincipal User user) {
		ExtraInfoResponse response = userService.updateUserExtraInfo(user, request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 심부름 요청 내역
	 */
	@GetMapping("/tasks/requested")
	public ResponseEntity<CursorPageResponse<UserTaskResponse>> getRequestedTasks(
		@RequestParam(name = "cursor", required = false) String cursor,
		@RequestParam(name = "size", defaultValue = "20") int size,
		@AuthenticationPrincipal User user
	){
		CursorPageResponse<UserTaskResponse> response = userService.getRequestedTasks(cursor, size, user);
		return ResponseEntity.ok(response);
	}

	/**
	 * 심부름 수행 내역
	 */
	@GetMapping("/tasks/performed")
	public ResponseEntity<CursorPageResponse<UserTaskResponse>> getPerformedTasks(
		@RequestParam(name = "cursor", required = false) String cursor,
		@RequestParam(name = "size", defaultValue = "20") int size,
		@AuthenticationPrincipal User user
	){
		CursorPageResponse<UserTaskResponse> response = userService.getPerformedTasks(cursor, size, user);
		return ResponseEntity.ok(response);
	}
}
