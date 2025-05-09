package com.dangsim.jwt.filter;

import static com.dangsim.jwt.util.TokenUtil.*;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.jwt.JwtProvider;
import com.dangsim.user.entity.User;
import com.dangsim.user.exception.UserErrorCode;
import com.dangsim.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader(AUTH_HEADER);
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response); // 헤더 없으면 필터 통과만
			return;
		}

		String token = extractToken(request.getHeader(AUTH_HEADER));

		if (token != null && jwtProvider.validateToken(token)) {
			Claims claims = jwtProvider.getClaims(token);
			Long userId = Long.valueOf(claims.getSubject());

			// user 엔티티 조회
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

			// 권한 설정
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

			// 인증 객체 생성
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
				List.of(authority));

			//SecurityContext에 등록
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		filterChain.doFilter(request, response);
	}
}
