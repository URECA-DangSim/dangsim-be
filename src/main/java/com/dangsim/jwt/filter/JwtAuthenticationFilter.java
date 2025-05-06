package com.dangsim.jwt.filter;

import static com.dangsim.jwt.util.TokenUtil.*;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dangsim.jwt.JwtProvider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = extractToken(request.getHeader("Authorization"));
		if (token != null && jwtProvider.validateToken(token)) {
			Claims claims = jwtProvider.getClaims(token);
			Long userId = Long.valueOf(claims.getSubject());

			UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(userId, null, null);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(request, response);
	}
}
