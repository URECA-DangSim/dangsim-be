package com.dangsim.user.dto.response;

import java.math.BigDecimal;

public record UserProfileResponse(
	String nickname,
	String address,
	BigDecimal reward,
	String profileImage
) {
}
