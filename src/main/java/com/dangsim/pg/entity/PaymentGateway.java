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
import jakarta.validation.constraints.Null;
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
	// nullable
	@Column(name = "merchant_uid", length = 40, unique = true)
	private String merchantUid;

	@Size(max = 255)
	// nullable
	@Column(name = "imp_uid", length = 255)
	private String impUid;

	@Size(max = 50)
	// nullable
	@Column(name = "pg_tid", length = 30)
	private String pgTid;

	@Size(max = 10)
//	@NotNull
	// nullable
	@Column(name = "currency", length = 10)
	private String currency;

	@Size(max = 30)
//	@NotNull
	// nullable
	@Column(name = "method", length = 30)
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

//	@NotNull
// nullable
	@Column(name = "amount")
	private BigDecimal amount; // BigDecimal

	@Column(name = "installment")
	private int installment;

//	@NotNull
// nullable
	@Column(name = "paymentGatewayStatus")
	private PaymentGatewayStatus status;

	@Column(name = "requestedAt")
//	private LocalDateTime requestedAt;
	private LocalDateTime startedAt; // 결제 시작시점

	@Column(name = "approvedAt")
//	private LocalDateTime approvedAt;
	private LocalDateTime paidAt; // 결제 완료 시점

	@Column(name = "cancelledAt")
	private LocalDateTime cancelledAt;

	// new!
	@Column(name = "failedAt")
	private LocalDateTime failedAt;


	@Builder(access = PRIVATE)
	// 250507 : method 다음 failReason
	private PaymentGateway(String merchantUid, String impUid, String pgTid, String currency,
		String method, String cardCompany, String cardNumberMasked,
		BigDecimal amount, Integer installment, PaymentGatewayStatus status,
		LocalDateTime startedAt, LocalDateTime paidAt, LocalDateTime cancelledAt, LocalDateTime failedAt) {

		this.merchantUid = merchantUid;
		this.impUid = impUid;
		this.pgTid = pgTid;
		this.currency = currency;
		this.method = method;
//		this.failReason = failReason;
		this.cardCompany = cardCompany;
		this.cardNumberMasked = cardNumberMasked;
		this.amount = amount;
		this.installment = (installment != null) ? installment : 0; // installment이 null일 경우 0으로 설정
		this.status = status;
		this.startedAt = startedAt;
		this.paidAt = paidAt;
		this.cancelledAt = cancelledAt;
		this.failedAt = failedAt;
	}

	public static PaymentGateway of(String merchantUid, String impUid, String pgTid, String currency,
									String method, String cardCompany, String cardNumberMasked,
									BigDecimal amount, Integer installment, PaymentGatewayStatus status,
									LocalDateTime startedAt, LocalDateTime paidAt) {

		return PaymentGateway.builder()
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
//				.installment((installment != null) ? installment : 0) // installment이 null일 경우 0으로 설정
				.status(status)
				.startedAt(startedAt)
				.paidAt(paidAt)
//				.cancelledAt(cancelledAt)
//				.failedAt(failedAt)
				.build();
	}
}
