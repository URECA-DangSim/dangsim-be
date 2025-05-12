package com.dangsim.chat.dto.response;

import com.dangsim.chat.entity.ChatMessage;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.querydsl.core.annotations.QueryProjection;

public record ChatMessageDetailResponse(
	Long messageId,
	Long senderId,
	String content,
	String timeStamp
) {
	@QueryProjection
	public ChatMessageDetailResponse(ChatMessage chatMessage) {
		this(
			chatMessage.getId(),
			chatMessage.getUserId(),
			chatMessage.getMessage(),
			DateTimeFormatUtils.formatDateTime(chatMessage.getCreatedAt())
		);
	}
}
