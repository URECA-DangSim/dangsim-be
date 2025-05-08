package com.dangsim.chat.dto.request;

import com.dangsim.chat.dto.MessageType;

import jakarta.validation.constraints.NotNull;

public record CreateChatMessageRequest(
	@NotNull
	MessageType type,
	String content,
	@NotNull
	Long chatRoomId
) {
}
