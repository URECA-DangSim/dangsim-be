package com.dangsim.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExtraInfoRequest(
	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(min = 2, max = 12, message = "닉네임은 2~12자 사이여야 합니다.")
	String nickname,

	@NotBlank(message = "주소는 필수입니다.")
	String address
) {
}
