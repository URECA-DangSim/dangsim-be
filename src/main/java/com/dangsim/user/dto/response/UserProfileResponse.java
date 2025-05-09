package com.dangsim.user.dto.response;

import java.math.BigDecimal;

import com.dangsim.user.entity.User;

public record UserProfileResponse(
	String nickname,
	String address,
	BigDecimal reward,
	String profileImage
) {
	public static UserProfileResponse from(User user) {
		return new UserProfileResponse(
			user.getNickname(),
			user.getAddress().toString(),
			user.getReward(),
			user.getProfileImage()
		);
	}
}
