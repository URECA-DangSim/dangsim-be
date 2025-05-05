package com.dangsim.task.repository;

import static com.dangsim.task.entity.QTask.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.task.dto.response.QTaskSimpleResponseDto;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	@Transactional(readOnly = true)
	public CursorPageResponse<TaskSimpleResponseDto> findTasksByCursor(String cursor, int size) {

		List<TaskSimpleResponseDto> items = queryFactory
			.select(new QTaskSimpleResponseDto(task))
			.from(task)
			.where(task.createdAt.lt(DateTimeFormatUtils.parseDate(cursor)))
			.orderBy(task.createdAt.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = items.size() > size;

		String nextCursor = getNextCursor(items, size, hasNext);

		return new CursorPageResponse<>(items, nextCursor, hasNext);
	}

	private static String getNextCursor(List<TaskSimpleResponseDto> items, int size, boolean hasNext) {
		if (hasNext) {
			return items.get(size).createdAt();
		}

		return null;
	}
}
