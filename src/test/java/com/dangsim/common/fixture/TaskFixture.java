package com.dangsim.common.fixture;

import static com.dangsim.task.entity.TaskStatus.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskFixture {

	public static Task task(String title, String content, User user) {
		return Task.of(
			title,
			content,
			new Address("서울시", "강남구", "123"),
			LocalDateTime.of(2000, 1, 1, 5, 0),
			BigDecimal.ONE,
			TASK_NOT_ASSIGNED,
			user,
			List.of(
				TaskImage.from("test1.jpg"),
				TaskImage.from("test2.jpg")
			)
		);
	}
}
