package com.drey.aramarok.domain.model;

import java.util.ArrayList;
import java.util.List;

public enum BugGeneralStatus {
	NEW,
	
	ASSIGNED,
	
	FIXED,
	INVALID,
	WONTFIX,
	LATER,
	WORKSFORME,
	
	REOPENED,
	
	CLOSED;
	
	public static List<String> getBugGeneralStatusAsList(){
		List<String> returnList = new ArrayList<String>();
		BugGeneralStatus[] t = BugGeneralStatus.values();
		for(int i=0;i<t.length;i++){
			returnList.add(t[i].name());
		}
		return returnList;
	}
	
	public static BugGeneralStatus getBugGeneralStatusByName(String s){
		if (s != null){
			BugGeneralStatus[] t = BugGeneralStatus.values();
			for(int i=0;i<t.length;i++){
				if (t[i].name().compareTo(s)==0){
					return t[i];
				}
			}
		}
		return null;
	}
}