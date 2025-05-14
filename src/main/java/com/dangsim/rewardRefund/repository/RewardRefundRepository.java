package com.dangsim.rewardRefund.repository;

import com.dangsim.rewardRefund.entity.RewardRefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRefundRepository extends JpaRepository<RewardRefundEntity, Long> {

}