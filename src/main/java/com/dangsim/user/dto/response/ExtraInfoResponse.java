package com.dangsim.user.dto.response;

public record ExtraInfoResponse(boolean result) {
	public static ExtraInfoResponse of(boolean result) {
		return new ExtraInfoResponse(result);
	}
}
