package com.dangsim.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.dangsim.auth.dto.response.KakaoUserResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoOauthClient {

	private final WebClient webClient;

	@Value("${oauth.kakao.token-uri}")
	private String kakaoTokenUri;

	@Value("${oauth.kakao.user-info-uri}")
	private String kakaoUserInfoUri;

	@Value("${oauth.kakao.client-id}")
	private String kakaoClientId;

	@Value("${oauth.kakao.client-secret}")
	private String kakaoClientSecret;

	@Value("${oauth.kakao.redirect-uri}")
	private String kakaoRedirectUri;

	public String getAccessToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", kakaoClientId);
		params.add("redirect_uri", kakaoRedirectUri);
		params.add("client_secret", kakaoClientSecret);
		params.add("code", code);

		return webClient.post()
			.uri(kakaoTokenUri)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(params)
			.retrieve()
			.bodyToMono(Map.class)
			.map(body -> (String)body.get("access_token"))
			.block();
	}

	@Transactional(readOnly = true)
	public KakaoUserResponse getUserInfo(String accessToken) {
		return webClient.get()
			.uri(kakaoUserInfoUri)
			.headers(headers -> headers.setBearerAuth(accessToken))
			.retrieve()
			.bodyToMono(KakaoUserResponse.class)
			.block();
	}
}
