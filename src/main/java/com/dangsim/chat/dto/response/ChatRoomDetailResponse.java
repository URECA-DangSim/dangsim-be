package com.dangsim.chat.dto.response;

import java.util.List;

public record ChatRoomDetailResponse(
	Long chatRoomId,
	List<ChatMessageDetailResponse> messages
) {
}
