package com.dangsim.task.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {

	TASK_NOT_ASSIGNED("미배정"),
	TASK_IN_PROGRESS("진행"),
	TASK_COMPLETE("진행완료");

	private final String status;
}
