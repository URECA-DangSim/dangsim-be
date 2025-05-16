//package com.dangsim.reward.service;
//
//import com.dangsim.chat.entity.ChatRoom;
//import com.dangsim.chat.repository.ChatRoomRepository;
//import com.dangsim.payment.entity.Payment;
//import com.dangsim.payment.repository.PaymentRepository;
//import com.dangsim.reward.entity.Reward;
//import com.dangsim.reward.repository.RewardRepository;
//import com.dangsim.task.entity.Task;
//import com.dangsim.task.repository.TaskRepository;
//import com.dangsim.user.entity.User;
//import com.dangsim.user.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//
//
//@ExtendWith(MockitoExtension.class)
//public class RewardServiceTest {
//    @Mock
//    private ChatRoomRepository chatRoomRepository;
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RewardRepository rewardRepository;
//
//    @InjectMocks
//    private RewardService rewardService;
//
//    @DisplayName("심부름 완료 버튼 클릭 시 수행자의 리워드가 정상 지급된다.")
//    @Test
//    void updateRewardByTaskCompleteBtn_success() {
//        // given
//        Long chatId = 1L;
//        Long taskId = 10L;
//        Long performerId = 20L;
//
//        BigDecimal rewardAmount = BigDecimal.valueOf(1000);
//        BigDecimal initialUserReward = BigDecimal.valueOf(5000);
//        BigDecimal expectedReward = initialUserReward.add(rewardAmount);
//
//        User performer = UserFixture.create(performerId, performerRewardBefore);
//        Task task = TaskFixture.create(taskId, rewardAmount, LocalDateTime.now().plusDays(1));
//        ChatRoom chatRoom = ChatRoomFixture.create(chatId, task);
//        Payment payment = PaymentFixture.create(100L, task, performer);
//
//        given(chatRoomRepository.findById(chatId)).willReturn(Optional.of(chatRoom));
//        given(paymentRepository.findByTaskId(taskId)).willReturn(Optional.of(payment));
//        given(userRepository.findById(performerId)).willReturn(Optional.of(performer));
//        given(rewardRepository.save(org.mockito.ArgumentMatchers.any(Reward.class)))
//                .willAnswer(invocation -> invocation.getArgument(0));
//
//        // when
//        Reward result = rewardService.updateRewardByTaskCompleteBtn(chatId);
//
//        // then
//        assertThat(result.getUser().getReward()).isEqualByComparingTo(expectedReward);
//        assertThat(result.getAmount()).isEqualByComparingTo(rewardAmount);
//        assertThat(result.getTask().getId()).isEqualTo(taskId);
//    }
//
//}
