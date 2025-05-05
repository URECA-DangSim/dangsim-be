package com.dangsim.task.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.fixture.TaskFixture;
import com.dangsim.common.fixture.UserFixture;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@Mock
	TaskRepository taskRepository;

	@InjectMocks
	TaskService taskService;

	@DisplayName("일치하는 심부름 요청이 없으면 예외가 발생한다.")
	@Test
	void throwNotFoundExceptionWhenNotExistTaskByTaskId() {
		// given
		Long taskId = 1L;
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);

		// when
		given(taskRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// then
		assertThatThrownBy(() -> taskService.getTaskById(taskId, user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.NOT_FOUND_TASK.getMessage());
	}

	@DisplayName("해당하는 심부름 요청 상세 정보를 가져온다.")
	@Test
	void getTaskDetailsByTaskId() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), requester);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent())
		);
	}

	@DisplayName("심부름 요청 정보를 가져올 때 자신의 요청인지 검증한다.")
	@Test
	void getTaskDetailsByTaskIdWithOwner() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), requester);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent()),
			() -> assertThat(responseDto.isMyTask()).isTrue()
		);
	}

	@DisplayName("심부름 요청 정보를 가져올 때 자신의 요청인지 검증한다.")
	@Test
	void getTaskDetailsByTaskIdWithOther() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);
		ReflectionTestUtils.setField(other, "id", 2L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), other);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent()),
			() -> assertThat(responseDto.isMyTask()).isFalse()
		);
	}

	@DisplayName("심부름을 저장한다.")
	@Test
	void createTask() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다";
		final String deadline = "25.01.01 15:00";
		final int reward = 1000;
		final String address = "서울특별시 강남구 대치2동";
		List<String> imageUrls = Collections.EMPTY_LIST;

		TaskRequestDto requestDto = new TaskRequestDto(title, content, deadline, reward, address, imageUrls);

		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(title, content, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.save(any(Task.class))).willReturn(task);

		// when
		TaskResponseDto responseDto = taskService.createTask(requestDto, user);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertTrue(responseDto.merchantUid().startsWith(task.getId().toString())),
			() -> assertThat(responseDto.result()).isTrue()
		);
	}

}
