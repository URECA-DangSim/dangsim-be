package com.dangsim.common.exception.errorcode;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UtilsErrorCode implements ErrorCode {

	DATE_TIME_IS_NULL("날짜가 비어있습니다.", "DATE_TIME_FORMAT_001");

	private final String message;

	private final String code;
}
