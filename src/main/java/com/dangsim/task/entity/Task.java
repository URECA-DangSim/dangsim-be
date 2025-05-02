package com.dangsim.task.entity;

import static jakarta.persistence.EnumType.*;
import static java.math.BigDecimal.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Check;
import org.springframework.boot.context.properties.bind.DefaultValue;

import com.dangsim.common.entity.BaseEntity;
import com.dangsim.user.entity.Address;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task")
@Check(constraints = "reward >= 100 AND reward <= 1000000")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Task extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Long id;

	@Size(max = 30)
	@NotNull
	@Column(name = "title", length = 30, nullable = false)
	private String title;

	@Size(max = 300)
	@NotNull
	@Column(name = "content", length = 300, nullable = false)
	private String content;

	@NotNull
	@Column(name = "address" , nullable = false)
	private Address address;

	@NotNull
	@Column(name = "deadline", nullable = false)
	private LocalDateTime deadline;

	@NotNull
	@Column(name = "reward", nullable = false)
	private BigDecimal reward;

	@Enumerated(STRING)
	@NotNull
	@Column(name = "status", nullable = false)
	private TaskStatus status;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	private List<TaskImage> images = new ArrayList<>();

	@Builder(access = PRIVATE)
	private Task(String title, String content, Address address, LocalDateTime deadline,
		BigDecimal reward, TaskStatus status) {
		this.title = title;
		this.content = content;
		this.address = address;
		this.deadline = deadline;
		this.reward = reward;
		this.status = status;
	}

	private void updateTaskImage(List<TaskImage> images) {
		images.forEach(image -> {
			this.images.add(image);
			image.addTask(this);
		});
	}
}

