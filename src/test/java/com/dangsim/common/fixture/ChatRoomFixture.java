package com.dangsim.common.fixture;

import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.task.entity.Task;
import com.dangsim.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ChatRoomFixture {

	public static ChatRoom chatRoom(Task task, User requester, User performer) {
		return ChatRoom.of(
			task,
			requester,
			performer
		);
	}
}
