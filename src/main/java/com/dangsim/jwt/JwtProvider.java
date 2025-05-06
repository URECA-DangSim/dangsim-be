package com.dangsim.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

	private SecretKey key;

	private static final long ACCESS_EXP = 1000 * 60 * 60 * 1;  // 1시간
	private static final long REFRESH_EXP = 1000 * 60 * 60 * 24; // 1일

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(Long userId) {
		return Jwts.builder()
			.subject(String.valueOf(userId))
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
			.signWith(key, Jwts.SIG.HS256)
			.compact();
	}

	public String createRefreshToken() {
		return Jwts.builder()
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
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
}
