package com.dangsim.common.interceptor;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.dangsim.common.exception.errorcode.InterceptorErrorCode;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.jwt.JwtProvider;
import com.dangsim.jwt.exception.JwtErrorCode;

@Component
public class StompChannelInterceptor implements ChannelInterceptor {

	private final JwtProvider jwtProvider;
	private final String PREFIX = "Bearer ";
	private final String HEADERNAME = "Authorization";

	public StompChannelInterceptor(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {

			String header = accessor.getFirstNativeHeader(HEADERNAME);

			if (header == null || !header.startsWith(PREFIX)) {
				throw new BaseException(InterceptorErrorCode.WRONG_HEADER);
			}

			String token = header.substring(PREFIX.length());

			if (!jwtProvider.validateToken(token)) {
				throw new BaseException(JwtErrorCode.INVALID_TOKEN);
			}

			Long userId = jwtProvider.getUserIdFromToken(token);
			Principal principal = () -> String.valueOf(userId);
			accessor.setUser(principal);
			accessor.setLeaveMutable(true);
		}
		return message;
	}

}
