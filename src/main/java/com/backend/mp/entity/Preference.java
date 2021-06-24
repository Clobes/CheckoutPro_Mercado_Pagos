package com.backend.mp.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Preference {

	private Long collector_id;
	private List<Item> items;
	private String id;
	private String init_point;
	
	
	public void addItem(Item item) {
		items = getItems();
		items.add(item);
	} 
	
	public List<Item> getItems(){
		if(items==null) {
			items = new ArrayList<Item>();
		}
		return items;
	}
	
	@Override
	public String toString() {
		return "Preference {collector_id=" + collector_id + ", items=" + items + ", id=" + id + ", init_point="
				+ init_point + "}";
	}
	
	
}
