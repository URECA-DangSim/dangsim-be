package com.dangsim.chat.dto.request;

public record SendChatMessageRequest(

        MessageType type,
        Long chatRoomId,
        Long userId,
        String message

) {
    public enum MessageType{
        TALK, //일반 메세지
        READ //읽음 처리 신호
    }

    public static SendChatMessageRequest of (
            MessageType type,
            Long chatRoomId,
            Long userId,
            String message
    ){
        return new SendChatMessageRequest(type,chatRoomId, userId, message);
    }
}
