package com.backend.mp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NotificationWebHooksDTO {
		
	private Long id;
	
	private boolean live_mode;
	
	private String type;
	
	private String date_created;
	
	private Long application_id;
	
	private Long user_id;
	
	private int version;
	
	private String api_version;
	
	private String action;
	
	private DataMpDTO data;

	@Override
	public String toString() {
		return "{id:" + id + ", live_mode:" + live_mode + ", type:\"" + type + "\", date_created:\""
				+ date_created + "\", application_id:" + application_id + ", user_id:" + user_id + ", version:" + version
				+ ", api_version:\"" + api_version + "\", action:\"" + action + "\", data:" + data + "}";
	}
	
	


}
