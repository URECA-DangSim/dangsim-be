package com.dangsim.user.repository;

import static com.dangsim.payment.entity.QPayment.*;
import static com.dangsim.task.entity.QTask.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.user.dto.response.QUserTaskResponse;
import com.dangsim.user.dto.response.UserTaskResponse;
import com.dangsim.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public CursorPageResponse<UserTaskResponse> findRequestedTasksByCursor(String cursor, int size, User user) {

		List<UserTaskResponse> items = queryFactory
			.select(new QUserTaskResponse(task))
			.from(task)
			.where(
				cursorFilter(cursor),
				task.user.eq(user)
			)
			.orderBy(task.createdAt.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = items.size() > size;
		List<UserTaskResponse> pageItems = subLastPage(items, hasNext);
		String nextCursor = getNextCursor(pageItems, hasNext);

		return new CursorPageResponse<>(pageItems, nextCursor, hasNext);
	}

	@Override
	public CursorPageResponse<UserTaskResponse> findPerformedTasksByCursor(String cursor, int size, User user) {
		List<UserTaskResponse> items = queryFactory
			.select(new QUserTaskResponse(task))
			.from(payment)
			.join(payment.task, task)
			.where(
				cursorFilter(cursor),
				payment.performer.eq(user)
			)
			.orderBy(task.createdAt.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = items.size() > size;
		List<UserTaskResponse> pageItems = subLastPage(items, hasNext);
		String nextCursor = getNextCursor(pageItems, hasNext);

		return new CursorPageResponse<>(pageItems, nextCursor, hasNext);
	}

	private BooleanExpression cursorFilter(String cursor) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			return null;
		}

		return task.id.lt(Long.parseLong(cursor));
	}

	private static String getNextCursor(List<UserTaskResponse> items, boolean hasNext) {
		if (Objects.isNull(items) || items.isEmpty() || !hasNext) {
			return null;
		}
		return String.valueOf(items.get(items.size() - 1).taskId());
	}

	private List<UserTaskResponse> subLastPage(List<UserTaskResponse> items, boolean hasNext) {
		if (Objects.isNull(items) || items.isEmpty()) {
			return Collections.emptyList();
		}
		if (hasNext) {
			return items.subList(0, items.size() - 1);
		}

		return items;
	}

}
