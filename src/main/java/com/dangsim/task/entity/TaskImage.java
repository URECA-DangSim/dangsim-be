package com.dangsim.task.entity;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import com.dangsim.common.entity.BaseEntity;

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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_image")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TaskImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_image_id")
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column(name = "image_url", length = 255, nullable = false)
	private String imageUrl;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "question_post_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Task task;

	private TaskImage(String imageUrl, Task task) {
		this.imageUrl = imageUrl;
		this.task = task;
	}

	public void addTask(Task task) {
		this.task = task;
	}
}
