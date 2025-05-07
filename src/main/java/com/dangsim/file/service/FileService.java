package com.dangsim.file.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.file.dto.FileMapper;
import com.dangsim.file.dto.request.UploadFilesRequestDto;
import com.dangsim.file.dto.response.UploadFilesResponseDto;
import com.dangsim.file.exception.FileException;
import com.dangsim.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

	private final AmazonS3Client amazonS3Client;

	private static final Set<String> ALLOWED_FILE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");
	private static final String TASK_IMG_DIR = "task/";
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public UploadFilesResponseDto uploadFromTask(UploadFilesRequestDto uploadFilesRequestDto, User user) {
		List<MultipartFile> files = FileMapper.toMultipartFiles(uploadFilesRequestDto);

		if (Objects.isNull(files) || files.isEmpty()) {
			throw new BaseException(FileException.EMPTY_FILE);
		}

		return FileMapper.toUploadFilesResponseDto(this.uploadFiles(files, user, TASK_IMG_DIR));
	}

	private List<String> uploadFiles(List<MultipartFile> files, User user, String dir) {
		List<String> uploadedFileUrls = new ArrayList<>();

		for (MultipartFile file : files) {
			String fileExtension = extractFileExtension(file);

			try {
				uploadedFileUrls.add(this.uploadFilesToS3(file, user, dir, fileExtension));
			} catch (Exception e) {
				throw new BaseException(FileException.FAIL_FILE_UPLOAD);
			}
		}

		return uploadedFileUrls;
	}

	private String extractFileExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if (Objects.isNull(fileName) || fileName.isEmpty()) {
			throw new BaseException(FileException.EMPTY_FILE);
		}

		int lastDotIndex = fileName.lastIndexOf(".");

		if (lastDotIndex == -1) { // 확장자 미존재
			throw new BaseException(FileException.NO_FILE_EXTENSION);
		}

		String extension = fileName.substring(lastDotIndex).toLowerCase(); // 확장자 추출 (".jpg")
		this.validateFileExtension(extension);

		return extension;
	}

	private void validateFileExtension(String extension) {
		if (!ALLOWED_FILE_EXTENSIONS.contains(extension)) {
			throw new BaseException(FileException.INVALID_FILE_EXTENSION);
		}
	}

	private String uploadFilesToS3(MultipartFile file, User user, String dir, String extension) throws IOException {
		String fileName = createFileName(dir, user, extension);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		amazonS3Client.putObject(
			new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
		);

		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	private String createFileName(String dir, User user, String extension) {
		StringBuilder fileName = new StringBuilder();

		fileName.append(dir)
			.append(user.getId())
			.append("/")
			.append(UUID.randomUUID())
			.append("/")
			.append(extension);

		return fileName.toString();
	}

}
