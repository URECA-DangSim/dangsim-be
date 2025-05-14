package com.dangsim.task.service;

import static com.dangsim.payment.entity.PaymentStatus.*;
import static com.dangsim.task.entity.TaskStatus.*;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.common.util.IdentifierUtils;
import com.dangsim.payment.entity.Payment;
import com.dangsim.payment.exception.PaymentErrorCode;
import com.dangsim.payment.repository.PaymentRepository;
import com.dangsim.task.dto.TaskMapper;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDeleteResponse;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskMatchResponse;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskStatus;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final PaymentRepository paymentRepository;
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public TaskResponseDto createTask(TaskRequestDto requestDto, User user) {
		validateEnoughDeadLine(requestDto.deadline(), LocalDateTime.now());

		Task task = TaskMapper.toTask(requestDto, user);
		Task saveTask = taskRepository.save(task);

		String merchantUid = IdentifierUtils.generateMerchantUid(saveTask.getId(), LocalDateTime.now());

		paymentRepository.save(Payment.of(saveTask.getReward(), PAYMENT_SUCCESSES, merchantUid, saveTask, user));

		return TaskMapper.toTaskResponseDto(saveTask, merchantUid);
	}

	private void validateEnoughDeadLine(String deadline, LocalDateTime now) {
		LocalDateTime formattedDeadline = DateTimeFormatUtils.parseDateTime(deadline);

		if (formattedDeadline.isBefore(now.plusMinutes(30))) {
			throw new BaseException(TaskErrorCode.NOT_ENOUGH_DEADLINE);
		}
	}

	@Transactional(readOnly = true)
	public TaskDetailsResponseDto getTaskById(Long taskId, User user) {
		Task findTask = getTaskById(taskId);

		boolean isMyTask = isTaskOwner(findTask, user);

		return TaskMapper.toTaskDetailsResponseDto(findTask, isMyTask);
	}

	private boolean isTaskOwner(Task task, User user) {
		return Objects.equals(task.getUser().getId(), user.getId());
	}

	@Transactional(readOnly = true)
	public CursorPageResponse<TaskSimpleResponseDto> getTasksByCursor(String cursor, int size, User user) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			cursor = DateTimeFormatUtils.formatDateTime(LocalDateTime.now());
		}

		return taskRepository.findTasksByCursor(cursor, size, user);
	}

	@Transactional
	public TaskDeleteResponse deleteTaskById(Long taskId, User user) {
		Task findTask = getTaskById(taskId);

		if (!isTaskOwner(findTask, user)) {
			throw new BaseException(TaskErrorCode.NOT_TASK_OWNER);
		}

		validateNotAssigned(findTask);

		taskRepository.deleteById(taskId);

		return new TaskDeleteResponse(true);
	}

	@Transactional
	public TaskMatchResponse matchPerformer(Long taskId, User performer) {
		Task findTask = getTaskById(taskId);

		validateMatchByTaskAndPerformer(findTask, performer);

		Payment findPayment = paymentRepository.findByTaskId(findTask.getId()).orElseThrow(
			() -> new BaseException(PaymentErrorCode.NOT_FOUND_PAYMENT)
		);

		validateMatchByPaymentAndPerformer(findPayment);

		findPayment.updatePerformer(performer);

		ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.of(findTask, findTask.getUser(), performer));

		return TaskMapper.toTaskMatchResponse(savedChatRoom);
	}

	private static void validateMatchByTaskAndPerformer(Task findTask, User performer) {
		if (Objects.equals(findTask.getStatus(), TASK_COMPLETE) || Objects.equals(findTask.getStatus(),
			TASK_IN_PROGRESS)) {
			throw new BaseException(TaskErrorCode.IS_MATCHING);
		}

		if (Objects.equals(findTask.getUser(), performer)) {
			throw new BaseException(TaskErrorCode.NOT_MATCH_YOURSELF);
		}
	}

	private static void validateMatchByPaymentAndPerformer(Payment findPayment) {
		if (!Objects.isNull(findPayment.getPerformer())) {
			throw new BaseException(PaymentErrorCode.PERFORMER_ALREADY_EXISTS);
		}

		if (!Objects.equals(findPayment.getStatus(), PAYMENT_SUCCESSES)) {
			throw new BaseException(PaymentErrorCode.NOT_MATCH_PAYMENT_STATUS);
		}
	}

	private void validateNotAssigned(Task task) {
		if (TaskStatus.isMatching(task.getStatus())) {
			throw new BaseException(TaskErrorCode.IS_MATCHING);
		}
	}

	private Task getTaskById(Long taskId) {
		return taskRepository.findById(taskId).orElseThrow(
			() -> new BaseException(TaskErrorCode.NOT_FOUND_TASK)
		);
	}
}
