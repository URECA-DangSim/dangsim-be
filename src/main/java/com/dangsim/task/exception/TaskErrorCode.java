package com.dangsim.task.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskErrorCode implements ErrorCode {

	private final String message;
	private final String code;
}
