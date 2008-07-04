package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class ViewSavedFiltersBean {
	private Logger log = Logger.getLogger(ViewSavedFiltersBean.class);
	
	private SavedSearch savedSearch = null;
	private boolean filteredBugListEmpty = true;
	private ArrayList<BugsWrapperBean> filteredBugs = new ArrayList<BugsWrapperBean>();
	
	
	public ViewSavedFiltersBean(){
		readFromSession();
		filterBugs();
	}
	
	private void readFromSession(){
		if (getBugForViewingFormSession()==0){
			filteredBugListEmpty = false;
		} else {
			filteredBugListEmpty = true;
		}
	}
	
	
	private void filterBugs(){
		BugFilter bugFilter = buildFilter();
		
		DomainFacade facade = WebUtil.getDomainFacade();
		List<Bug> returnedBugs;
		try {
			returnedBugs = facade.getBugs(bugFilter);

			ArrayList<BugsWrapperBean> wb = new ArrayList<BugsWrapperBean>();
			for (Bug b : returnedBugs){
				wb.add(new BugsWrapperBean(b));
			}
			filteredBugs = wb;
			if (wb.isEmpty())
				filteredBugListEmpty = true;
			else 
				filteredBugListEmpty = false;
			
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	private BugFilter buildFilter(){
		BugFilter filter = new BugFilter();
		
		List<BugGeneralStatus> bgsList = new ArrayList<BugGeneralStatus>();
		if (savedSearch.getBugStatusList()!=null && !savedSearch.getBugStatusList().isEmpty()) {
			for (BugGeneralStatus bgs: savedSearch.getBugStatusList()){
				bgsList.add(bgs);
			}
			filter.setBugStatusList(bgsList);
		}
		if (savedSearch.getOperatingSystemList()!=null && !savedSearch.getOperatingSystemList().isEmpty()){
			filter.setOperatingSystemList(new ArrayList(savedSearch.getOperatingSystemList()));
		}
		if (savedSearch.getOwnerList()!=null && !savedSearch.getOwnerList().isEmpty()){
			filter.setOwnerList(new ArrayList(savedSearch.getOwnerList()));
		}
		if (savedSearch.getPlatformList()!=null && !savedSearch.getPlatformList().isEmpty()){
			filter.setPlatformList(new ArrayList(savedSearch.getPlatformList()));
		}
		if (savedSearch.getPriorityList()!=null && !savedSearch.getPriorityList().isEmpty()){
			filter.setPriorityList(new ArrayList(savedSearch.getPriorityList()));
		}
		if (savedSearch.getSeverityList()!=null && !savedSearch.getSeverityList().isEmpty()){
			filter.setSeverityList(new ArrayList(savedSearch.getSeverityList()));
		}
		if (savedSearch.getSortingMode()!=null){
			filter.setSortingMode(savedSearch.getSortingMode());
		}
		/*if (filter.getBugIdList()!=null)
			ss.setUserAssignedToList(new HashSet(filter.getBugIdList()));
		*/
		
		return filter;
	}

	private int getBugForViewingFormSession(){
		/* 0 - bug id from session was not null; bug id found in the database 
		 * 1 - bug id from session was not null; bug id was not found in the database
		 * 2 - bug id from session was null
		 * 3 - bug id from session was invalid
		*/
		
		HttpSession session = WebUtil.getHttpSession();
		Object o = session.getAttribute(WebUtil.SAVED_FILTER_ID_SELECTED_FOR_VIEWING);
		if (o!=null && o instanceof String){
			String bugId = (String)o;
			Long bugIdL = null;
			try {
				bugIdL = Long.parseLong(bugId);
				
				DomainFacade facade = WebUtil.getDomainFacade();
				try {
					savedSearch = facade.getSavedSearch(bugIdL);
				} catch (ExternalSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (savedSearch == null){
					return 1;
				}
				return 0;
				
			} catch (NumberFormatException nfe){
				return 3;
			} catch (IllegalArgumentException iar){
				return 3;
			}
		} else {
			log.error("Saved search from session could not be retrieved!");
			return 2;
		}
	}
	
	public boolean isFilteredBugListEmpty() {
		return filteredBugListEmpty;
	}

	public void setFilteredBugListEmpty(boolean filteredBugListEmpty) {
		this.filteredBugListEmpty = filteredBugListEmpty;
	}

	public ArrayList<BugsWrapperBean> getFilteredBugs() {
		return filteredBugs;
	}

	public void setFilteredBugs(ArrayList<BugsWrapperBean> filteredBugs) {
		this.filteredBugs = filteredBugs;
	}
}