package com.dangsim.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.dangsim.chat.dto.request.CreateChatMessageRequest;
import com.dangsim.chat.service.ChatMessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageStompController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	public void createChatMessage(
		@DestinationVariable Long chatRoomId,
		@Valid CreateChatMessageRequest request
	) {

		Long userId = request.senderId();
		chatMessageService.createChatMessage(request, userId, chatRoomId);
	}

}
