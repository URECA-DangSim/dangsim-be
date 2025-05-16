package com.dangsim.chat.dto.request;

import com.dangsim.chat.dto.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateChatMessageRequest(
	@NotNull
	MessageType type,
	@Size(max = 300, message = "최대 300자까지 입력이 가능합니다.")
	@NotBlank
	String content,
	@NotNull
	Long senderId
) {
}
