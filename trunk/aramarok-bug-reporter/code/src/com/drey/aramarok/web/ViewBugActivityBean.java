package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugHistory;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class ViewBugActivityBean {
	
	private Logger log = Logger.getLogger(ViewBugActivityBean.class);
	
	private Bug bug = null;
	
	private List<BugActivityWrapperBean> bugActivityList = new ArrayList<BugActivityWrapperBean>();
	private String bugId = "";
	private boolean bugOk = false;
	
	public ViewBugActivityBean(){
		if (getBugForViewingFormSession() == 0){
			bugId = String.valueOf(bug.getId());
			bugOk = true;
		}
		createBugActivityList();
		sortList();
	}
	
	public String viewBug(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.BUG_ID_SELECTED_FOR_VIEWING, String.valueOf(bug.getId()));
		return WebUtil.VIEW_BUG_OUTCOME;
	}
	
	private void createBugActivityList(){
		if (bugOk){
			if (bug.getBugHistory()!=null && !bug.getBugHistory().isEmpty()){
				for (BugHistory bh: bug.getBugHistory()){
					BugActivityWrapperBean bawb = new BugActivityWrapperBean(bug, bh);
					bugActivityList.add(bawb);
				}
			}
		}
	}
	
	private void sortList(){
		Collections.sort(bugActivityList, new ActivityBugListComparator());
	}
	
	private int getBugForViewingFormSession(){
		/* 0 - bug id from session was not null; bug id found in the database 
		 * 1 - bug id from session was not null; bug id was not found in the database
		 * 2 - bug id from session was null
		 * 3 - bug id from session was invalid
		 */
		
		HttpSession session = WebUtil.getHttpSession();
		Object o = session.getAttribute(WebUtil.BUG_ID_SELECTED_FOR_VIEWING);
		if (o!=null && o instanceof String){
			String bugId = (String)o;
			Long bugIdL = null;
			try {
				bugIdL = Long.parseLong(bugId);
				
				DomainFacade facade = WebUtil.getDomainFacade();
				try {
					bug = facade.getBug(bugIdL);
				} catch (ExternalSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bug == null){
					
					return 1;
				}
				return 0;
				
			} catch (NumberFormatException nfe){
				return 3;
			} catch (IllegalArgumentException iar){
				return 3;
			}
		} else {
			log.error("Bug from session could not be retrieved!");
			return 2;
		}
	}

	public boolean isBugOk() {
		return bugOk;
	}

	public void setBugOk(boolean bugOk) {
		this.bugOk = bugOk;
	}

	public List<BugActivityWrapperBean> getBugActivityList() {
		return bugActivityList;
	}

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}
}

class ActivityBugListComparator implements Comparator<BugActivityWrapperBean> {
	public int compare(BugActivityWrapperBean item1, BugActivityWrapperBean item2) {
		if (item1 instanceof BugActivityWrapperBean && item2 instanceof BugActivityWrapperBean) {
			if (((BugActivityWrapperBean) item1).getActionDateAsLong() < ((BugActivityWrapperBean) item2).getActionDateAsLong()){
				return -1;
			} else if (((BugActivityWrapperBean) item1).getActionDateAsLong() == ((BugActivityWrapperBean) item2).getActionDateAsLong()){
				return 0;
			} else if (((BugActivityWrapperBean) item1).getActionDateAsLong() > ((BugActivityWrapperBean) item2).getActionDateAsLong()){
				return 1;
			}
		}
		return 0;
	}
}