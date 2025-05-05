package com.dangsim.common.util;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
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
}
