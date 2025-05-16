package com.dangsim.chat.dto;

import static com.dangsim.task.entity.TaskStatus.*;

import java.util.List;
import java.util.Objects;

import com.dangsim.chat.dto.response.ChatRoomInfoResponse;
import com.dangsim.chat.dto.response.ChatRoomResponse;
import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.task.dto.response.TaskInfoResponse;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;
import com.dangsim.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomMapper {

	public static ChatRoomResponse toChatRoomResponse(
		ChatRoom chatRoom
	) {
		return new ChatRoomResponse(chatRoom.getId());
	}

	public static TaskInfoResponse toTaskInfoResponse(Task task) {
		return new TaskInfoResponse(
			task.getId(),
			task.getTitle(),
			task.getDeadline(),
			task.getReward().toPlainString(),
			getThumbNail(task.getImages()),//썸네일 이미지
			isCompletedTask(task)
		);
	}

	public static ChatRoomInfoResponse toChatRoomInfoResponse(Long chatRoomId, User chatPartner,
		TaskInfoResponse taskInfoResponse, Long userId) {
		return new ChatRoomInfoResponse(
			chatRoomId,
			taskInfoResponse,
			chatPartner.getId(),
			chatPartner.getNickname(),
			userId
		);
	}

	public static String getThumbNail(List<TaskImage> imageUrls) {
		if (Objects.isNull(imageUrls) || imageUrls.isEmpty()) {
			return "";
		}

		return imageUrls.get(0).getImageUrl();
	}

	public static boolean isCompletedTask(Task task) {
		if (Objects.equals(task.getStatus(), TASK_COMPLETE)) {
			return true;
		}
		return false;
	}

}
