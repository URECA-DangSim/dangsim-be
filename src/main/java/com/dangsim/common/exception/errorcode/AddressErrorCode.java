package com.dangsim.common.exception.errorcode;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressErrorCode implements ErrorCode {

	ADDRESS_EMPTY("주소값이 비어있습니다.", "ADDRESS_001"),
	INVALID_ADDRESS_FORMAT("주소 형식이 올바르지 않습니다.", "ADDRESS_002");

	private final String message;

	private final String code;
}
