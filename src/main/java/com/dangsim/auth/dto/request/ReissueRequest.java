package com.dangsim.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReissueRequest(
	@NotNull(message = "userId는 필수입니다.")
	Long userId,

	@NotBlank(message = "refreshToken은 공백일 수 없습니다.")
	String refreshToken
) {
}
