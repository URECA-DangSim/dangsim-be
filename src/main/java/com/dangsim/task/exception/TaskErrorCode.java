package com.dangsim.task.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskErrorCode implements ErrorCode {

	NOT_FOUND_TASK("해당하는 심부름을 찾을 수 없습니다.", "TASK_001"),
	NOT_ENOUGH_DEADLINE("충분한 마감기한이 아닙니다.", "TASK_002"),
	NOT_TASK_OWNER("해당 심부름 요청의 주인이 아닙니다.", "TASK_003"),
	IS_MATCHING("해당 심부름 요청은 매칭된 요청입니다.", "TASK_004"),
	NOT_MATCH_YOURSELF("자신의 심부름 요청에 매칭할 수 없습니다.", "TASK_005");

	private final String message;

	private final String code;

}
