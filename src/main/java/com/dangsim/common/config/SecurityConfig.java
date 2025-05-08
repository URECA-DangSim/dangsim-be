package com.dangsim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dangsim.jwt.JwtProvider;
import com.dangsim.jwt.filter.JwtAuthenticationFilter;
import com.dangsim.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtProvider jwtProvider,
		UserRepository userRepository) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> {
			})
			.formLogin(form -> form.disable())// CORS 기본 설정 활성화
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			).addFilterBefore(
				new JwtAuthenticationFilter(jwtProvider, userRepository),
				UsernamePasswordAuthenticationFilter.class
			);
		return http.build();
	}
}
