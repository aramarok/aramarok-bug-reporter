package com.drey.aramarok.web;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.search.SearchException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class SavedFiltersWrapperBean {

	private Logger log = Logger.getLogger(SavedFiltersWrapperBean.class);
	
	private SavedSearch ss = null;
	
	public SavedFiltersWrapperBean(SavedSearch ss){
		this.ss = ss;
	}
	
	public String getName(){
		return ss.getName();
	}
	
	public String deleteFilter(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			facade.removeASavedSearch(ss);
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		} catch (UserException e) {
			log.error("UserException");
		} catch (SearchException e) {
			log.error("SearchException");
		}
		return WebUtil.SAVED_FILTERS_OUTCOME;
	}
	
	public String viewFilter(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.SAVED_FILTER_ID_SELECTED_FOR_VIEWING, String.valueOf(ss.getId()));
		return WebUtil.VIEW_SAVED_FILTER_OUTCOME;
	}
}