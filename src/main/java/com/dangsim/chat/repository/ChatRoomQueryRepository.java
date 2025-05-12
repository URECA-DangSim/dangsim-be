package com.dangsim.chat.repository;

import com.dangsim.chat.dto.response.ChatRoomDetailResponse;
import com.dangsim.chat.dto.response.ChatRoomSimpleResponse;
import com.dangsim.common.CursorPageResponse;

public interface ChatRoomQueryRepository {
	public CursorPageResponse<ChatRoomSimpleResponse> findChatRoomsByCursor(String cursor, int size, Long userId);

	public CursorPageResponse<ChatRoomDetailResponse> findChatMessagesByCursor(Long chatRoomId, String cursor, int size,
		Long userId);
}
