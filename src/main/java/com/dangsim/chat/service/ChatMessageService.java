package com.dangsim.chat.service;

import com.dangsim.chat.dto.request.ChatMessageRequest;
import com.dangsim.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void createChatMessage(ChatMessageRequest request){
        //서비스 구현
    }

    public void updateMessagesAsRead(Long chatRoomId, Long userId){
        //메시지 읽음 처리 로직 작성 필요
    }

}
