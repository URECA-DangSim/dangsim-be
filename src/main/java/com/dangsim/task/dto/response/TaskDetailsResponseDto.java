package com.dangsim.task.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TaskDetailsResponseDto(

	Long taskId,
	String title,
	String content,
	String address,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd HH:mm")
	LocalDateTime deadline,
	String reward,
	String status,
	boolean isMyTask,
	List<String> imageUrls
) {
}
