package com.dangsim.file.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileException implements ErrorCode {

	EMPTY_FILE("파일이 비어있습니다.", "FILE_001"),
	NO_FILE_EXTENSION("파일 확장자가 존재하지 않습니다.", "FILE_002"),
	INVALID_FILE_EXTENSION("지원되지 않는 파일 확장자 입니다.", "FILE_003"),
	FAIL_FILE_UPLOAD("파일 업로드에 실패했습니다.", "FILE_004");

	private final String message;
	private final String code;
}