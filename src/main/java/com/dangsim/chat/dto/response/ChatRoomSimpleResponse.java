package com.dangsim.chat.dto.response;

import java.util.Objects;

import com.dangsim.chat.entity.ChatMessage;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomSimpleResponse(
	Long chatRoomId,
	Long chatPartnerId,
	String nickname,
	String content,
	String timestamp,
	Boolean isRead,
	Long chatMessageId

) {
	@QueryProjection
	public ChatRoomSimpleResponse(ChatRoom chatRoom, ChatMessage lastChatMessage, User partner) {
		this(
			chatRoom.getId(),
			partner.getId(),
			partner.getNickname(),
			initChatMessage(lastChatMessage),
			initTimeStamp(lastChatMessage),
			initIsRead(lastChatMessage),
			lastChatMessage.getId()
		);
	}

	private static final String DEFAULT = "채팅을 시작해보세요";

	public static String initChatMessage(ChatMessage lastChatMessage) {
		if (Objects.isNull(lastChatMessage)) {
			return DEFAULT;
		}
		return lastChatMessage.getMessage();
	}

	public static String initTimeStamp(ChatMessage lastChatMessage) {
		if (Objects.isNull(lastChatMessage)) {
			return "";
		}
		return DateTimeFormatUtils.formatDateTime(lastChatMessage.getCreatedAt());
	}

	public static Boolean initIsRead(ChatMessage lastChatMessage) {
		if (Objects.isNull(lastChatMessage)) {
			return false;
		}
		return lastChatMessage.isRead();
	}

}

