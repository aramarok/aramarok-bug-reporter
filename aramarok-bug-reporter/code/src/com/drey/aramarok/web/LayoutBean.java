package com.drey.aramarok.web;

import javax.servlet.http.HttpSession;

import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.web.util.WebUtil;


/**
 * 
 * @author Tolnai Andrei
 *
 */

public class LayoutBean {
	
	private String bugId = null;
	
	public boolean isLoggedIn(){
		return (WebUtil.getUser()!=null);
	}
	
	public String getUserName() {
		User user = WebUtil.getUser();
		if (user != null) {
			return user.getName();
		}
		return "";
	}
	
	public String goToBug(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.BUG_ID_SELECTED_FOR_VIEWING, bugId);
		return WebUtil.VIEW_BUG_OUTCOME;
	}

	public String helpPage(){
		return WebUtil.HELP_PAGE_OUTCOME;
	}
	
	// Getters and Setters
	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}
}