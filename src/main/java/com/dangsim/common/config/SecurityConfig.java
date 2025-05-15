package com.dangsim.common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.formLogin(form -> form.disable())// CORS 기본 설정 활성화
			.authorizeHttpRequests(auth -> auth
				// .anyRequest().permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/tasks").permitAll()
				.requestMatchers(
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/swagger-resources/**",
					"/webjars/**"
				).permitAll()
				.requestMatchers(HttpMethod.POST, "/api/users/user/extra-info").hasRole("TMP_USER")
				.requestMatchers(HttpMethod.PUT, "/api/users/user/extra-info").authenticated()
				.anyRequest().authenticated()
			).addFilterBefore(
				new JwtAuthenticationFilter(jwtProvider, userRepository),
				UsernamePasswordAuthenticationFilter.class
			);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(
			"http://localhost:3000",
			"https://dangsim-fe.pages.dev"
		));
		config.setAllowedMethods(Arrays.asList(
			"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
		));
		config.setAllowedHeaders(Arrays.asList(
			"Authorization", "Content-Type", "X-Requested-With", "Accept"
		));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 모든 경로에 대해 위 정책을 적용
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
