package com.dangsim.chat.dto.response;

public record ChatMessageResponse(
	Long messageId,
	String content,
	Long senderId,
	String timestamp
) {
}
