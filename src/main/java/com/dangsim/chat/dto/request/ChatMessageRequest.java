package com.dangsim.chat.dto.request;

import com.dangsim.chat.dto.MessageType;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
        @NotNull
        MessageType type,
        @NotNull
        Long userId,
        String message
) {
}
