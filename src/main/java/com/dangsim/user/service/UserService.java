package com.dangsim.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.user.dto.UserMapper;
import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.CheckNicknameResponse;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.dto.response.UserTaskResponse;
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
		if (userRepository.existsByNicknameAndIdNot(request.nickname(), user.getId())) {
			throw new BaseException(UserErrorCode.NICKNAME_DUPLICATED);
		}

		User managedUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
		managedUser.updateExtraInfo(request.nickname(), Address.from(request.address()));

		return UserMapper.toExtraInfoResponse(true);
	}

	public CheckNicknameResponse isNicknameDuplicated(String nickname) {
		if (nickname == null || nickname.isBlank()) {
			throw new BaseException(UserErrorCode.NICKNAME_REQUIRED);
		}
		if (nickname.length() < 2 || nickname.length() > 12) {
			throw new BaseException(UserErrorCode.NICKNAME_LENGTH_INVALID);
		}
		boolean isDuplicated = userRepository.findByNickname(nickname).isPresent();
		return UserMapper.toCheckNicknameResponse(isDuplicated);
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getMemberProfile(User user) {
		return UserMapper.toUserProfileResponse(user);
	}

	public CursorPageResponse<UserTaskResponse> getRequestedTasks(String cursor, int size, User user) {
		if (!StringUtils.hasText(cursor)) {
			cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		}
		return userRepository.findRequestedTasksByCursor(cursor, size, user);
	}

	public CursorPageResponse<UserTaskResponse> getPerformedTasks(String cursor, int size, User user) {
		if (!StringUtils.hasText(cursor)) {
			cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		}
		return userRepository.findPerformedTasksByCursor(cursor, size, user);
	}

}
