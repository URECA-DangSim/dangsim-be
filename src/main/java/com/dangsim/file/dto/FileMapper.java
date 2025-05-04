package com.dangsim.file.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dangsim.file.dto.request.UploadFilesRequestDto;
import com.dangsim.file.dto.response.UploadFilesResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileMapper {

	public static List<MultipartFile> toMultipartFiles(UploadFilesRequestDto uploadFilesRequestDto) {
		return uploadFilesRequestDto.files();
	}

	public static UploadFilesResponseDto toUploadFilesResponseDto(List<String> uploadedFileUrls) {
		return new UploadFilesResponseDto(uploadedFileUrls);
	}

}
