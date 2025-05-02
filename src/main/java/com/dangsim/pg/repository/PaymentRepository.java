package com.dangsim.pg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.pg.entity.PaymentGateway;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentGateway, Long> {
}
