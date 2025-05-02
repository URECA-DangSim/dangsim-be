package com.dangsim.chat.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageErrorCode implements ErrorCode {

	private final String message;
	private final String code;
}
