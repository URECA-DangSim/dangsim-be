package com.dangsim.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dangsim.jwt.JwtProvider;
import com.dangsim.token.entity.Token;
import com.dangsim.token.repository.TokenRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TokenRepository tokenRepository;

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그아웃 시 사용자에 해당하는 토큰 정보를 삭제한다.")
	void logout() {
		// given
		User user = mock(User.class);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		doNothing().when(tokenRepository).deleteByUser(user);

		// when
		authService.logout(1L, "access");

		// then
		verify(tokenRepository, times(1)).deleteByUser(user);
	}

	@Test
	@DisplayName("refresh 토큰이 유효할 경우 새로운 access, refresh 토큰을 발급한다.")
	void reissue() {
		// given
		User user = mock(User.class);
		Token token = mock(Token.class);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(tokenRepository.findByUser(user)).thenReturn(Optional.of(token));
		when(token.getRefreshToken()).thenReturn("oldToken");
		when(token.getExpiryTime()).thenReturn(LocalDateTime.now().plusMinutes(10));
		when(jwtProvider.createAccessToken(any())).thenReturn("newAccessToken");
		when(jwtProvider.createRefreshToken()).thenReturn("newRefreshToken");

		// when
		Map<String, String> result = authService.reissue(1L, "oldToken");

		// then
		assertThat(result).containsKeys("accessToken", "refreshToken");
	}
}