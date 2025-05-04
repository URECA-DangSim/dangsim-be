package com.dangsim.task.dto;

import static lombok.AccessLevel.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class TaskMapper {

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

	private static List<String> extractImageUrls(List<TaskImage> images) {
		if (Objects.isNull(images) || images.isEmpty()) {
			return Collections.emptyList();
		}

		return images.stream()
			.map(TaskImage::getImageUrl)
			.toList();
	}
}
