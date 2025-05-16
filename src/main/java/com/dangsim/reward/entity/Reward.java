package com.dangsim.reward.entity;

import com.dangsim.common.entity.BaseEntity;
import com.dangsim.task.entity.Task;
import com.dangsim.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "reward_statement")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_statement_id")
    private Long id;

    // 수행자가 가지고 있던 리워드 값
    @NotNull
    @Column(name = "before_reward", nullable = false)
    private BigDecimal beforeReward;

    // task의 리워드 값
    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    // 수행자의 최종 리워드 값
    @NotNull
    @Column(name = "after_reward", nullable = false)
    private BigDecimal afterReward;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reward_task"))
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reward_user"))
    private User user;

    @NotNull
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder(access = PRIVATE)
    public Reward(BigDecimal amount, Task task, User user, LocalDateTime completedAt) {
        this.amount = amount;
        this.task = task;
        this.user = user;
        this.requestedAt = LocalDateTime.now();
        this.completedAt = completedAt;
    }

    public static Reward of(BigDecimal amount, Task task, User user, LocalDateTime completedAt) {
        return Reward.builder()
                .amount(amount)
                .task(task)
                .user(user)
                .requestedAt(LocalDateTime.now())
                .completedAt(completedAt)
                .build();
    }

    public void markCompleted() {
        this.completedAt = LocalDateTime.now();
    }
}
