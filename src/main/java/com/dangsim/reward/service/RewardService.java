package com.dangsim.reward.service;

import com.dangsim.chat.entity.ChatRoom;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.payment.entity.Payment;
import com.dangsim.payment.repository.PaymentRepository;
import com.dangsim.reward.dto.response.RewardChatResponse;
import com.dangsim.reward.entity.Reward;
import com.dangsim.reward.repository.RewardRepository;
import com.dangsim.task.entity.Task;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;
import com.dangsim.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final ChatRoomRepository chatRoomRepository;
    private final TaskRepository taskRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RewardRepository rewardStatementRepository;

    /**
     * 1. 실제 리워드 지급 로직 (심부름 완료 버튼 클릭 시 호출)
     */
    @Transactional
    public Reward updateRewardByTaskCompleteBtn(Long chatId) {
        // 1. chatId → ChatRoom
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        // 2. ChatRoom → Task
        Task task = chatRoom.getTask();
        Long taskId = task.getId();

        // 3. Task → 리워드, 마감 시간
        BigDecimal rewardAmount = task.getReward();
        LocalDateTime deadline = task.getDeadline();

        // 4. Task → Payment → performerId
        Payment payment = paymentRepository.findByTaskId(taskId)
                .orElseThrow(() -> new EntityNotFoundException("해당 Task에 대한 결제 정보를 찾을 수 없습니다."));
        Long performerId = payment.getPerformer().getId();

        // 5. performer → User
        User performer = userRepository.findById(performerId)
                .orElseThrow(() -> new EntityNotFoundException("해당 수행자 유저를 찾을 수 없습니다."));

        BigDecimal beforeReward = performer.getReward();
        BigDecimal afterReward = beforeReward.add(rewardAmount);

        // 6. User reward 값 업데이트
        performer.updateReward(afterReward);
        userRepository.save(performer);

        // 7. 정산 내역 저장
        Reward statement = Reward.of(
                rewardAmount,
                task,
                performer,
                LocalDateTime.now()
        );
        rewardStatementRepository.save(statement);

        return statement;
    }

        /**
         * 2. 리워드 정산 후 결과 반환
         */
        @Transactional(readOnly = true)
        public RewardChatResponse rewardByChatId (Long chatId){
            ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 채팅방을 찾을 수 없습니다."));
            Long taskId = chatRoom.getTask().getId();
            Long performerId = chatRoom.getPerformer().getId();

            Reward statement = rewardStatementRepository
                    .findTopByTaskIdAndUserIdOrderByCreatedAtDesc(taskId, performerId)
                    .orElseThrow(() -> new EntityNotFoundException("정산 내역을 찾을 수 없습니다."));

            return RewardChatResponse.from(statement);
        }
    }
