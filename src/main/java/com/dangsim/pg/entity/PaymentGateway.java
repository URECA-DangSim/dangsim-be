package com.dangsim.pg.entity;

import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.dangsim.task.entity.Task;
import com.dangsim.task.entity.TaskImage;
import com.dangsim.task.entity.TaskStatus;
import com.dangsim.user.entity.Address;
import com.dangsim.user.entity.User;
import org.hibernate.annotations.Check;

import com.dangsim.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.BindParam;

@Entity
@Table(name = "payment_gateway")
@Check(constraints = "amount >= 100 AND amount <= 1000000")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PaymentGateway extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_gateway_id")
	private Long id;

	@Size(max = 40)
	@NotNull
	@Column(name = "merchant_uid", length = 40, nullable = false, unique = true)
	private String merchantUid;

	@Size(max = 255)
	@NotNull
	@Column(name = "imp_uid", length = 255, nullable = false)
	private String impUid;

	@Size(max = 30)
	@NotNull
	@Column(name = "pg_tid", length = 30, nullable = false)
	private String pgTid;

	@Size(max = 10)
	@NotNull
	@Column(name = "currency", length = 10, nullable = false)
	private String currency;

	@Size(max = 30)
	@NotNull
	@Column(name = "method", length = 30, nullable = false)
	private String method;

//	@Size(max = 255)
//	@Column(name = "failReason", length = 255)
//	private String failReason;

	@Size(max = 50)
	@Column(name = "cardCompany", length = 50)
	private String cardCompany;

	@Size(max = 20)
	@Column(name = "cardNumberMasked", length = 20)
	private String cardNumberMasked;

	@NotNull
	@Column(name = "amount", nullable = false)
	private BigDecimal amount; // BigDecimal

	@Column(name = "installment")
	private int installment;

	@NotNull
	@Column(name = "paymentGatewayStatus", nullable = false)
	private PaymentGatewayStatus status;

	@Column(name = "requestedAt")
	private LocalDateTime requestedAt;

	@Column(name = "approvedAt")
	private LocalDateTime approvedAt;

	@Column(name = "cancelledAt")
	private LocalDateTime cancelledAt;

	private String userNickname;

	private String taskTitle;

	@Builder(access = PRIVATE)
	// 250507 : nickName, taskTitle, amount ++
	// 250507 : method 다음 failReason
	private PaymentGateway(String userNickname, String taskTitle,
						   String merchantUid, String impUid, String pgTid, String currency,
		String method, String cardCompany, String cardNumberMasked,
		BigDecimal amount, int installment, PaymentGatewayStatus status,
		LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime cancelledAt) {

		this.userNickname = userNickname;
		this.taskTitle = taskTitle;
		this.merchantUid = merchantUid;
		this.impUid = impUid;
		this.pgTid = pgTid;
		this.currency = currency;
		this.method = method;
//		this.failReason = failReason;
		this.cardCompany = cardCompany;
		this.cardNumberMasked = cardNumberMasked;
		this.amount = amount;
		this.installment = installment;
		this.status = status;
		this.requestedAt = requestedAt;
		this.approvedAt = approvedAt;
		this.cancelledAt = cancelledAt;
	}

	public static PaymentGateway of(String userNickname, String taskTitle,
									String merchantUid, String impUid, String pgTid, String currency,
									String method, String cardCompany, String cardNumberMasked,
									BigDecimal amount, int installment, PaymentGatewayStatus status,
									LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime cancelledAt) {
		return PaymentGateway.builder()
				.userNickname(userNickname)
				.taskTitle(taskTitle)
				.merchantUid(merchantUid)
				.impUid(impUid)
				.pgTid(pgTid)
				.status(status)
				.currency(currency)
				.method(method)
//				.failReason(failReason)
				.cardCompany(cardCompany)
				.cardNumberMasked(cardNumberMasked)
				.amount(amount)
				.installment(installment)
				.status(status)
				.requestedAt(requestedAt)
				.approvedAt(approvedAt)
				.cancelledAt(cancelledAt)
				.build();
	}

//	public void setStatus(PaymentGatewayStatus status) {
//		this.status = status;
//	}
}
