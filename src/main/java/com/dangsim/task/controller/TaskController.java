package com.dangsim.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDeleteResponse;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.task.service.TaskService;
import com.dangsim.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@GetMapping("/api/tasks/{taskId}")
	public ResponseEntity<TaskDetailsResponseDto> getTaskById(
		@PathVariable(name = "taskId") Long taskId,
		@AuthenticationPrincipal User user
	) {
		TaskDetailsResponseDto response = taskService.getTaskById(taskId, user);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/api/tasks/task")
	public ResponseEntity<TaskResponseDto> createTask(
		@Valid @RequestBody TaskRequestDto requestDto,
		@AuthenticationPrincipal User user
	) {
		TaskResponseDto response = taskService.createTask(requestDto, user);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/tasks")
	public ResponseEntity<CursorPageResponse<TaskSimpleResponseDto>> getTasksByCursor(
		@RequestParam(name = "cursor", required = false) String cursor,
		@RequestParam(name = "size", defaultValue = "20") int size
	) {
		CursorPageResponse<TaskSimpleResponseDto> response = taskService.getTasksByCursor(cursor, size);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/api/tasks/{taskId}")
	public ResponseEntity<TaskDeleteResponse> deleteTaskById(
		@PathVariable(name = "taskId") Long taskId,
		@AuthenticationPrincipal User user
	) {
		TaskDeleteResponse response = taskService.deleteTaskById(taskId, user);
		return ResponseEntity.ok(response);
	}
}
