package com.dangsim.chat.dto;

import com.dangsim.chat.dto.request.ChatMessageRequest;
import com.dangsim.chat.entity.ChatMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessageMapper {

    public static ChatMessage toChatMessage(
            ChatMessageRequest request,
            Long chatRoomId

    ){
        return ChatMessage.of(
                chatRoomId,
                request.userId(),
                request.message()
        );
    }

}
