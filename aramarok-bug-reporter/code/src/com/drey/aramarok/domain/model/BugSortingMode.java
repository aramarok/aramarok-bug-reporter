package com.drey.aramarok.domain.model;

import java.util.ArrayList;
import java.util.List;

public enum BugSortingMode {
	ID_ASC, ID_DESC,
	OBSERVED_DATE_ASC, OBSERVED_DATE_DESC,
	OPEN_DATE_ASC, OPEN_DATE_DESC,
	SUMMARY_ASC, SUMMARY_DESC,
	PRIORITY_ASC, PRIORITY_DESC,
	OWNER_USER_NAME_ASC, OWNER_USER_NAME_DESC,
	STATUS_ASC, STATUS_DESC,
	PLATFORM_NAME_ASC, PLATFORM_NAME_DESC,
	OPERATING_SYSTEM_NAME_ASC, OPERATING_SYSTEM_NAME_DESC,
	SEVERITY_ASC, SEVERITY_DESC,
	VOTES_ASC, VOTES_DESC;
	
	public static List<String> getBugSortingModeAsList(){
		List<String> returnList = new ArrayList<String>();
		BugSortingMode[] t = BugSortingMode.values();
		for(int i=0;i<t.length;i++){
			returnList.add(t[i].name());
		}
		return returnList;
	}
	
	public static BugSortingMode getBugSortingModeByName(String s){
		if (s != null){
			BugSortingMode[] t = BugSortingMode.values();
			for(int i=0;i<t.length;i++){
				if (t[i].name().compareTo(s)==0){
					return t[i];
				}
			}
		}
		return null;
	}
}