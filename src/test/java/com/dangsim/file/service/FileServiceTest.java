package com.dangsim.file.service;

import static org.assertj.core.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dangsim.common.exception.runtime.BaseException;
import com.dangsim.common.fixture.UserFixture;
import com.dangsim.file.dto.request.UploadFilesRequestDto;
import com.dangsim.file.dto.response.UploadFilesResponseDto;
import com.dangsim.file.exception.FileException;
import com.dangsim.user.entity.Role;
import com.dangsim.user.entity.User;

@SpringBootTest
public class FileServiceTest {

	@Autowired
	FileService fileService;

	@DisplayName("요청된 파일 리스트가 NULL이라면 예외가 발생한다.")
	@Test
	void throwEmptyFileExceptionWhenFilesIsNull() {
		// given
		UploadFilesRequestDto requestDto = new UploadFilesRequestDto(null);
		User user = UserFixture.user(Role.ADMIN, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		// when		// then
		assertThatThrownBy(() -> fileService.uploadFromTask(requestDto, user))
			.isInstanceOf(BaseException.class)
			.hasMessage(FileException.EMPTY_FILE.getMessage());
	}

	@DisplayName("요청된 파일 리스트가 비어있다면 예외가 발생한다.")
	@Test
	void throwEmptyFileExceptionWhenFilesIsEmpty() {
		// given
		UploadFilesRequestDto requestDto = new UploadFilesRequestDto(Collections.emptyList());
		User user = UserFixture.user(Role.ADMIN, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 1L);

		// when		// then
		assertThatThrownBy(() -> fileService.uploadFromTask(requestDto, user))
			.isInstanceOf(BaseException.class)
			.hasMessage(FileException.EMPTY_FILE.getMessage());
	}

	@Disabled
	@DisplayName("1개의 파일을 S3에 업로드하고, 저장된 경로를 획득한다.")
	@Test
	void uploadFile() throws IOException {
		// given
		String fileName = "test-file";
		String contentType = "png";
		String filePath = "src/test/resources/files/칠가이.png";
		MockMultipartFile file1 = getMockMultipartFile(fileName, contentType, filePath);
		UploadFilesRequestDto requestDto = new UploadFilesRequestDto(List.of(file1));

		User user = UserFixture.user(Role.ADMIN, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 8135L);

		// when
		UploadFilesResponseDto response = fileService.uploadFromTask(requestDto, user);

		// then
		assertThat(response.uploadedFileUrls())
			.hasSize(1)
			.allMatch(url -> url.contains("task/8135"));
	}

	@Disabled
	@DisplayName("3개의 파일을 S3에 업로드하고, 저장된 경로를 획득한다.")
	@Test
	void uploadFiles() throws IOException {
		// given
		List<MultipartFile> files = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			String fileName = "test-file" + i;
			String contentType = "png";
			String filePath = "src/test/resources/files/칠가이.png";
			files.add(getMockMultipartFile(fileName, contentType, filePath));
		}
		UploadFilesRequestDto requestDto = new UploadFilesRequestDto(files);

		User user = UserFixture.user(Role.ADMIN, BigDecimal.ONE);
		ReflectionTestUtils.setField(user, "id", 8135L);

		// when
		UploadFilesResponseDto response = fileService.uploadFromTask(requestDto, user);

		// then
		assertThat(response.uploadedFileUrls())
			.hasSize(3)
			.allMatch(url -> url.contains("task/8135"));
	}

	private MockMultipartFile getMockMultipartFile(
		String fileName, String contentType, String path) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(path);
		return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
	}
}
