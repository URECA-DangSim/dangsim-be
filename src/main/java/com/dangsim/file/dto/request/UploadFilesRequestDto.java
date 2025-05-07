package com.dangsim.file.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UploadFilesRequestDto(
	@NotNull
	@Size(min = 1, max = 3)
	List<MultipartFile> files
) {
}
