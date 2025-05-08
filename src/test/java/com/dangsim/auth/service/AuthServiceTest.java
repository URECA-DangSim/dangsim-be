package com.dangsim.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dangsim.auth.dto.response.AuthTokenResponse;
import com.dangsim.auth.dto.response.KakaoUserResponse;
import com.dangsim.auth.entity.AuthProvider;
import com.dangsim.token.service.TokenService;
import com.dangsim.user.entity.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private KakaoOauthClient kakaoOauthClient;
	@Mock
	private AuthProcessor authProcessor;
	@Mock
	private TokenService tokenService;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("카카오 로그인 시 사용자 정보를 기반으로 토큰을 발급한다.")
	void handleKakaoLogin_success() {
		//given
		String code = "auth-code";
		String accessToken = "auth-access-token";
		KakaoUserResponse.KakaoProfile profile = new KakaoUserResponse.KakaoProfile("https://test.com/image.png");
		KakaoUserResponse.KakaoAccount account = new KakaoUserResponse.KakaoAccount(profile);
		KakaoUserResponse userInfo = new KakaoUserResponse(123L, account);
		User user = mock(User.class);
		AuthTokenResponse expected = new AuthTokenResponse("access", "refresh", "USER");

		when(kakaoOauthClient.getAccessToken(code)).thenReturn(accessToken);
		when(kakaoOauthClient.getUserInfo(accessToken)).thenReturn(userInfo);
		when(authProcessor.process(AuthProvider.KAKAO, userInfo)).thenReturn(user);
		when(tokenService.issueTokensFor(user)).thenReturn(expected);

		//when
		AuthTokenResponse response = authService.handleKakaoLogin(code);

		//then
		assertThat(response).isEqualTo(expected);

	}

}