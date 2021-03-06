package com.backend.mp.converter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter<E, D> {
	
	public abstract D fromEntity(E entity);
	public abstract E fromDTO(D dto);

	public List<D> fromEntity(List<E> entities){
		if(entities == null) return null;
		return entities.stream()
			.map(e -> fromEntity(e))
			.collect(Collectors.toList());
	}
	
	public List<E> fromDTO(List<D> dtos){
		if(dtos == null) return null;
		return dtos.stream()
			.map(d -> fromDTO(d))
			.collect(Collectors.toList());
	}
}

