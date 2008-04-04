package com.drey.aramarok.web;

import javax.servlet.http.HttpSession;

import com.drey.aramarok.domain.User;
import com.drey.aramarok.web.util.WebUtil;


/**
 * 
 * @author Tolnai Andrei
 *
 */

public class LayoutBean {
	
	private Long bugId = null;
	
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

	
	// Getters and Setters
	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}
}