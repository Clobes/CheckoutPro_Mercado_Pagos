package com.backend.mp.dto;

import java.util.List;

import com.backend.mp.entity.Item;

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
public class PreferenceDTO {
	
	private long collector_id;
	private String id;
	private String init_point;
	private List<Item> items;

}
