package com.dangsim.task.repository;

import static com.dangsim.task.entity.QTask.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.task.dto.response.QTaskSimpleResponseDto;
import com.dangsim.task.dto.response.TaskSimpleResponseDto;
import com.dangsim.task.entity.TaskStatus;
import com.dangsim.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskQueryRepositoryImpl implements TaskQueryRepository {

	private final JPAQueryFactory queryFactory;
	private static final String BASE_CITY = "서울특별시";
	private static final String BASE_STREET = "강남구";
	private static final String BASE_ZIPCODE = "대치2동";

	@Override
	public CursorPageResponse<TaskSimpleResponseDto> findTasksByCursor(String cursor, int size, User user) {

		List<TaskSimpleResponseDto> items = queryFactory
			.select(new QTaskSimpleResponseDto(task))
			.from(task)
			.where(task.createdAt.lt(DateTimeFormatUtils.parseDateTime(cursor))
				.and(isAddressEq(user))
				.and(task.status.eq(TaskStatus.TASK_NOT_ASSIGNED))
			)
			.orderBy(task.createdAt.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = items.size() > size;

		List<TaskSimpleResponseDto> pageItems = subLastPage(items, hasNext);

		String nextCursor = getNextCursor(pageItems, hasNext);

		return new CursorPageResponse<>(pageItems, nextCursor, hasNext);
	}

	private static String getNextCursor(List<TaskSimpleResponseDto> items, boolean hasNext) {
		if (Objects.isNull(items) || items.isEmpty() || !hasNext) {
			return null;
		}

		return items.get(items.size() - 1).createdAt();
	}

	private BooleanExpression isAddressEq(User user) {
		if (Objects.isNull(user)) {
			return task.address.city.eq(BASE_CITY)
				.and(task.address.street.eq(BASE_STREET))
				.and(task.address.zipcode.eq(BASE_ZIPCODE));
		}

		return task.address.city.eq(user.getAddress().getCity())
			.and(task.address.street.eq(user.getAddress().getStreet()))
			.and(task.address.zipcode.eq(user.getAddress().getZipcode()));
	}

	private List<TaskSimpleResponseDto> subLastPage(List<TaskSimpleResponseDto> items, boolean hasNext) {
		if (Objects.isNull(items) || items.isEmpty()) {
			return Collections.emptyList();
		}

		if (hasNext) {
			return items.subList(0, items.size() - 1);
		}

		return items;
	}

}
