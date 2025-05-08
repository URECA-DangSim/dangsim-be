package com.dangsim.common.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;

public class TimeConfig {
	@Bean
	public Clock clock() {
		return Clock.systemUTC();
	}
}
