package com.dangsim.auth.service;

import org.springframework.stereotype.Component;

import com.dangsim.auth.dto.response.KakaoUserResponse;
import com.dangsim.auth.entity.Auth;
import com.dangsim.auth.entity.AuthProvider;
import com.dangsim.auth.repository.AuthRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthProcessor {

	private final AuthRepository authRepository;
	private final UserRepository userRepository;

	public User process(AuthProvider provider, KakaoUserResponse userInfo) {
		String oauthId = provider.getProvider() + "_" + userInfo.id();

		return authRepository.findByProviderAndProviderId(provider, oauthId)
			.map(auth -> {
				auth.getUser().updateProfileImage(userInfo.profileImage());
				return auth.getUser();
			})
			.orElseGet(() -> {
				User newUser = userRepository.save(User.of(userInfo.profileImage()));
				authRepository.save(Auth.of(provider, oauthId, newUser));
				return newUser;
			});

	}

	private User createAuthAndUser(KakaoUserResponse user, AuthProvider provider, String oauthId) {
		String profileImage = user.profileImage();
		User newUser = userRepository.save(User.of(profileImage));
		authRepository.save(Auth.of(provider, oauthId, newUser));
		return newUser;
	}
}
