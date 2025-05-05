package com.dangsim.common.util;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class IdentifierUtils {

	private static final String DATE_PATTERN = "yyMMdd";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

	public static String generateMerchantUid(Long taskId, LocalDateTime now) {
		StringBuilder sb = new StringBuilder();

		String uuidLen20 = UUID.randomUUID()
			.toString()
			.replace("-", "")
			.substring(0, 20);

		String datePart = now.format(formatter);

		sb.append(taskId)
			.append("-")
			.append(uuidLen20)
			.append("-")
			.append(datePart);

		return sb.toString();
	}
}
