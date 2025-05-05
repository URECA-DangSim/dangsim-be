package com.dangsim.task.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.common.util.IdentifierUtils;
import com.dangsim.task.dto.TaskMapper;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDeleteResponse;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskStatus;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;

	@Transactional(readOnly = true)
	public TaskDetailsResponseDto getTaskById(Long taskId, User user) {
		Task findTask = getTaskById(taskId);

		boolean isMyTask = isTaskOwner(findTask, user);

		return TaskMapper.toTaskDetailsResponseDto(findTask, isMyTask);
	}

	@Transactional
	public TaskResponseDto createTask(TaskRequestDto requestDto, User user) {
		validateIsEnoughDeadLine(requestDto.deadline(), LocalDateTime.now());

		Task task = TaskMapper.toTask(requestDto, user);
		Task saveTask = taskRepository.save(task);

		String merchantUid = IdentifierUtils.generateMerchantUid(saveTask.getId(), LocalDateTime.now());

		return TaskMapper.toTaskResponseDto(saveTask, merchantUid);
	}

	private void validateIsEnoughDeadLine(String deadline, LocalDateTime now) {
		LocalDateTime formattedDeadline = DateTimeFormatUtils.parseDateTime(deadline);

		if (formattedDeadline.isBefore(now.plusMinutes(30))) {
			throw new BaseException(TaskErrorCode.NOT_ENOUGH_DEADLINE);
		}
	}

	@Transactional(readOnly = true)
	public CursorPageResponse<TaskSimpleResponseDto> getTasksByCursor(String cursor, int size) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		}

		return taskRepository.findTasksByCursor(cursor, size);
	}

	@Transactional
	public TaskDeleteResponse deleteTaskById(Long taskId, User user) {
		Task findTask = getTaskById(taskId);

		if (!isTaskOwner(findTask, user)) {
			throw new BaseException(TaskErrorCode.NOT_TASK_OWNER);
		}

		validateNotAssigned(findTask);

		taskRepository.deleteById(taskId);

		return new TaskDeleteResponse(true);
	}

	private void validateNotAssigned(Task task) {
		if (TaskStatus.isMatching(task.getStatus())) {
			throw new BaseException(TaskErrorCode.IS_MATCHING);
		}
	}

	private boolean isTaskOwner(Task task, User user) {
		return Objects.equals(task.getUser(), user);
	}

	private Task getTaskById(Long taskId) {
		return taskRepository.findById(taskId).orElseThrow(
			() -> new BaseException(TaskErrorCode.NOT_FOUND_TASK)
		);
	}
}
