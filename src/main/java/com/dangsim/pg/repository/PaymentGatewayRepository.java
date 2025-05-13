package com.dangsim.pg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.pg.entity.PaymentGateway;
import com.dangsim.pg.entity.PaymentGatewayStatus;

@Repository
public interface PaymentGatewayRepository extends JpaRepository<PaymentGateway, Long> {

	// imp_uid로 결제 내역 조회
	Optional<PaymentGateway> findByImpUid(String impUid);

	// merchant_uid로 결제 내역 조회
	Optional<PaymentGateway> findByMerchantUid(String merchantUid);

	// 특정 결제 상태로 결제 내역 조회 (예: 결제 완료된 내역만 조회)
	List<PaymentGateway> findByStatus(PaymentGatewayStatus status);

	// 추가적으로 필요한 쿼리 메서드들 (예시)
	// 예: 특정 금액 범위 내에서 결제 내역 조회
	List<PaymentGateway> findByAmountBetween(int minAmount, int maxAmount);
}
