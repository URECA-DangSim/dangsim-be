package com.dangsim.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.auth.dto.request.ReissueRequest;
import com.dangsim.auth.dto.response.AuthTokenResponse;
import com.dangsim.auth.service.AuthService;
import com.dangsim.token.service.TokenService;
import com.dangsim.user.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final TokenService tokenService;

	/**
	 * 카카오 로그인
	 */
	@GetMapping("/login/kakao")
	public ResponseEntity<AuthTokenResponse> kakaoLogin(@RequestParam("code") String code) {
		AuthTokenResponse response = authService.handleKakaoLogin(code);
		return ResponseEntity.ok(response);
	}

	/**
	 * 로그아웃
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		@AuthenticationPrincipal User user
	) {
		tokenService.logout(user.getId());
		return ResponseEntity.ok().build();
	}

	/**
	 * 토큰 재발급
	 */
	@PostMapping("/reissue")
	public ResponseEntity<AuthTokenResponse> reissue(@Valid @RequestBody ReissueRequest request) {
		AuthTokenResponse response = tokenService.reissue(request.userId(), request.refreshToken());
		return ResponseEntity.ok(response);
	}
}