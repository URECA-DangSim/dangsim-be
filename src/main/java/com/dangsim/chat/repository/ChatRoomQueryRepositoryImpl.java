package com.dangsim.chat.repository;

import static com.dangsim.chat.entity.QChatMessage.*;
import static com.dangsim.chat.entity.QChatRoom.*;

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
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public CursorPageResponse<ChatRoomSimpleResponse> findChatRoomsByCursor(String cursor, int size, Long userId) {

		QChatMessage lastMessage = new QChatMessage("lastMessage");
		QUser partner = new QUser("partner");

		List<ChatRoomSimpleResponse> chatRooms = queryFactory
			.select(new QChatRoomSimpleResponse(chatRoom, lastMessage, partner))
			.from(chatRoom)
			//최근 메시지 조인
			.leftJoin(lastMessage)
			.on(lastMessage.chatRoomId.eq(chatRoom.id),
				lastMessage.id.eq(
					JPAExpressions
						.select(chatMessage.id.max())
						.from(chatMessage)
						.where(chatMessage.chatRoomId.eq(chatRoom.id))
				))
			//상대방 유저 조인
			.leftJoin(partner)
			.on(
				chatRoom.requester.id.eq(userId).and(partner.id.eq(chatRoom.performer.id))
					.or(chatRoom.performer.id.eq(userId).and(partner.id.eq(chatRoom.requester.id)))
			)
			.where(
				chatRoom.requester.id.eq(userId).or(chatRoom.performer.id.eq(userId)),
				cursor != null ? lastMessage.createdAt.lt(DateTimeFormatUtils.parseDateTime(cursor)) : null
			)
			.orderBy(lastMessage.createdAt.desc())
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
				cursor != null ? chatMessage.createdAt.lt(DateTimeFormatUtils.parseDateTime(cursor)) : null
			)
			.orderBy(chatMessage.createdAt.desc())
			.limit(size + 1)
			.fetch();

		boolean hasNext = allMessages.size() > size;
		List<ChatMessageDetailResponse> pageMessages = subLastPage(allMessages, hasNext);
		String nextCursor = getNextCursorByMessage(pageMessages, hasNext);

		ChatRoomDetailResponse detailResponse = new ChatRoomDetailResponse(chatRoomId, pageMessages);

		return new CursorPageResponse<>(Collections.singletonList(detailResponse), nextCursor, hasNext);
	}

	private String getNextCursor(List<ChatRoomSimpleResponse> chatrooms, boolean hasNext) {
		if (Objects.isNull(chatrooms) || chatrooms.isEmpty() || !hasNext) {
			return null;
		}
		return chatrooms.get(chatrooms.size() - 1).timestamp();
	}

	private String getNextCursorByMessage(List<ChatMessageDetailResponse> messages, boolean hasNext) {
		if (Objects.isNull(messages) || messages.isEmpty() || !hasNext) {
			return null;
		}
		return messages.get(messages.size() - 1).timeStamp();
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
