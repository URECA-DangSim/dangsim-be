package com.dangsim.task.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequestDto(

	@NotBlank(message = "제목은 필수 입력 항목입니다.")
	@Size(min = 2, max = 30, message = "제목은 최소 2자 최대 30자 입니다.")
	String title,
	@NotBlank(message = "본문은 필수 입력 항목입니다.")
	@Size(min = 2, max = 300, message = "본문은 최소 2자 최대 300자 입니다.")
	String content,
	@NotBlank(message = "마감기한 설정은 필수 입력 항목입니다.")
	String deadline,
	@NotNull(message = "보상 액은 필수 입력 항목입니다.")
	@Min(100)
	@Max(1_000_000)
	Integer reward,
	@NotBlank(message = "주소는 필수 입력 항목입니다.")
	String address,
	@Size(max = 3, message = "이미지는 최대 3장입니다.")
	List<String> imageUrls
) {
}
