package com.dangsim.chat.dto.response;

import com.dangsim.chat.entity.ChatMessage;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomSimpleResponse(
	Long chatRoomId,
	Long chatPartnerId,
	String nickname,
	String content,
	String timestamp,
	Boolean isRead

) {
	@QueryProjection
	public ChatRoomSimpleResponse(ChatRoom chatRoom, ChatMessage lastChatMessage, User partner) {
		this(
			chatRoom.getId(),
			partner.getId(),
			partner.getNickname(),
			lastChatMessage.getMessage(),
			lastChatMessage.getCreatedAt().toString(),
			lastChatMessage.isRead()
		);

	}

}
