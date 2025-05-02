package com.dangsim.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {

	KAKAO("kakao"),
	GOOGLE("google");

	private final String provider;
}
