package com.dangsim.auth.entity;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import com.dangsim.common.entity.BaseEntity;
import com.dangsim.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Auth extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_id")
	private Long id;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "provider", nullable = false)
	private AuthProvider provider;

	@NotNull
	@Column(name = "provider_id", nullable = false, unique = true)
	private String providerId;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(
		name = "user_id",
		nullable = false,
		unique = true,
		foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
	)
	private User user;

	@Builder(access = PRIVATE)
	private Auth(AuthProvider provider, String providerId, User user) {
		this.provider = provider;
		this.providerId = providerId;
		this.user = user;
	}
}
