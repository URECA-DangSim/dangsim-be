package com.dangsim.chat.controller;

import com.dangsim.chat.dto.request.CreateChatRoomRequest;
import com.dangsim.chat.dto.response.ChatRoomResponse;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.service.ChatRoomService;
import com.dangsim.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/api/chat-rooms/chat-room")
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @Valid @RequestBody
            CreateChatRoomRequest request,
            @AuthenticationPrincipal User performer
    ){
        //chatRoomService.createChatRoom(request, performer);
        return null;
    }


}
