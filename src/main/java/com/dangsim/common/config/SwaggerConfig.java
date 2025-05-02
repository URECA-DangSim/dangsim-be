package com.dangsim.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	servers = {
		// @Server(url = "", description = "배포 서버"),
		@Server(url = "http://localhost:8080", description = "로컬 서버")}
)
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("Authorization", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					//                    .scheme("bearer") // 토큰 앞에 Bearer 붙여주는 역할
					.in(SecurityScheme.In.HEADER)
					.bearerFormat("JWT")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList("Authorization"))
			.info(new Info()
				.title("강심")
				.version("1.0.0")
			);
	}
}
