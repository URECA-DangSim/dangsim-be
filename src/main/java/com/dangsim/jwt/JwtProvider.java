package com.dangsim.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.jwt.exception.JwtErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secret;

	private final Clock clock;
	private SecretKey key;

	private static final long ACCESS_EXP = 1000 * 60 * 60 * 1;  // 1시간
	private static final long REFRESH_EXP = 1000 * 60 * 60 * 24; // 1일

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(Long userId) {
		Date now = Date.from(clock.instant());
		Date expiry = new Date(now.getTime() + ACCESS_EXP);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public String createRefreshToken() {
		Date now = Date.from(clock.instant());
		Date expiry = new Date(now.getTime() + REFRESH_EXP);

		return Jwts.builder()
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public Long getUserIdFromToken(String token) {
		if (Objects.isNull(token) && !validateToken(token)) {
			throw new BaseException(JwtErrorCode.TOKEN_IS_EMPTY);
		}

		Claims claims = getClaims(token);
		return Long.valueOf(claims.getSubject());
	}

}
