package com.dangsim.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.auth.dto.request.ReissueRequest;
import com.dangsim.auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * 카카오 로그인
	 */
	@GetMapping("/login/kakao")
	public ResponseEntity<Map<String, String>> kakaoLogin(@RequestParam("code") String code) {
		Map<String, String> tokens = authService.handleKakaoLogin(code);
		return ResponseEntity.ok(tokens);
	}

	/**
	 * 로그아웃
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		HttpServletRequest request,
		@RequestHeader("Authorization") String accessToken
	) {
		Long userId = (Long)request.getAttribute("userId");
		authService.logout(userId, accessToken);
		return ResponseEntity.ok().build();
	}

	/**
	 * 토큰 재발급
	 */
	@PostMapping("/reissue")
	public ResponseEntity<Map<String, String>> reissue(@RequestBody ReissueRequest request) {
		Map<String, String> tokens = authService.reissue(request.userId(), request.refreshToken());
		return ResponseEntity.ok(tokens);
	}
}