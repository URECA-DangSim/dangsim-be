package com.dangsim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
				// .anyRequest().permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/users/user/extra-info").hasRole("TMP_USER")
				.requestMatchers(HttpMethod.PUT, "/api/users/user/extra-info").authenticated()
				.anyRequest().authenticated()
			).addFilterBefore(
				new JwtAuthenticationFilter(jwtProvider, userRepository),
				UsernamePasswordAuthenticationFilter.class
			);
		return http.build();
	}
}
