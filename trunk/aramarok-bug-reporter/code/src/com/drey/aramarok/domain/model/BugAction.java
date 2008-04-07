package com.drey.aramarok.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tolnai.Andrei
 */

public enum BugAction {
	CREATE_NEW_BUG,
	CHANGE_BUG_GENERAL_STATUS,
	ADD_USER_TO_CC,
	REMOVE_USER_FROM_CC,
	CHANGE_BUG_SUMMARY,
	CHANGE_BUG_OPERATING_SYSTEM,
	CHANGE_BUG_PLATFORM,
	CHANGE_BUG_PRIORITY,
	CHANGE_BUG_SEVERITY,
	ADD_COMMENT_TO_BUG;
	
	public static List<String> getBugActionAsList(){
		List<String> returnList = new ArrayList<String>();
		BugAction[] t = BugAction.values();
		for(int i=0;i<t.length;i++){
			returnList.add(t[i].name());
		}
		return returnList;
	}
	
	public static BugAction getBugActionByName(String s){
		if (s != null){
			BugAction[] t = BugAction.values();
			for(int i=0;i<t.length;i++){
				if (t[i].name().compareTo(s)==0){
					return t[i];
				}
			}
		}
		return null;
	}
}