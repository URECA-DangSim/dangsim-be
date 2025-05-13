package com.dangsim.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

public record UserTaskResponse (
	Long taskId,
	String title,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
	LocalDateTime deadline,
	String reward,
	String imageUrl,
	String createdAt
){
	@QueryProjection
	public UserTaskResponse(Task task){
		this(
			task.getId(),
			task.getTitle(),
			task.getDeadline(),
			task.getReward().toPlainString(),
			setImageUrl(task.getImages()),
			DateTimeFormatUtils.formatDateTime(task.getCreatedAt())
		);
	}

	private static String setImageUrl(List<TaskImage> images) {
		if (Objects.isNull(images) || images.size() == 0) {
			return ""; // 기본 로고
		}

		return images.get(0).getImageUrl();
	}
}
