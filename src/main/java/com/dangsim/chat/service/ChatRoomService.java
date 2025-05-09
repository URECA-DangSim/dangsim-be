package com.dangsim.chat.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.chat.dto.ChatRoomMapper;
import com.dangsim.chat.dto.request.CreateChatRoomRequest;
import com.dangsim.chat.dto.response.ChatRoomResponse;
import com.dangsim.chat.dto.response.ChatRoomSimpleResponse;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.task.entity.Task;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.exception.UserErrorCode;
import com.dangsim.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public ChatRoomResponse createChatRoom(@Valid CreateChatRoomRequest request, User performer) {
		Task task = taskRepository.findById(request.taskID())
			.orElseThrow(() -> new BaseException(TaskErrorCode.NOT_FOUND_TASK));
		User requester = userRepository.findById(task.getUser().getId())
			.orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
		ChatRoom chatRoom = ChatRoom.of(task, requester, performer);
		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

		return ChatRoomMapper.toChatRoomResponse(savedChatRoom);
	}

	@Transactional(readOnly = true)
	public CursorPageResponse<ChatRoomSimpleResponse> getChatRoomsByCursor(String cursor, int size, Long userId) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		}

		return chatRoomRepository.findChatRoomsByCursor(cursor, size, userId);
	}
}
