package com.dangsim.chat.entity;

import static lombok.AccessLevel.*;

import com.dangsim.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_message_id")
	private Long id;

	@Size(max = 600)
	@NotNull
	@Column(name = "message", length = 600, nullable = false)
	private String message;

	@NotNull
	@Column(name = "is_read", nullable = false)
	private boolean isRead;

	@NotNull
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@NotNull
	@Column(name = "chat_room_id", nullable = false)
	private Long chatRoomId;
}
