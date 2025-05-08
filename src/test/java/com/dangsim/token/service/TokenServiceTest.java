package com.dangsim.token.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dangsim.auth.dto.response.AuthTokenResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.jwt.JwtProvider;
import com.dangsim.token.entity.Token;
import com.dangsim.token.exception.TokenErrorCode;
import com.dangsim.token.repository.TokenRepository;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TokenRepository tokenRepository;

	@InjectMocks
	private TokenService tokenService;

	@Test
	@DisplayName("로그아웃 시 사용자에 해당하는 토큰 정보를 삭제한다.")
	void logout() {
		// given
		User user = mock(User.class);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		doNothing().when(tokenRepository).deleteByUser(user);

		// when
		tokenService.logout(1L);

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
		when(user.getRole()).thenReturn(Role.TMP_USER);

		// when
		AuthTokenResponse result = tokenService.reissue(1L, "oldToken");

		// then
		assertThat(result.accessToken()).isEqualTo("newAccessToken");
		assertThat(result.refreshToken()).isEqualTo("newRefreshToken");
	}

	@Test
	@DisplayName("refresh 토큰이 저장되어 있지 않으면 예외가 발생한다.")
	void reissue_whenTokenNotFound_thenThrows() {
		//given
		User user = mock(User.class);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(tokenRepository.findByUser(user)).thenReturn(Optional.empty());

		//when&then
		assertThatThrownBy(() -> tokenService.reissue(1L, "refreshToken"))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("refresh 토큰이 저장된 값과 다르면 예외가 발생한다.")
	void reissue_whenTokenMismatch_thenThrows() {
		// given
		User user = mock(User.class);
		Token token = mock(Token.class);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(tokenRepository.findByUser(user)).thenReturn(Optional.of(token));
		when(token.getRefreshToken()).thenReturn("storedToken");

		// when & then
		assertThatThrownBy(() -> tokenService.reissue(1L, "wrongToken"))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining(TokenErrorCode.REFRESH_TOKEN_MISMATCH.getMessage());
	}

	@Test
	@DisplayName("refresh 토큰이 만료된 경우 예외가 발생한다.")
	void reissue_whenTokenExpired_thenThrows() {
		// given
		User user = mock(User.class);
		Token token = mock(Token.class);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(tokenRepository.findByUser(user)).thenReturn(Optional.of(token));
		when(token.getRefreshToken()).thenReturn("validToken");
		when(token.getExpiryTime()).thenReturn(LocalDateTime.now().minusMinutes(1));

		// when & then
		assertThatThrownBy(() -> tokenService.reissue(1L, "validToken"))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining(TokenErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
	}
}