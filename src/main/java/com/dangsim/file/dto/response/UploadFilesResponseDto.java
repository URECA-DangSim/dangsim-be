package com.dangsim.file.dto.response;

import java.util.List;

public record UploadFilesResponseDto(
	List<String> uploadedFileUrls
) {
}
