package com.dangsim.rewardRefund.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dangsim.rewardRefund.entity.RewardRefundEntity;

@Repository
public interface RewardRefundRepository extends JpaRepository<RewardRefundEntity, Long> {

}