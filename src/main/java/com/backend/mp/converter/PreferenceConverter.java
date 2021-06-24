package com.backend.mp.converter;

import com.backend.mp.dto.PreferenceDTO;
import com.backend.mp.entity.Preference;

public class PreferenceConverter extends AbstractConverter<Preference, PreferenceDTO>{

	@Override
	public PreferenceDTO fromEntity(Preference entity) {
		if(entity == null) return null;
		return PreferenceDTO.builder()
				.id(entity.getId())
				.collector_id(entity.getCollector_id())
				.init_point(entity.getInit_point())
				.items(entity.getItems())
				.build();
	}

	@Override
	public Preference fromDTO(PreferenceDTO dto) {
		if(dto == null) return null;
		return Preference.builder()
				.collector_id(dto.getCollector_id())
				.id(dto.getId())
				.init_point(dto.getInit_point())
				.build();
	}

}
