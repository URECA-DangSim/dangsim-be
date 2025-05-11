package com.dangsim.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.dangsim.user.dto.request.ExtraInfoRequest;
import com.dangsim.user.dto.response.ExtraInfoResponse;
import com.dangsim.user.dto.response.UserProfileResponse;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;
import com.dangsim.user.exception.UserErrorCode;
import com.dangsim.user.repository.UserRepository;
import com.dangsim.common.exception.runtime.BaseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("닉네임이 중복되지 않았을 경우 false를 반환한다")
	void testIsNicknameDuplicated_False() {
		String nickname = "tester";
		when(userRepository.findByNickname(nickname)).thenReturn(Optional.empty());

		boolean result = userService.isNicknameDuplicated(nickname);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("닉네임이 중복되었을 경우 true를 반환한다")
	void testIsNicknameDuplicated_True() {
		String nickname = "tester";
		User existingUser = mock(User.class);
		when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(existingUser));

		boolean result = userService.isNicknameDuplicated(nickname);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자 정보 업데이트에 성공한다")
	void testUpdateUserExtraInfo_Success() {
		User user = User.of("image-url");
		ExtraInfoRequest request = new ExtraInfoRequest("newNick", "서울 강남구 역삼동");

		when(userRepository.findByNickname("newNick")).thenReturn(Optional.empty());
		when(userRepository.findById(any())).thenReturn(Optional.of(user));

		ExtraInfoResponse response = userService.updateUserExtraInfo(user, request);

		assertThat(response.result()).isTrue();
		assertThat(user.getNickname()).isEqualTo("newNick");
		assertThat(user.getAddress().toString()).isEqualTo("서울 강남구 역삼동");
		assertThat(user.getRole()).isEqualTo(Role.USER);
	}

	@Test
	@DisplayName("중복된 닉네임일 경우 예외를 발생시킨다")
	void testUpdateUserExtraInfo_DuplicatedNickname() {
		ExtraInfoRequest request = new ExtraInfoRequest("existingNickname", "서울특별시 강남구 역삼동");

		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);

		User duplicatedUser = mock(User.class);
		when(duplicatedUser.getId()).thenReturn(2L);
		when(userRepository.findByNickname("existingNickname")).thenReturn(Optional.of(duplicatedUser));

		assertThrows(BaseException.class, () -> userService.updateUserExtraInfo(user, request));
		verify(userRepository).findByNickname("existingNickname");
	}

	@Test
	@DisplayName("유저 프로필 정보를 성공적으로 조회한다")
	void testGetMemberProfile() {
		User user = User.of("profile-image-url");
		user.updateExtraInfo("nickname", Address.from("서울 마포구 서교동"));

		UserProfileResponse response = userService.getMemberProfile(user);

		assertThat(response.nickname()).isEqualTo("nickname");
		assertThat(response.address()).isEqualTo("서울 마포구 서교동");
		assertThat(response.profileImage()).isEqualTo("profile-image-url");
	}
}
