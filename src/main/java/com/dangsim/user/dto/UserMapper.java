package com.dangsim.user.dto;

import com.dangsim.user.dto.response.CheckNicknameResponse;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.entity.User;

public class UserMapper {
	public static CheckNicknameResponse toCheckNicknameResponse(boolean isDuplicated) {
		return new CheckNicknameResponse(isDuplicated);
	}

	public static UserProfileResponse toUserProfileResponse(User user) {
		return new UserProfileResponse(
			user.getNickname(),
			user.getAddress().toString(),
			user.getReward(),
			user.getProfileImage()
		);
	}

	public static ExtraInfoResponse toExtraInfoResponse(boolean result) {
		return new ExtraInfoResponse(result);
	}
}

