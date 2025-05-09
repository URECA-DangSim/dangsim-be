package com.dangsim.chat.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.chat.dto.ChatMessageMapper;
import com.dangsim.chat.dto.request.CreateChatMessageRequest;
import com.dangsim.chat.dto.response.ChatMessageResponse;
import com.dangsim.chat.entity.ChatMessage;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.exception.ChatRoomErrorCode;
import com.dangsim.chat.repository.ChatMessageRepository;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.common.exception.runtime.BaseException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final SimpMessagingTemplate messagingTemplate;

	@Transactional
	public void createChatMessage(@Valid CreateChatMessageRequest request, Long userId, Long chatRoomId) {

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
			() -> new BaseException(ChatRoomErrorCode.NOT_FOUND_CHATROOM)
		);

		if (!chatRoom.getPerformer().getId().equals(userId) && !chatRoom.getRequester().getId().equals(userId)) {
			throw new BaseException(ChatRoomErrorCode.NOT_CHAT_ROOM_OWNER);
		}

		ChatMessage chatMessage = ChatMessage.of(chatRoomId, userId, request.content());
		ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

		ChatMessageResponse response = ChatMessageMapper.toChatMessageResponse(savedChatMessage);

		messagingTemplate.convertAndSend(
			"/sub/chat-rooms/" + chatRoomId,
			response
		);

	}
}
