package com.dangsim.common.util;

import static lombok.AccessLevel.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.dangsim.common.exception.errorcode.UtilsErrorCode;
import com.dangsim.common.exception.runtime.BaseException;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class DateTimeFormatUtils {

	private static final String DATE_PATTERN = "yy.MM.dd HH:mm";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

	public static String formatDateTime(LocalDateTime now) {
		if (Objects.isNull(now)) {
			throw new BaseException(UtilsErrorCode.DATE_TIME_IS_NULL);
		}

		return now.format(formatter);
	}

	public static LocalDateTime parseDateTime(String deadline) {
		if (Objects.isNull(deadline) || deadline.isBlank()) {
			throw new BaseException(UtilsErrorCode.DATE_TIME_IS_NULL);
		}

		return LocalDateTime.parse(deadline, formatter);
	}

	public static LocalDateTime parseDateTimePG(String deadline) {
		if (Objects.isNull(deadline) || deadline.isBlank()) {
			throw new BaseException(UtilsErrorCode.DATE_TIME_IS_NULL);
		}

		try {
			// String 값이 숫자 형태일 때 (초 단위 timestamp)
			long epochSecond = Long.parseLong(deadline);
			return LocalDateTime.ofInstant(
					Instant.ofEpochSecond(epochSecond),
					ZoneId.systemDefault() // 시스템 기본 시간대 사용
			);
		} catch (NumberFormatException e) {
			// 숫자가 아니면 기존 포맷 (yy.MM.dd HH:mm)으로 파싱
			return LocalDateTime.parse(deadline, formatter);
		}
	}

}
