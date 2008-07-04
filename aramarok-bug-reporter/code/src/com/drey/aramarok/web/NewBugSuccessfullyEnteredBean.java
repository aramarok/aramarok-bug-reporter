package com.drey.aramarok.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.web.util.WebUtil;

public class NewBugSuccessfullyEnteredBean {
	
	private Logger log = Logger.getLogger(NewBugSuccessfullyEnteredBean.class);
	
	private Long newBugEnteredId = null;
	
	public NewBugSuccessfullyEnteredBean(){
		HttpSession session = WebUtil.getHttpSession();
		Object o = session.getAttribute(WebUtil.NEW_INSERED_BUG_ID);
		if (o!=null && o instanceof Long){
			this.newBugEnteredId = (Long)o;
		} else {
			log.error("Bug ID from session could not be retrieved!");
		}
	}

	public Long getNewBugEnteredId() {
		return newBugEnteredId;
	}

	public void setNewBugEnteredId(Long newBugEnteredId) {
		this.newBugEnteredId = newBugEnteredId;
	}
	
	public String viewBug(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.BUG_ID_SELECTED_FOR_VIEWING, String.valueOf(newBugEnteredId));
		return WebUtil.VIEW_BUG_OUTCOME;
	}
}