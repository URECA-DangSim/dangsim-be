package com.dangsim.chat.exception;

import com.dangsim.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {

	NOT_FOUND_CHATROOM("해당하는 채팅방을 찾을 수 없습니다.", "CHATROOM_001"),
	NOT_CHAT_ROOM_OWNER("해당 채팅방에 속하지 않은 유저입니다.", "CHATROOM_002");

	private final String message;
	private final String code;
}
