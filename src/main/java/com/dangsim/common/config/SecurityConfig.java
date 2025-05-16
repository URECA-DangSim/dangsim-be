package com.dangsim.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers(
				"/error", "/favicon.ico", "/ws/**"
			);
	}

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
				.requestMatchers("/ws-chat/**").permitAll()
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
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(
			"http://localhost:3000",
			"https://dangsim-fe.pages.dev",
      "/ws/**"
		));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
