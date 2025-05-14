package com.dangsim.task.entity;

import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {

	TASK_NOT_ASSIGNED("미배정"),
	TASK_IN_PROGRESS("진행"),
	TASK_COMPLETE("진행완료"),
	TASK_DELETE("삭제");

	private final String status;

	public static boolean isMatching(TaskStatus status) {
		if (Objects.equals(TaskStatus.TASK_NOT_ASSIGNED, status)) {
			return false;
		}

		return true;
	}
}
