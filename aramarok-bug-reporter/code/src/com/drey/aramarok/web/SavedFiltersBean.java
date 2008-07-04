package com.drey.aramarok.web;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class SavedFiltersBean {
	private Logger log = Logger.getLogger(SavedFiltersBean.class);
	
	private boolean savedFilterListEmpty = true;
	private ArrayList<SavedFiltersWrapperBean> savedFilters = new ArrayList<SavedFiltersWrapperBean>();
	
	public SavedFiltersBean(){
		loadSavedFiltersList();
	}
	
	private void loadSavedFiltersList(){
		savedFilters = new ArrayList<SavedFiltersWrapperBean>();
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			User user = facade.getUser(WebUtil.getUser().getUserName(), true);
			if (user.getSavedSearches()==null || user.getSavedSearches().isEmpty()){
				savedFilterListEmpty = true;
			} else {
				savedFilterListEmpty = false;
				for (SavedSearch ss : user.getSavedSearches()){
					if (!ss.isRemoved()){
						savedFilters.add(new SavedFiltersWrapperBean(ss));
					}
				}
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
		
	}

	public boolean isSavedFilterListEmpty() {
		return savedFilterListEmpty;
	}

	public void setSavedFilterListEmpty(boolean savedFilterListEmpty) {
		this.savedFilterListEmpty = savedFilterListEmpty;
	}

	public ArrayList<SavedFiltersWrapperBean> getSavedFilters() {
		return savedFilters;
	}

	public void setSavedFilters(ArrayList<SavedFiltersWrapperBean> savedFilters) {
		this.savedFilters = savedFilters;
	}
}