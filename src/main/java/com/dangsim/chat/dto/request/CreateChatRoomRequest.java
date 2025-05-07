package com.dangsim.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateChatRoomRequest(

        @NotNull
        Long taskID

) {
}
