package com.backend.mp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataMpDTO {
	
	private String id;

	@Override
	public String toString() {
		return "{id:\"" + id + "\"}";
	}
	
	
}
