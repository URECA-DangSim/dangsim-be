package com.dangsim.chat.repository;

import static com.dangsim.chat.entity.QChatMessage.*;
import static com.dangsim.chat.entity.QChatRoom.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.dangsim.chat.dto.response.ChatMessageDetailResponse;
import com.dangsim.chat.dto.response.ChatRoomDetailResponse;
import com.dangsim.chat.dto.response.ChatRoomSimpleResponse;
import com.dangsim.chat.dto.response.QChatMessageDetailResponse;
import com.dangsim.chat.dto.response.QChatRoomSimpleResponse;
import com.dangsim.chat.entity.QChatMessage;
import com.dangsim.common.CursorPageResponse;
import com.dangsim.common.util.DateTimeFormatUtils;
import com.dangsim.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public CursorPageResponse<ChatRoomSimpleResponse> findChatRoomsByCursor(
		String cursor, int size, Long userId) {

		QChatMessage lastMessage = new QChatMessage("lastMessage");
		QUser partner = new QUser("partner");

		BooleanExpression isRequester = chatRoom.requester.id.eq(userId)
			.and(partner.id.eq(chatRoom.performer.id));
		BooleanExpression isPerformer = chatRoom.performer.id.eq(userId)
			.and(partner.id.eq(chatRoom.requester.id));
		BooleanExpression partnerJoinCondition = isRequester.or(isPerformer);

		BooleanExpression cursorFilter = null;
		if (cursor != null && !cursor.isBlank()) {
			cursorFilter = lastMessage.id.lt(Long.parseLong(cursor));
		}

		List<ChatRoomSimpleResponse> chatRooms = queryFactory
			.select(new QChatRoomSimpleResponse(chatRoom, lastMessage, partner))
			.from(chatRoom)
			.leftJoin(lastMessage)
			.on(lastMessage.chatRoomId.eq(chatRoom.id),
				lastMessage.id.eq(
					JPAExpressions
						.select(chatMessage.id.max())
						.from(chatMessage)
						.where(chatMessage.chatRoomId.eq(chatRoom.id))
				))
			.leftJoin(partner)
			.on(partnerJoinCondition)
			.where(
				chatRoom.requester.id.eq(userId)
					.or(chatRoom.performer.id.eq(userId)),
				cursorFilter
			)
			.orderBy(lastMessage.id.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = chatRooms.size() > size;
		List<ChatRoomSimpleResponse> pageChatRooms = subLastPage(chatRooms, hasNext);
		String nextCursor = getNextCursor(pageChatRooms, hasNext);

		return new CursorPageResponse<>(pageChatRooms, nextCursor, hasNext);
	}

	@Override
	public CursorPageResponse<ChatRoomDetailResponse> findChatMessagesByCursor(Long chatRoomId, String cursor, int size,
		Long userId) {
		List<ChatMessageDetailResponse> allMessages = queryFactory
			.select(new QChatMessageDetailResponse(chatMessage))
			.from(chatMessage)
			.where(
				chatMessage.chatRoomId.eq(chatRoomId),
				chatMessageCursorFilter(cursor)
			)
			.orderBy(chatMessage.id.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = allMessages.size() > size;
		List<ChatMessageDetailResponse> pageMessages = subLastPage(allMessages, hasNext);
		String nextCursor = getNextCursorByMessage(pageMessages, hasNext);
		System.out.println("nextCursor = " + nextCursor);

		ChatRoomDetailResponse detailResponse = new ChatRoomDetailResponse(chatRoomId, pageMessages);

		return new CursorPageResponse<>(Collections.singletonList(detailResponse), nextCursor, hasNext);
	}

	private BooleanExpression chatRoomCursorFilter(String cursor) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			return null;
		}

		return chatRoom.id.lt(Long.parseLong(cursor));
	}

	private BooleanExpression chatMessageCursorFilter(String cursor) {
		if (Objects.isNull(cursor) || cursor.isBlank()) {
			return null;
		}

		return chatMessage.id.lt(Long.parseLong(cursor));
	}

	private BooleanExpression isBeforeCursor(DateTimePath<LocalDateTime> dateTimePath, String cursor) {
		if (cursor == null) {
			return null;
		}
		return dateTimePath.lt(DateTimeFormatUtils.parseDateTime(cursor));
	}

	private String getNextCursor(List<ChatRoomSimpleResponse> chatrooms, boolean hasNext) {
		if (Objects.isNull(chatrooms) || chatrooms.isEmpty() || !hasNext) {
			return null;
		}
		return String.valueOf(chatrooms.get(chatrooms.size() - 1).chatMessageId());
	}

	private String getNextCursorByMessage(List<ChatMessageDetailResponse> messages, boolean hasNext) {
		if (Objects.isNull(messages) || messages.isEmpty() || !hasNext) {
			return null;
		}
		return String.valueOf(messages.get(messages.size() - 1).messageId());
	}

	private <T> List<T> subLastPage(List<T> list, boolean hasNext) {
		if (Objects.isNull(list) || list.isEmpty()) {
			return Collections.emptyList();
		}

		if (hasNext) {
			return list.subList(0, list.size() - 1);
		}
		return list;
	}

}
