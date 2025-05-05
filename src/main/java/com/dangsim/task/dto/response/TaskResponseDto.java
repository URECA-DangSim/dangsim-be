package com.dangsim.task.dto.response;

public record TaskResponseDto(

	long taskId,
	String merchantUid,
	boolean result

) {
}
