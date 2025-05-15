package com.dangsim.common.fixture;

import java.math.BigDecimal;

import com.dangsim.payment.entity.Payment;
import com.dangsim.payment.entity.PaymentStatus;
import com.dangsim.task.entity.Task;
import com.dangsim.user.entity.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentFixture {

	public static Payment payment(Task task, User requester, User performer) {
		Payment payment = Payment.of(
			BigDecimal.ONE,
			PaymentStatus.PAYMENT_WAITING,
			"merchantUid",
			task,
			requester
		);
		payment.updatePerformer(performer);
		return payment;
	}

	public static Payment payment(PaymentStatus status, Task task, User requester, User performer) {
		Payment payment = Payment.of(
			BigDecimal.ONE,
			status,
			"merchantUid",
			task,
			requester
		);
		payment.updatePerformer(performer);
		return payment;
	}
}
