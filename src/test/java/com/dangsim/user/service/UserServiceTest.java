package com.dangsim.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.fixture.UserFixture;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.dto.response.UserTaskResponse;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private static final String NICKNAME = "tester";
	private static final String NEW_NICKNAME = "newNick";
	private static final String EXISTING_NICKNAME = "existingNickname";
	private static final String ADDRESS = "서울특별시 강남구 역삼동";
	private static final String PROFILE_IMAGE = "profile-image-url";

	@Test
	@DisplayName("닉네임이 중복되지 않았을 경우 false를 반환한다")
	void testIsNicknameDuplicated_False() {
		given(userRepository.findByNickname(NICKNAME)).willReturn(Optional.empty());
		boolean result = userService.isNicknameDuplicated(NICKNAME).isDuplicated();
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("닉네임이 중복되었을 경우 true를 반환한다")
	void testIsNicknameDuplicated_True() {
		User existingUser = mock(User.class);
		given(userRepository.findByNickname(NICKNAME)).willReturn(Optional.of(existingUser));
		boolean result = userService.isNicknameDuplicated(NICKNAME).isDuplicated();
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자 정보 업데이트에 성공한다")
	void testUpdateUserExtraInfo_Success() {
		User user = User.of(PROFILE_IMAGE);
		ReflectionTestUtils.setField(user, "id", 1L); // ID 직접 설정

		ExtraInfoRequest request = new ExtraInfoRequest(NEW_NICKNAME, ADDRESS);

		given(userRepository.findById(1L)).willReturn(Optional.of(user));

		ExtraInfoResponse response = userService.updateUserExtraInfo(user, request);

		assertThat(response.result()).isTrue();
		assertThat(user.getNickname()).isEqualTo(NEW_NICKNAME);
		assertThat(user.getAddress().toString()).isEqualTo(ADDRESS);
		assertThat(user.getRole()).isEqualTo(Role.USER);
	}

	@Test
	@DisplayName("중복된 닉네임일 경우 예외를 발생시킨다")
	void testUpdateUserExtraInfo_DuplicatedNickname() {
		final String EXISTING_NICKNAME = "existingNickname";
		final String ADDRESS = "서울특별시 강남구 역삼동";

		ExtraInfoRequest request = new ExtraInfoRequest(EXISTING_NICKNAME, ADDRESS);
		User user = mock(User.class);
		given(user.getId()).willReturn(1L);

		given(userRepository.existsByNicknameAndIdNot(EXISTING_NICKNAME, 1L)).willReturn(true);

		assertThrows(BaseException.class, () -> userService.updateUserExtraInfo(user, request));

		verify(userRepository).existsByNicknameAndIdNot(EXISTING_NICKNAME, 1L);
	}

	@Test
	@DisplayName("유저 프로필 정보를 성공적으로 조회한다")
	void testGetMemberProfile() {
		User user = User.of(PROFILE_IMAGE);
		user.updateExtraInfo(NICKNAME, Address.from(ADDRESS));

		UserProfileResponse response = userService.getMemberProfile(user);

		assertThat(response.nickname()).isEqualTo(NICKNAME);
		assertThat(response.address()).isEqualTo(ADDRESS);
		assertThat(response.profileImage()).isEqualTo(PROFILE_IMAGE);
	}

	@Disabled
	@DisplayName("유저의 심부름 요청 내역을 조회한다. - 커서 없음")
	@Test
	void getRequestedTasksWithNoCursor() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		CursorPageResponse<UserTaskResponse> mockResponse = new CursorPageResponse<>(null, null, false);

		given(userRepository.findRequestedTasksByCursor(anyString(), anyInt(), any())).willReturn(mockResponse);

		// when
		CursorPageResponse<UserTaskResponse> response = userService.getRequestedTasks(null, 5, user);

		// then
		assertThat(response).isEqualTo(mockResponse);
	}

	@DisplayName("유저의 심부름 수행 내역을 조회한다. - 커서 제공됨")
	@Test
	void getPerformedTasksWithCursor() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		String cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		CursorPageResponse<UserTaskResponse> mockResponse = new CursorPageResponse<>(null, null, false);

		given(userRepository.findPerformedTasksByCursor(cursor, 10, user)).willReturn(mockResponse);

		// when
		CursorPageResponse<UserTaskResponse> response = userService.getPerformedTasks(cursor, 10, user);

		// then
		assertThat(response).isEqualTo(mockResponse);
	}

}
