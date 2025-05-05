package com.dangsim.task.dto;

import static com.dangsim.task.entity.TaskStatus.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class TaskMapper {

	private static final DateTimeFormatter DEADLINE_FORMATTER =
		DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");

	public static TaskDetailsResponseDto toTaskDetailsResponseDto(Task task, boolean isMyTask) {
		return new TaskDetailsResponseDto(
			task.getId(),
			task.getTitle(),
			task.getContent(),
			task.getAddress().toString(),
			task.getDeadline(),
			task.getReward().toPlainString(),
			task.getStatus().getStatus(),
			isMyTask,
			extractImageUrls(task.getImages())
		);
	}

	public static TaskResponseDto toTaskResponseDto(Task task, String merchantUid) {
		return new TaskResponseDto(
			task.getId(),
			merchantUid,
			true
		);
	}

	public static Task toTask(TaskRequestDto requestDto, User user) {
		return Task.of(
			requestDto.title(),
			requestDto.content(),
			Address.from(requestDto.address()),
			LocalDateTime.parse(requestDto.deadline(), DEADLINE_FORMATTER),
			BigDecimal.valueOf(requestDto.reward()),
			TASK_NOT_ASSIGNED,
			user,
			toTaskImage(requestDto.imageUrls())
		);
	}

	private static List<String> extractImageUrls(List<TaskImage> images) {
		if (Objects.isNull(images) || images.isEmpty()) {
			return Collections.emptyList();
		}

		return images.stream()
			.map(TaskImage::getImageUrl)
			.toList();
	}

	private static List<TaskImage> toTaskImage(List<String> imageUrls) {
		List<TaskImage> images = new ArrayList<>();

		if (Objects.isNull(imageUrls)) {
			return images;
		}

		return images = imageUrls.stream()
			.map(TaskImage::from)
			.toList();
	}
}
