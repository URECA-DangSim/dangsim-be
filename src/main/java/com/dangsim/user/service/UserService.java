package com.dangsim.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.User;
import com.dangsim.user.exception.UserErrorCode;
import com.dangsim.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public ExtraInfoResponse updateUserExtraInfo(User user, ExtraInfoRequest request) {

		// 닉네임 중복 검사
		if (userRepository.findByNickname(request.nickname())
			.filter(found -> !found.getId().equals(user.getId()))
			.isPresent()) {
			throw new BaseException(UserErrorCode.NICKNAME_DUPLICATED);
		}

		User managedUser = userRepository.getReferenceById(user.getId());
		managedUser.updateExtraInfo(request.nickname(), Address.from(request.address()));

		user.updateExtraInfo(request.nickname(), Address.from(request.address()));
		return ExtraInfoResponse.of(true);
	}

	public boolean isNicknameDuplicated(String nickname) {
		if (nickname == null || nickname.isBlank()) {
			throw new BaseException(UserErrorCode.NICKNAME_REQUIRED);
		}
		if (nickname.length() < 2 || nickname.length() > 12) {
			throw new BaseException(UserErrorCode.NICKNAME_LENGTH_INVALID);
		}
		return userRepository.findByNickname(nickname).isPresent();
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getMemberProfile(User user) {
		return UserProfileResponse.from(user);
	}

}
