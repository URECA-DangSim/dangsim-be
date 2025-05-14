package com.dangsim.file.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dangsim.file.dto.response.UploadFilesResponseDto;
import com.dangsim.file.service.FileService;
import com.dangsim.user.entity.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping(value = "/api/files/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadFilesResponseDto> uploadFromProfile(
		@RequestPart("files") @NotNull @Size(min = 1, max = 3) List<MultipartFile> files,
		@AuthenticationPrincipal User user) {
		UploadFilesResponseDto response = fileService.uploadFromTask(files, user);
		return ResponseEntity.ok(response);
	}
}
