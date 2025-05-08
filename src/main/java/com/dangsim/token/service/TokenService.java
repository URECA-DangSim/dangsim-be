package com.dangsim.token.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.auth.dto.response.AuthTokenResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.jwt.JwtProvider;
import com.dangsim.token.entity.Token;
import com.dangsim.token.exception.TokenErrorCode;
import com.dangsim.token.repository.TokenRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.exception.UserErrorCode;
import com.dangsim.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;

	private static final long REFRESH_TOKEN_EXPIRE_DAYS = 1L;

	@Transactional
	public AuthTokenResponse issueTokensFor(User user) {
		String accessToken = jwtProvider.createAccessToken(user.getId());
		String refreshToken = jwtProvider.createRefreshToken();

		Token token = tokenRepository.findByUser(user)
			.map(t -> {
				t.updateRefreshToken(refreshToken, LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS));
				return t;
			})
			.orElse(Token.builder()
				.refrestToken(refreshToken)
				.expiryTime(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS))
				.user(user)
				.build());

		tokenRepository.save(token);
		return new AuthTokenResponse(accessToken, refreshToken, user.getRole().name());
	}

	@Transactional
	public AuthTokenResponse reissue(Long userId, String oldRefreshToken) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

		Token token = tokenRepository.findByUser(user)
			.orElseThrow(() -> new BaseException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND));

		if (!Objects.equals(token.getRefreshToken(), oldRefreshToken)) {
			throw new BaseException(TokenErrorCode.REFRESH_TOKEN_MISMATCH);
		}

		if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
			tokenRepository.delete(token);
			throw new BaseException(TokenErrorCode.REFRESH_TOKEN_EXPIRED);
		}

		// 새 access/refresh 발급
		String newAccessToken = jwtProvider.createAccessToken(user.getId());
		String newRefreshToken = jwtProvider.createRefreshToken();

		token.updateRefreshToken(newRefreshToken, LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS));
		tokenRepository.save(token);

		return new AuthTokenResponse(newAccessToken, newRefreshToken, user.getRole().name());
	}

	@Transactional
	public void logout(Long userId) {
		//토큰 테이블 삭제
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
		tokenRepository.deleteByUser(user);
	}

}
