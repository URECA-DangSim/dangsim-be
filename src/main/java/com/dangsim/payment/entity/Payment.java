package com.dangsim.payment.entity;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;

import org.hibernate.annotations.Check;

import com.dangsim.common.entity.BaseEntity;
import com.dangsim.task.entity.Task;
import com.dangsim.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Check(constraints = "reward >= 100 AND reward <= 1000000")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	@NotNull
	@Column(name = "reward", nullable = false)
	private BigDecimal reward;

	@Enumerated(STRING)
	@NotNull
	@Column(name = "status", nullable = false)
	private PaymentStatus status;

	@Size(max = 40)
	@NotNull
	@Column(name = "merchant_uid", length = 40, nullable = false)
	private String merchantUid;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "task_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_payment_task")
	)
	private Task task;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "requester_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_payment_requester")
	)
	private User requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "performer_id",
		foreignKey = @ForeignKey(name = "fk_payment_performer")
	)
	private User performer;

	@Builder(access = PRIVATE)
	private Payment(BigDecimal reward, PaymentStatus status, String merchantUid,
		Task task, User requester, User performer) {
		this.reward = reward;
		this.status = status;
		this.merchantUid = merchantUid;
		this.task = task;
		this.requester = requester;
		this.performer = performer;
	}

	public static Payment of(BigDecimal reward, PaymentStatus status, String merchantUid, Task task, User requester) {
		return Payment.builder()
			.reward(reward)
			.status(status)
			.merchantUid(merchantUid)
			.task(task)
			.requester(requester)
			.build();
	}

	public void updatePerformer(User performer) {
		this.performer = performer;
	}
}
