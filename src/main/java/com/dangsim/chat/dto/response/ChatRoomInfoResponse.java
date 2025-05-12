package com.dangsim.chat.dto.response;

import com.dangsim.task.dto.response.TaskInfoResponse;

public record ChatRoomInfoResponse(
	Long chatRoomId,
	TaskInfoResponse taskInfo,
	Long chatPartnerId,
	String partnerNickname
) {
}
