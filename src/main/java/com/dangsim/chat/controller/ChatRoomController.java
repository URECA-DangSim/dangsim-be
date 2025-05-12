package com.dangsim.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.chat.dto.request.CreateChatRoomRequest;
import com.dangsim.chat.dto.response.ChatRoomDetailResponse;
import com.dangsim.chat.dto.response.ChatRoomInfoResponse;
import com.dangsim.chat.dto.response.ChatRoomResponse;
import com.dangsim.chat.dto.response.ChatRoomSimpleResponse;
import com.dangsim.chat.service.ChatRoomService;
import com.dangsim.common.CursorPageResponse;
import com.dangsim.user.entity.User;

import jakarta.validation.Valid;
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
	) {
		ChatRoomResponse response = chatRoomService.createChatRoom(request, performer);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/chat-rooms")
	public ResponseEntity<CursorPageResponse<ChatRoomSimpleResponse>> getChatRoomsByCursor(
		@RequestParam(name = "cursor", required = false) String cursor,
		@RequestParam(name = "size", defaultValue = "20") int size,
		@AuthenticationPrincipal User user
	) {
		CursorPageResponse<ChatRoomSimpleResponse> response = chatRoomService.getChatRoomsByCursor(cursor, size,
			user.getId());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/chat-rooms/{chatRoomId}")
	public ResponseEntity<CursorPageResponse<ChatRoomDetailResponse>> getChatMessagesByCursor(
		@PathVariable(name = "chatRoomId") Long chatRoomId,
		@RequestParam(name = "cursor", required = false) String cursor,
		@RequestParam(name = "size", defaultValue = "20") int size,
		@AuthenticationPrincipal User user
	) {
		CursorPageResponse<ChatRoomDetailResponse> response = chatRoomService.getChatMessagesByCursor(chatRoomId,
			cursor, size, user.getId());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/chat-rooms/{chatRoomId}/taskInfo")
	public ResponseEntity<ChatRoomInfoResponse> getChatRoomInfo(
		@PathVariable(name = "chatRoomId") Long chatRoomId,
		@AuthenticationPrincipal User user
	) {
		ChatRoomInfoResponse response = chatRoomService.getChatRoomInfo(chatRoomId, user.getId());
		return ResponseEntity.ok(response);
	}

}
