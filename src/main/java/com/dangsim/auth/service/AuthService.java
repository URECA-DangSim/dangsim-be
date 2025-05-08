package com.dangsim.auth.service;

import static com.dangsim.auth.entity.AuthProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.auth.dto.response.AuthTokenResponse;
import com.dangsim.auth.dto.response.KakaoUserResponse;
import com.dangsim.token.service.TokenService;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoOauthClient kakaoOauthClient;
	private final AuthProcessor authProcessor;
	private final TokenService tokenService;

	@Transactional
	public AuthTokenResponse handleKakaoLogin(String code) {
		String accessToken = kakaoOauthClient.getAccessToken(code);
		KakaoUserResponse userInfo = kakaoOauthClient.getUserInfo(accessToken);
		User user = authProcessor.process(KAKAO, userInfo);
		return tokenService.issueTokensFor(user);
	}

}
