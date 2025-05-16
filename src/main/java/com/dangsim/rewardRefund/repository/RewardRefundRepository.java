package com.dangsim.rewardRefund.repository;

import com.dangsim.rewardRefund.entity.RewardRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRefundRepository extends JpaRepository<RewardRefund, Long> {

}