package com.dangsim.file.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.dangsim.file.dto.request.UploadFilesRequestDto;
import com.dangsim.file.dto.response.UploadFilesResponseDto;
import com.dangsim.file.service.FileService;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@PostMapping(value = "/api/files/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadFilesResponseDto> uploadFromProfile(
		@RequestPart("files") UploadFilesRequestDto request,
		@AuthenticationPrincipal User user) {
		UploadFilesResponseDto response = fileService.uploadFromTask(request, user);
		return ResponseEntity.ok(response);
	}
}
