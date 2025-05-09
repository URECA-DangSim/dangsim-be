package com.dangsim.pg.entity;

import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.Check;

import com.dangsim.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_gateway")
@Check(constraints = "amount >= 1 AND amount <= 1000000")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PaymentGateway extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_gateway_id")
	private Long id;

	@Size(max = 255)
//	@Nullable
	@Column(name = "imp_uid", length = 255)
	private String impUid;

	@Size(max = 40)
//	@Nullable
	@Column(name = "merchant_uid", length = 40, unique = true)
	private String merchantUid;

	@Size(max = 30)
//	@NotNull
	// nullable
	@Column(name = "pay_method", length = 30)
	private String payMethod;

	@Size(max = 30)
//	@NotNull
	@Column(name = "pg_provider", length = 30)
	private String pgProvider;

	@Size(max = 50)
	// nullable
	@Column(name = "pg_tid", length = 30)
	private String pgTid;

	@Size(max = 30)
	@Column(name = "pg_id", length = 30)
	private String pgId;

//	@NotNull
// nullable
	@Column(name = "amount")
	private BigDecimal amount; // BigDecimal

	@Size(max = 10)
//	@NotNull
	// nullable
	@Column(name = "currency", length = 10)
	private String currency;

	@Size(max = 30)
//	@NotNull
	@Column(name = "apply_num", length = 30)
	private String applyNum;

	@Size(max = 30)
//	@NotNull
	@Column(name = "buyer_name", length = 30)
	private String buyerName;

	@Size(max = 10)
//	@NotNull
	@Column(name = "card_code")
	private String cardCode;

	@Size(max = 50)
	@Column(name = "card_name", length = 50)
	private String cardName;

	@Size(max = 20)
	@Column(name = "card_number_masked", length = 20)
	private String cardNumberMasked;

	@Column(name = "card_quota")
	// int라매!!!!
	private String cardQuota;

	@Column(name = "card_number")
	private String cardNumber;

//	@NotNull
	@Column(name = "status")
	private PaymentGatewayStatus status;

	@Column(name = "card_type")
	private String cardType;

	@Column(name = "start_at")
	private LocalDateTime startedAt; // 결제 시작시점

	@Column(name = "paid_at")
	private LocalDateTime paidAt; // 결제 완료 시점

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;

	@Column(name = "failed_at")
	private LocalDateTime failedAt;

	@Builder(access = PRIVATE)
	private PaymentGateway(String impUid, String merchantUid, String payMethod, String pgProvider,
						   String pgTid, String pgId, BigDecimal amount, String currency,
						   String applyNum, String buyerName, String cardCode, String cardName,
						   String cardQuota, String cardNumber, PaymentGatewayStatus status, String cardType,
						   LocalDateTime startedAt, LocalDateTime paidAt, LocalDateTime canceledAt, LocalDateTime failedAt) {
		this.impUid = impUid;
		this.merchantUid = merchantUid;
		this.payMethod = payMethod;
		this.pgProvider = pgProvider;
		this.pgTid = pgTid;
		this.pgId = pgId;
		this.amount = amount;
		this.currency = currency;
		this.applyNum = applyNum;
		this.buyerName = buyerName;
		this.cardCode = cardCode;
		this.cardName = cardName;
		this.cardQuota = cardQuota;
		this.cardNumber = cardNumber;
		this.status = status;
		this.cardType = cardType;
		this.startedAt = startedAt;
		this.paidAt = paidAt;
		this.canceledAt = canceledAt;
		this.failedAt = failedAt;
	}

	public static PaymentGateway of(String impUid, String merchantUid, String payMethod, String pgProvider,
									String pgTid, String pgId, BigDecimal amount, String currency,
									String applyNum, String buyerName, String cardCode, String cardName,
									String cardQuota, String cardNumber, PaymentGatewayStatus status, String cardType,
									LocalDateTime startedAt, LocalDateTime paidAt,
									LocalDateTime canceledAt, LocalDateTime failedAt) {

		return PaymentGateway.builder()
				.impUid(impUid)
				.merchantUid(merchantUid)
				.payMethod(payMethod)
				.pgProvider(pgProvider)
				.pgTid(pgTid)
				.pgId(pgId)
				.amount(amount)
				.currency(currency)
				.applyNum(applyNum)
				.buyerName(buyerName)
				.cardCode(cardCode)
				.cardName(cardName)
				.cardQuota(cardQuota)
				.cardNumber(cardNumber)
				.status(status)
				.cardType(cardType)
				.startedAt(startedAt)
				.paidAt(paidAt)
				.canceledAt(canceledAt)
				.failedAt(failedAt)
				.build();
	}
}