package com.drey.aramarok.domain.model;

public enum UserStatus {
	ACTIVE,
	DISABLED;
	
	public static UserStatus getUserStatusByName(String s) {
		if (s != null && !s.isEmpty()){
			UserStatus[] status = UserStatus.values();
			for(int i=0;i<status.length;i++){
				if (status[i].name().compareTo(s)==0){
					return status[i];
				}
			}
		}
		return null;
	}
}