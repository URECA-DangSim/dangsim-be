package com.dangsim.chat.repository;

import static com.dangsim.chat.entity.QChatMessage.*;
import static com.dangsim.chat.entity.QChatRoom.*;
import static com.dangsim.user.entity.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dangsim.chat.dto.response.ChatRoomSimpleResponse;
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
			.select(new QChatRoomSimpleResponse(chatRoom, chatMessage, user))
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
				chatRoom.requester.id.eq(userId).and(partner.id.eq(chatRoom.requester.id))
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
		String nextCursor = hasNext ? chatRooms.get(size).timestamp() : null;

		return new CursorPageResponse<>(chatRooms.stream().limit(size).toList(), nextCursor, hasNext);
	}
}
