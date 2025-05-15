package com.dangsim.task.service;

import static com.dangsim.payment.entity.PaymentStatus.*;
import static com.dangsim.task.entity.TaskStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.dangsim.payment.entity.PaymentStatus;
import com.dangsim.pg.repository.PaymentGatewayRepository;
import com.dangsim.pg.service.PaymentGatewayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.fixture.ChatRoomFixture;
import com.dangsim.common.fixture.PaymentFixture;
import com.dangsim.common.fixture.TaskFixture;
import com.dangsim.common.fixture.UserFixture;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.payment.entity.Payment;
import com.dangsim.payment.exception.PaymentErrorCode;
import com.dangsim.payment.repository.PaymentRepository;
import com.dangsim.task.dto.request.TaskRequestDto;
import com.dangsim.task.dto.response.TaskDeleteResponse;
import com.dangsim.task.dto.response.TaskDetailsResponseDto;
import com.dangsim.task.dto.response.TaskMatchResponse;
import com.dangsim.task.dto.response.TaskResponseDto;
import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskStatus;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@Mock
	TaskRepository taskRepository;

	@Mock
	PaymentRepository paymentRepository;

	@Mock
	ChatRoomRepository chatRoomRepository;

	@InjectMocks
	TaskService taskService;

	@Mock
	PaymentGatewayRepository paymentGatewayRepository;

	@InjectMocks
	PaymentGatewayService paymentGatewayService;

	@DisplayName("일치하는 심부름 요청이 없으면 예외가 발생한다.")
	@Test
	void throwNotFoundExceptionWhenNotExistTaskByTaskId() {
		// given
		Long taskId = 1L;
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);

		// when
		given(taskRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// then
		assertThatThrownBy(() -> taskService.getTaskById(taskId, user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.NOT_FOUND_TASK.getMessage());
	}

	@DisplayName("해당하는 심부름 요청 상세 정보를 가져온다.")
	@Test
	void getTaskDetailsByTaskId() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), requester);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent())
		);
	}

	@DisplayName("심부름 요청 정보를 가져올 때 자신의 요청인지 검증한다.")
	@Test
	void getTaskDetailsByTaskIdWithOwner() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), requester);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent()),
			() -> assertThat(responseDto.isMyTask()).isTrue()
		);
	}

	@DisplayName("심부름 요청 정보를 가져올 때 다른 사람의 요청인지 검증한다.")
	@Test
	void getTaskDetailsByTaskIdWithOther() {
		// given
		final String title = "test1";
		final String content = "content1";
		User requester = UserFixture.user(Role.USER, BigDecimal.ONE);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(requester, "id", 1L);
		ReflectionTestUtils.setField(other, "id", 2L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDetailsResponseDto responseDto = taskService.getTaskById(task.getId(), other);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertThat(responseDto.title()).isEqualTo(task.getTitle()),
			() -> assertThat(responseDto.content()).isEqualTo(task.getContent()),
			() -> assertThat(responseDto.isMyTask()).isFalse()
		);
	}

	@DisplayName("심부름을 저장한다.")
	@Test
	void createTask() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다";
		final String deadline = DateTimeFormatUtils.formatDateTime(LocalDateTime.now().plusHours(3));
		final int reward = 1000;
		final String address = "서울특별시 강남구 대치2동";
		List<String> imageUrls = Collections.EMPTY_LIST;

		TaskRequestDto requestDto = new TaskRequestDto(title, content, deadline, reward, address, imageUrls);

		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(title, content, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.save(any(Task.class))).willReturn(task);
		given(paymentRepository.save(any(Payment.class))).willReturn(any());

		// when
		TaskResponseDto responseDto = taskService.createTask(requestDto, user);

		// then
		assertAll(
			() -> assertThat(responseDto.taskId()).isEqualTo(task.getId()),
			() -> assertTrue(responseDto.merchantUid().startsWith(task.getId().toString())),
			() -> assertThat(responseDto.result()).isTrue()
		);
	}

	@DisplayName("심부름 마감 기한이 30분 이내라면 예외가 발생한다.")
	@Test
	void throwFailSaveExceptionWhenNotEnoughDeadLine() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다";
		final String deadline = DateTimeFormatUtils.formatDateTime(LocalDateTime.now().minusMinutes(15));
		final int reward = 1000;
		final String address = "서울특별시 강남구 대치2동";
		List<String> imageUrls = Collections.EMPTY_LIST;

		TaskRequestDto requestDto = new TaskRequestDto(title, content, deadline, reward, address, imageUrls);

		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(title, content, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		// when  // then
		assertThatThrownBy(() -> taskService.createTask(requestDto, user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.NOT_ENOUGH_DEADLINE.getMessage());
	}

	@DisplayName("요청자가 해당 심부름에 작성자가 아니라면 예외가 발생한다.")
	@Test
	void throwNotTaskOwnerExceptionWhenRequesterIsNotOwnerOfTask() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다.";

		User requester = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 2L);

		Task task = TaskFixture.task(title, content, other);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when		// then
		assertThatThrownBy(() -> taskService.deleteTaskById(task.getId(), requester))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.NOT_TASK_OWNER.getMessage());
	}

	@DisplayName("해당 심부름이 진행 상태라면 예외가 발생한다.")
	@Test
	void throwIsMatchingExceptionWhenTaskStatusIsProgress() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다.";

		User requester = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);
		ReflectionTestUtils.setField(task, "status", TaskStatus.TASK_IN_PROGRESS);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when		// then
		assertThatThrownBy(() -> taskService.deleteTaskById(task.getId(), requester))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.IS_MATCHING.getMessage());
	}

	@DisplayName("해당 심부름이 완료 상태라면 예외가 발생한다.")
	@Test
	void throwIsMatchingExceptionWhenTaskStatusIsComplete() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다.";

		User requester = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);
		ReflectionTestUtils.setField(task, "status", TaskStatus.TASK_COMPLETE);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when		// then
		assertThatThrownBy(() -> taskService.deleteTaskById(task.getId(), requester))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.IS_MATCHING.getMessage());
	}

	@DisplayName("미매칭된 자신의 심부름 요청을 삭제할 수 있다.")
	@Test
	void deleteTaskByOwnerAndNotMatched() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다.";

		User requester = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "id", 1L);
		ReflectionTestUtils.setField(task, "status", TASK_NOT_ASSIGNED);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when
		TaskDeleteResponse responseDto = taskService.deleteTaskById(task.getId(), requester);

		// then
		assertTrue(responseDto.result());
	}

	@DisplayName("결제 및 심부름 상태가 정상적으로 업데이트된다.")
	@Test
	void updatePaymentAndTaskStatus_successfullyUpdatesStatuses() {
		// given
		final String title = "제목입니다.";
		final String content = "내용입니다.";
		String merchantUid = "merchant_12345";

		User requester = UserFixture.user(Role.USER, BigDecimal.ZERO);
		ReflectionTestUtils.setField(requester, "id", 1L);

		Task task = TaskFixture.task(title, content, requester);
		ReflectionTestUtils.setField(task, "status", TaskStatus.TASK_NOT_ASSIGNED);

		Payment payment = mock(Payment.class);
		given(paymentRepository.findByMerchantUid(merchantUid)).willReturn(Optional.of(payment));
		given(payment.getTask()).willReturn(task);

		// when
		paymentGatewayService.updatePaymentAndTaskStatus(merchantUid);

		// then
		verify(payment).updatePaymentSuccessStatus(PaymentStatus.PAYMENT_SUCCESSES);
		assertThat(task.getStatus()).isEqualTo(TaskStatus.TASK_IN_PROGRESS);
	}

	@DisplayName("수행자를 매칭할 때 심부름 상태가 완료 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenTaskStatusIsComplete() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(TaskStatus.TASK_COMPLETE, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.IS_MATCHING.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 심부름 상태가 진행 중 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenTaskStatusIsProgress() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(TaskStatus.TASK_IN_PROGRESS, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.IS_MATCHING.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 요청자 자신의 심부름이라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenTaskIsOwner() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), user))
			.isInstanceOf(BaseException.class)
			.hasMessage(TaskErrorCode.NOT_MATCH_YOURSELF.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제를 찾을 수 없다면 예외가 발생한다.")
	@Test
	void throwExceptionWhenNotFoundPayment() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.ofNullable(null));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.NOT_FOUND_PAYMENT.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제의 수행자가 존재한다면 예외가 발생한다.")
	@Test
	void throwExceptionWhenExistPerformer() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(task, user, other);
		ReflectionTestUtils.setField(payment, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.PERFORMER_ALREADY_EXISTS.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제 상태가 결제 전 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenPaymentStatusIsWaiting() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(PAYMENT_WAITING, task, user, null);
		ReflectionTestUtils.setField(payment, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.NOT_MATCH_PAYMENT_STATUS.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제 상태가 결제 실패 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenPaymentStatusIsFail() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(PAYMENT_FAILED, task, user, null);
		ReflectionTestUtils.setField(payment, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.NOT_MATCH_PAYMENT_STATUS.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제 상태가 결제 정산 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenPaymentStatusIsCalculated() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(PAYMENT_CALCULATED, task, user, null);
		ReflectionTestUtils.setField(payment, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.NOT_MATCH_PAYMENT_STATUS.getMessage());
	}

	@DisplayName("수행자를 매칭할 때 연관된 결제 상태가 결제 환불 상태라면 예외가 발생한다.")
	@Test
	void throwExceptionWhenPaymentStatusIsRefunded() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(PAYMENT_REFUNDED, task, user, null);
		ReflectionTestUtils.setField(payment, "id", 1L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));

		// when	// then
		assertThatThrownBy(() -> taskService.matchPerformer(task.getId(), other))
			.isInstanceOf(BaseException.class)
			.hasMessage(PaymentErrorCode.NOT_MATCH_PAYMENT_STATUS.getMessage());
	}

	@DisplayName("수행자를 매칭이 성공되면, 채팅방이 생성된다")
	@Test
	void createChatRoomWhenMatchIsSuccess() {
		// given
		User user = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);
		User other = UserFixture.user(Role.USER, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 2L);

		Task task = TaskFixture.task(TASK_NOT_ASSIGNED, user);
		ReflectionTestUtils.setField(task, "id", 1L);

		Payment payment = PaymentFixture.payment(PAYMENT_SUCCESSES, task, user, null);
		ReflectionTestUtils.setField(payment, "id", 1L);

		ChatRoom chatRoom = ChatRoomFixture.chatRoom(task, user, other);
		ReflectionTestUtils.setField(chatRoom, "id", 156L);

		given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(task));
		given(paymentRepository.findByTaskId(any(Long.class))).willReturn(Optional.of(payment));
		given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

		// when
		TaskMatchResponse response = taskService.matchPerformer(task.getId(), other);

		// then
		assertAll(
			() -> assertThat(response.chatRoomId()).isEqualTo(chatRoom.getId()),
			() -> assertThat(task.getStatus()).isEqualTo(TASK_IN_PROGRESS)
		);
	}
}
