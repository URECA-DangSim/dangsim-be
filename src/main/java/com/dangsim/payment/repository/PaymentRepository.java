package com.dangsim.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dangsim.payment.entity.Payment;

import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByMerchantUid(String merchantUid);

}
