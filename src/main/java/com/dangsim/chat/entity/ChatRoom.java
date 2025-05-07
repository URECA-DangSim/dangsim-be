package com.dangsim.chat.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import com.dangsim.common.entity.BaseEntity;
import com.dangsim.task.entity.Task;
import com.dangsim.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "task_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Task task;

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "requester_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User requester;

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "performer_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User performer;

	@Builder(access = PRIVATE)
	private ChatRoom(Task task, User requester, User performer){
		this.task = task;
		this.requester = requester;
		this.performer = performer;
	}

	public static ChatRoom of (Task task, User requester, User performer){
		return ChatRoom.builder()
				.task(task)
				.requester(requester)
				.performer(performer)
				.build();
	}


}
