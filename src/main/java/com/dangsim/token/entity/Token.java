package com.dangsim.token.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import com.dangsim.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private Long id;

	@Column(nullable = false, length = 500)
	private String refreshToken;

	@Column(nullable = false)
	private LocalDateTime expiryTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	private Token(String refrestToken, LocalDateTime expiryTime, User user) {
		this.refreshToken = refrestToken;
		this.expiryTime = expiryTime;
		this.user = user;
	}

	public void updateRefreshToken(String newToken, LocalDateTime newExpiry) {
		this.refreshToken = newToken;
		this.expiryTime = newExpiry;
	}
}
