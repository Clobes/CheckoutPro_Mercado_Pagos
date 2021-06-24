package com.backend.mp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.backend.mp.converter.PreferenceConverter;

@Configuration
public class ConverterConfig {

	@Bean
	public PreferenceConverter getPreferenceConverter() {
		return new PreferenceConverter();
	}
}
