package com.dangsim.user.entity;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;

import com.dangsim.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Size(max = 12)
	@Column(name = "nickname", length = 12)
	private String nickname;

	@Size(max = 255)
	@Column(name = "profile_image", length = 255)
	private String profileImage;

	@Embedded
	@Column(name = "address")
	private Address address;

	@Enumerated(STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "reward", nullable = false)
	private BigDecimal reward;

	@Builder(access = PRIVATE)
	private User(String nickname, String profileImage, Address address, Role role, BigDecimal reward) {
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.address = address;
		this.role = role;
		this.reward = reward;
	}

	public static User of(Role role, BigDecimal reward) {
		return User.builder()
			.role(role)
			.reward(reward)
			.build();
	}

	public static User of(String profileImage) {
		return User.builder()
			.nickname(null)  // 아직 미정
			.profileImage(profileImage)
			.address(null)   // 아직 미정
			.role(Role.TMP_USER)
			.reward(BigDecimal.ZERO)
			.build();
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updateExtraInfo(String nickname, Address address) {
		this.nickname = nickname;
		this.address = address;
		this.role = Role.USER;
	}

	public void updateReward(BigDecimal reward) {
		this.reward = reward;
	}
}