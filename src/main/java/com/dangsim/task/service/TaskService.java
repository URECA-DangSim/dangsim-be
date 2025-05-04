package com.dangsim.task.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.task.dto.TaskMapper;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskDetailsResponseDto getTaskById(Long taskId, User user) {
		Task findTask = taskRepository.findById(taskId).orElseThrow(
			() -> new BaseException(TaskErrorCode.NOT_FOUND_TASK)
		);

		User requester = findTask.getUser();
		boolean isMyTask = Objects.equals(requester, user);

		return TaskMapper.toTaskDetailsResponseDto(findTask, isMyTask);
	}
}
