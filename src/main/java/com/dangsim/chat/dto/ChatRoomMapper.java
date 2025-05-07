package com.dangsim.chat.dto;

import com.dangsim.chat.dto.response.ChatRoomResponse;
import com.dangsim.chat.entity.ChatRoom;

public class ChatRoomMapper {

    public static ChatRoomResponse toChatRoomResponse(
            ChatRoom chatRoom
    ){
        return new ChatRoomResponse(chatRoom.getId());
    }

}
