package com.dangsim.task.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.util.IdentifierUtils;
import com.dangsim.task.dto.TaskMapper;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.entity.Task;
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
		Task findTask = taskRepository.findById(taskId).orElseThrow(
			() -> new BaseException(TaskErrorCode.NOT_FOUND_TASK)
		);

		User requester = findTask.getUser();
		boolean isMyTask = Objects.equals(requester, user);

		return TaskMapper.toTaskDetailsResponseDto(findTask, isMyTask);
	}

	@Transactional
	public TaskResponseDto createTask(TaskRequestDto requestDto, User user) {
		Task task = TaskMapper.toTask(requestDto, user);
		Task saveTask = taskRepository.save(task);

		String merchantUid = IdentifierUtils.generateMerchantUid(saveTask.getId(), LocalDateTime.now());

		return TaskMapper.toTaskResponseDto(saveTask, merchantUid);
	}
}
