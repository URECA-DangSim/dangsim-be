package com.dangsim.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	TMP_USER("임시회원"),
	USER("일반회원"),
	ADMIN("관리자");

	private final String role;
}
