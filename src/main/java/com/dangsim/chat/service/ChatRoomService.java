package com.dangsim.chat.service;

import com.dangsim.chat.dto.request.CreateChatRoomRequest;
import com.dangsim.chat.repository.ChatRoomRepository;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.task.entity.Task;
import com.dangsim.task.exception.TaskErrorCode;
import com.dangsim.task.repository.TaskRepository;
import com.dangsim.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final TaskRepository taskRepository;

    public void createChatRoom(@Valid CreateChatRoomRequest request, User performer) {
        //Task task = taskRepository.findById(request.taskID()).orElseThrow(()->new BaseException(TaskErrorCode.));

    }
    //task 아이디 가지고 테스크 객체를 가져올 수 있다
    // 그리고 테스크 객체를 가지고 심부름의 주인 requester 를 찾을 수 있겟죠 = 유저 자체를 가지고 오니깐 객체겠죠
    //퍼포머는 authentication 뭐시기로
    //찾아서 chat room 엔티티를 만들어서 저장해라 .


}
