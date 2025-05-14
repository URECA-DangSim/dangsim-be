package com.dangsim.rewardRefund.entity;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reward_request")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardRefundEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reward_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_user"))
	private User user;

	@NotNull
	@Column(name = "amount")
	private BigDecimal amount;

	@Size(max = 20)
	@NotNull
	@Column(name = "bank_name")
	private String bankName;

	@Size(max = 30)
	@NotNull
	@Column(name = "bank_account")
	private String bankAccount;

	@Size(max = 24)
	@NotNull
	@Column(name = "holder_name")
	private String holderName;

	@Enumerated(STRING)
	@Column(name = "status")
	private RewardRefundStatus status;

	@NotNull
	@Column(name = "request_at")
	private LocalDateTime requestedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Builder(access = PRIVATE)
	private RewardRefundEntity(User user, BigDecimal amount, String bankName, String bankAccount, String holderName,
		RewardRefundStatus status) {
		this.user = user;
		this.amount = amount;
		this.bankName = bankName;
		this.bankAccount = bankAccount;
		this.holderName = holderName;
		this.status = status;
		this.requestedAt = LocalDateTime.now();
	}

	public static RewardRefundEntity of(User user, BigDecimal amount, String bankName, String bankAccount,
		String holderName, RewardRefundStatus status) {
		return RewardRefundEntity.builder()
			.user(user)
			.amount(amount)
			.bankName(bankName)
			.bankAccount(bankAccount)
			.holderName(holderName)
			.status(status)
			.requestedAt(LocalDateTime.now())
			.build();
	}
}