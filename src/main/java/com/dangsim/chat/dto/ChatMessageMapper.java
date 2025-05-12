package com.dangsim.chat.dto;

import com.dangsim.chat.dto.response.ChatMessageResponse;
import com.dangsim.chat.entity.ChatMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageMapper {

	public static ChatMessageResponse toChatMessageResponse(
		ChatMessage chatMessage
	) {

		return new ChatMessageResponse(
			chatMessage.getId(),
			chatMessage.getMessage(),
			chatMessage.getUserId(),
			chatMessage.getCreatedAt().toString()
		);
	}

}
