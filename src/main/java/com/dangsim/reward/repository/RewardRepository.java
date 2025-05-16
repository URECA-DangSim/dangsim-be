package com.dangsim.reward.repository;

import com.dangsim.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    Optional<Reward> findTopByTaskIdAndUserIdOrderByCreatedAtDesc(Long taskId, Long userId);
}