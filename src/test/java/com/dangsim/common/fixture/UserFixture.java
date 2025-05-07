package com.dangsim.common.fixture;

import java.math.BigDecimal;

import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFixture {

	public static User user(Role role, BigDecimal bigDecimal) {
		return User.of(role, bigDecimal);
	}
}
