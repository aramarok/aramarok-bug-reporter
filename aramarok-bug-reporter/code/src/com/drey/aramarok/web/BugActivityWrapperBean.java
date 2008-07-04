package com.drey.aramarok.web;

import java.text.SimpleDateFormat;

import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugAction;
import com.drey.aramarok.domain.model.BugHistory;
import com.drey.aramarok.web.util.WebUtil;
import com.drey.aramarok.web.util.WebUtilConstants;

public class BugActivityWrapperBean {
	
	private Bug bug = null;
	private BugHistory bugHistory = null;
	
	public BugActivityWrapperBean(Bug bug, BugHistory bugHistory){
		this.bug = bug;
		this.bugHistory = bugHistory;
	}
	
	public String getWho(){
		return bugHistory.getUser().getUserName();
	}
	
	public String getActionDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
		return formatter.format(bugHistory.getActionDate());
	}
	
	public long getActionDateAsLong(){
		return bugHistory.getActionDate().getTime();
	}
	
	public String getWhat(){
		String toReturn = "";
		if (bugHistory.getBugActions()!=null){
			int size = bugHistory.getBugActions().size();
			int i = 0;
			for (BugAction b: bugHistory.getBugActions()){
				i++;
				toReturn +=WebUtil.getResource(WebUtilConstants.ARAMAROK_PROPERTIES_FILE, b.name());
				if (i<size){//if we are not at the end, we add a comma. 
					toReturn += ", ";
				}
			}
		}
		return toReturn;
	}
	
	public String getBugStatus(){
		return bugHistory.getBugStatus().toString();
	}
}