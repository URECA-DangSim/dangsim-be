package com.dangsim.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dangsim.common.util.IdentifierUtils;

@SpringBootTest
public class IdentifierUtilsTest {

	@DisplayName("심부름 식별키와 현재 날짜가 포함된 40자 이하의 주문 번호가 생성된다.")
	@Test
	void generateMerchantUidContainTaskIdAndDate() {
		// given
		final String DATE_PATTERN = "yy.MM.dd HH:mm";
		final Long taskId = 15L;
		LocalDateTime now = LocalDateTime.now();

		// when
		String merchantUid = IdentifierUtils.generateMerchantUid(taskId, now);

		// then
		assertAll(
			() -> assertTrue(merchantUid.startsWith(taskId.toString())),
			() -> assertTrue(merchantUid.endsWith(now.format(DateTimeFormatter.ofPattern(DATE_PATTERN))))
		);
	}

}
