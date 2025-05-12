package com.dangsim.task.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TaskInfoResponse(
	Long taskId,
	String title,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
	LocalDateTime deadline,
	String reward,
	String imageUrl,
	boolean isCompleted
) {
}
