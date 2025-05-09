package com.dangsim.task.repository;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.user.entity.User;

public interface TaskQueryRepository {

	public CursorPageResponse<TaskSimpleResponseDto> findTasksByCursor(String cursor, int size, User user);
}
