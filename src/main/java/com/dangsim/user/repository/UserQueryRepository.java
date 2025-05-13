package com.dangsim.user.repository;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.user.dto.response.UserTaskResponse;
import com.dangsim.user.entity.User;

public interface UserQueryRepository {
	CursorPageResponse<UserTaskResponse> findRequestedTasksByCursor(String cursor, int size, User user);

	CursorPageResponse<UserTaskResponse> findPerformedTasksByCursor(String cursor, int size, User user);
}