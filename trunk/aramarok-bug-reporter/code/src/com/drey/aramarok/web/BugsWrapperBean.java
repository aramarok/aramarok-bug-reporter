package com.drey.aramarok.web;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.web.util.WebUtil;

public class BugsWrapperBean {
private Bug bug = null;
	
	public BugsWrapperBean(Bug bug){
		this.bug = bug;
	}
	
	public String viewBug(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.BUG_ID_SELECTED_FOR_VIEWING, String.valueOf(bug.getId()));
		return WebUtil.VIEW_BUG_OUTCOME;
	}
	
	public Long getId(){
		return bug.getId();
	}
	
	public String getSummary(){
		return bug.getSummary();
	}
	
	public String getOpenDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
		return formatter.format(bug.getOpenDate());
	}
	
	public String getPriority(){
		return bug.getPriority().getName();
	}
	
	public String getPlatform(){
		return bug.getPlatform().getName();
	}
	
	public String getOperatingSystem(){
		return bug.getOperatingSystem().getName();
	}
	
	public String getSeverity(){
		return bug.getSeverity().getName();
	}
	
	public String getBugStatus(){
		return bug.getStatus().name();
	}
	
	public String getReporter(){
		return bug.getOwner().getUserName();
	}
	
	public String getAssignedTo(){
		return bug.getUserAssignedTo().getUserName();
	}
}
