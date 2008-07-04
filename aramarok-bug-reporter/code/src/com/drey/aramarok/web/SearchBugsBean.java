package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.search.SearchException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class SearchBugsBean {
	private Logger log = Logger.getLogger(SearchBugsBean.class);
	
	private List<SelectItem> bugStatuses = new ArrayList<SelectItem>(0);	
	private List<String> selectedBugStatuses = new ArrayList<String>(0);
	private List<BugGeneralStatus> bugGeneralStatuses = new ArrayList<BugGeneralStatus>();
	
	private List<User> assignedToList = null;
	private String assignedToSelected = null;
	private LinkedList<SelectItem> assignedToList_out = new LinkedList<SelectItem>();
	
	private List<User> reportedByList = null;
	private String reportedBySelected = null;
	private LinkedList<SelectItem> reportedByList_out = new LinkedList<SelectItem>();
	
	private List<SelectItem> priorities = new ArrayList<SelectItem>(0);	
	private List<String> selectedPriorities = new ArrayList<String>(0);
	
	private List<SelectItem> severities = new ArrayList<SelectItem>(0);	
	private List<String> selectedSeverities = new ArrayList<String>(0);
	
	private List<OperatingSystem> operatingSystemsList = new ArrayList<OperatingSystem>();
	private String operatingSystemSelected = null;
	private LinkedList<SelectItem> operatingSystemsList_out = new LinkedList<SelectItem>();
	
	private List<SelectItem> platforms = new ArrayList<SelectItem>(0);	
	private List<String> selectedPlatforms = new ArrayList<String>(0);
	
	private boolean filteredBugListEmpty = true;
	private ArrayList<BugsWrapperBean> filteredBugs = new ArrayList<BugsWrapperBean>();
	
	private boolean filterNameEmpty = false;
	private String filterName = "";
		
	private boolean nameOfTheSearchAlreadyExists = false;
	
	public SearchBugsBean(){
		log.info("Search bug page");
		initializeBugStatuses();
		initializeUsersLists();
		initializePriorityList();
		initializeSeverityList();
		initializeOperatingSystemList();
		initializePlatformList();
	}
	
	public String getLoadData(){
		
		return null;
	}
	
	public String saveSearchBugFilter(){
		filterNameEmpty = false;
		nameOfTheSearchAlreadyExists = false;
		if (!(filterName.trim().compareTo("") == 0)){ 
			SavedSearch ss = new SavedSearch();
			ss.setSavedDate(new Date());
			ss.setName(filterName);
			
			BugFilter filter = buildFilter();
			
			if (filter==null){
				filter = new BugFilter();
			}
			EnumSet<BugGeneralStatus> bugGeneralStatus = EnumSet.noneOf(BugGeneralStatus.class);
			if (filter.getBugStatusList()!=null){
				for (BugGeneralStatus bgs : filter.getBugStatusList()){
					bugGeneralStatus.add(bgs);
				}
			}
			ss.setBugStatusList(bugGeneralStatus);
			//ss.setBugIdList(new HashSet(filter.getBugIdList()));
			if (filter.getOperatingSystemList()!=null)
				ss.setOperatingSystemList(new HashSet(filter.getOperatingSystemList()));
			if (filter.getOwnerList()!=null)
				ss.setOwnerList(new HashSet(filter.getOwnerList()));
			if (filter.getPlatformList()!=null)
				ss.setPlatformList(new HashSet(filter.getPlatformList()));
			if (filter.getPriorityList()!=null)
				ss.setPriorityList(new HashSet(filter.getPriorityList()));
			if (filter.getSeverityList()!=null)
				ss.setSeverityList(new HashSet(filter.getSeverityList()));
			if (filter.getSortingMode()!=null)
				ss.setSortingMode(filter.getSortingMode());
			/*if (filter.getBugIdList()!=null)
				ss.setUserAssignedToList(new HashSet(filter.getBugIdList()));
			*/
			DomainFacade facade = WebUtil.getDomainFacade();
			try {
				facade.addASavedBugFilterToUser(WebUtil.getUser().getId(), ss);
			} catch (SearchException se){
				log.error("SearchException");
				nameOfTheSearchAlreadyExists = true;
			} catch (UserNotFoundException e) {
				log.error("UserNotFoundException");
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException");
			}
		} else {
			filterNameEmpty = true;
			log.error("Filter name is invalid!");
		}
		return null;
	}
	
	private BugFilter buildFilter(){
		DomainFacade facade = WebUtil.getDomainFacade();
		
		boolean statusNull = false;
		boolean assignedToNull = false;
		boolean reportedByNull = false;
		boolean selectedPrioritiesNull = false;
		boolean selectedSeveritiesNull = false;
		boolean selectedOSsNull = false;
		boolean selectedPlatformsNull = false;
		
		BugFilter bugFilter = new BugFilter();
		if (!selectedBugStatuses.isEmpty()){
			List<BugGeneralStatus> bStatus = new ArrayList<BugGeneralStatus>();
			for (String s: selectedBugStatuses){
				if (s.compareTo(BugGeneralStatus.ASSIGNED.name()) == 0)
					bStatus.add(BugGeneralStatus.ASSIGNED);
				if (s.compareTo(BugGeneralStatus.FIXED.name()) == 0)
					bStatus.add(BugGeneralStatus.FIXED);
				if (s.compareTo(BugGeneralStatus.INVALID.name()) == 0)
					bStatus.add(BugGeneralStatus.INVALID);
				if (s.compareTo(BugGeneralStatus.LATER.name()) == 0)
					bStatus.add(BugGeneralStatus.LATER);
				if (s.compareTo(BugGeneralStatus.NEW.name()) == 0)
					bStatus.add(BugGeneralStatus.NEW);
				if (s.compareTo(BugGeneralStatus.REOPENED.name()) == 0)
					bStatus.add(BugGeneralStatus.REOPENED);
				if (s.compareTo(BugGeneralStatus.WONTFIX.name()) == 0)
					bStatus.add(BugGeneralStatus.WONTFIX);
				if (s.compareTo(BugGeneralStatus.WORKSFORME.name()) == 0)
					bStatus.add(BugGeneralStatus.WORKSFORME);
			}
			bugFilter.setBugStatusList(bStatus);
		} else {
			statusNull = true;
		}
		
		if (assignedToSelected!=null){
			if (assignedToSelected.compareTo("") != 0){
				List<User> assignedTo = new ArrayList<User>();
				try {
					assignedTo.add(facade.getUser(assignedToSelected, false));
					bugFilter.setUserAssignedToList(assignedTo);
				} catch (ExternalSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				assignedToNull = true;
			}
		} else {
			assignedToNull = true;
		}
		if (reportedBySelected!=null){
			if (reportedBySelected.compareTo("") != 0){
				List<User> reportedBy = new ArrayList<User>();
				try {
					reportedBy.add(facade.getUser(reportedBySelected, false));
					bugFilter.setOwnerList(reportedBy);
				} catch (ExternalSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				reportedByNull = true;
			}
		} else {
			reportedByNull = true;
		}
		if (!selectedPriorities.isEmpty()){
			List<Priority> priorityList = new ArrayList<Priority>();
			for (String p :selectedPriorities){
				Priority pri = facade.getPriority(p);
				if (pri != null)
					priorityList.add(pri);
			}
			bugFilter.setPriorityList(priorityList);
		} else {
			selectedPrioritiesNull = true;
		}
		if (!selectedSeverities.isEmpty()){
			List<Severity> severityList = new ArrayList<Severity>();
			for (String s :selectedSeverities){
				Severity sev = facade.getSeverity(s);
				if (sev != null)
					severityList.add(sev);
			}
			bugFilter.setSeverityList(severityList);
		} else {
			selectedSeveritiesNull = true;
		}
		if (operatingSystemSelected!=null){
			if (operatingSystemSelected.compareTo("") != 0){
				List<OperatingSystem> operatingSystemList = new ArrayList<OperatingSystem>();
				operatingSystemList.add(facade.getOperatingSystem(operatingSystemSelected));
				bugFilter.setOperatingSystemList(operatingSystemList);
			} else {
				selectedOSsNull = true;
			}
		} else {
			selectedOSsNull = true;
		}
		if (!selectedPlatforms.isEmpty()){
			List<Platform> platformList = new ArrayList<Platform>();
			for (String p :selectedPlatforms){
				Platform plat = facade.getPlatform(p);
				if (plat != null)
					platformList.add(plat);
			}
			bugFilter.setPlatformList(platformList);
		} else {
			selectedPlatformsNull = true;
		}
		
		if (	statusNull && assignedToNull && 
				reportedByNull && selectedPrioritiesNull && 
				selectedSeveritiesNull && selectedOSsNull && 
				selectedPlatformsNull){
			bugFilter = null;
		}
		
		return bugFilter;
		
	}
	public String searchBugs(){
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
			
			return null;
		} catch (ExternalSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void initializeBugStatuses(){
		bugStatuses.add(new SelectItem(BugGeneralStatus.ASSIGNED.name(), BugGeneralStatus.ASSIGNED.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.FIXED.name(), BugGeneralStatus.FIXED.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.INVALID.name(), BugGeneralStatus.INVALID.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.LATER.name(), BugGeneralStatus.LATER.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.NEW.name(), BugGeneralStatus.NEW.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.REOPENED.name(), BugGeneralStatus.REOPENED.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.WONTFIX.name(), BugGeneralStatus.WONTFIX.name()));
		bugStatuses.add(new SelectItem(BugGeneralStatus.WORKSFORME.name(), BugGeneralStatus.WORKSFORME.name()));
		bugGeneralStatuses.add(BugGeneralStatus.ASSIGNED );
		bugGeneralStatuses.add(BugGeneralStatus.FIXED );
		bugGeneralStatuses.add(BugGeneralStatus.INVALID );
		bugGeneralStatuses.add(BugGeneralStatus.LATER );
		bugGeneralStatuses.add(BugGeneralStatus.NEW );
		bugGeneralStatuses.add(BugGeneralStatus.REOPENED );
		bugGeneralStatuses.add(BugGeneralStatus.WONTFIX );
		bugGeneralStatuses.add(BugGeneralStatus.WORKSFORME );
	}

	// initializes the users lists, both of them
	private void initializeUsersLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<User> users = facade.getAllUsers();
			assignedToList = users;
			reportedByList = users;
			
			assignedToList_out.add(new SelectItem("", ""));
			reportedByList_out.add(new SelectItem("", ""));
			
			for (User u : users){
				assignedToList_out.add(new SelectItem(u.getUserName(), u.getUserName()));
				reportedByList_out.add(new SelectItem(u.getUserName(), u.getUserName()));
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	private void initializePriorityList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<Priority> priorities = facade.getAllPriorities();
			for (Priority p : priorities){
				this.priorities.add(new SelectItem(p.getName(), p.getName()));
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	private void initializeSeverityList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<Severity> sev = facade.getAllSeverities();
			for (Severity s : sev){
				this.severities.add(new SelectItem(s.getName(), s.getName()));
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	private void initializeOperatingSystemList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<OperatingSystem> op = facade.getAllOperatingSystems();
			operatingSystemsList_out.add(new SelectItem("", ""));
			for (OperatingSystem s : op){
				this.operatingSystemsList.add(s);
				this.operatingSystemsList_out.add(new SelectItem(s.getName(), s.getName()));
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	private void initializePlatformList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<Platform> platforms = facade.getAllPlatforms();
			for (Platform p : platforms){
				this.platforms.add(new SelectItem(p.getName(), p.getName()));
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException");
		}
	}
	
	//Getters and Setters
	public List<String> getSelectedBugStatuses() {
		return selectedBugStatuses;
	}

	public void setSelectedBugStatuses(List<String> selectedBugStatuses) {
		this.selectedBugStatuses = selectedBugStatuses;
	}

	public List<SelectItem> getBugStatuses() {
		return bugStatuses;
	}

	public void setBugStatuses(List<SelectItem> bugStatuses) {
		this.bugStatuses = bugStatuses;
	}

	public LinkedList<SelectItem> getAssignedToList_out() {
		return assignedToList_out;
	}

	public void setAssignedToList_out(LinkedList<SelectItem> assignedToList_out) {
		this.assignedToList_out = assignedToList_out;
	}

	public LinkedList<SelectItem> getReportedByList_out() {
		return reportedByList_out;
	}

	public void setReportedByList_out(LinkedList<SelectItem> reportedByList_out) {
		this.reportedByList_out = reportedByList_out;
	}

	public List<SelectItem> getPriorities() {
		return priorities;
	}

	public void setPriorities(List<SelectItem> priorities) {
		this.priorities = priorities;
	}

	public List<String> getSelectedPriorities() {
		return selectedPriorities;
	}

	public void setSelectedPriorities(List<String> selectedPriorities) {
		this.selectedPriorities = selectedPriorities;
	}

	public List<SelectItem> getSeverities() {
		return severities;
	}

	public void setSeverities(List<SelectItem> severities) {
		this.severities = severities;
	}

	public List<String> getSelectedSeverities() {
		return selectedSeverities;
	}

	public void setSelectedSeverities(List<String> selectedSeverities) {
		this.selectedSeverities = selectedSeverities;
	}

	public List<SelectItem> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<SelectItem> platforms) {
		this.platforms = platforms;
	}

	public List<String> getSelectedPlatforms() {
		return selectedPlatforms;
	}

	public void setSelectedPlatforms(List<String> selectedPlatforms) {
		this.selectedPlatforms = selectedPlatforms;
	}

	public String getAssignedToSelected() {
		return assignedToSelected;
	}

	public void setAssignedToSelected(String assignedToSelected) {
		this.assignedToSelected = assignedToSelected;
	}

	public String getReportedBySelected() {
		return reportedBySelected;
	}

	public void setReportedBySelected(String reportedBySelected) {
		this.reportedBySelected = reportedBySelected;
	}

	public String getOperatingSystemSelected() {
		return operatingSystemSelected;
	}

	public void setOperatingSystemSelected(String operatingSystemSelected) {
		this.operatingSystemSelected = operatingSystemSelected;
	}

	public LinkedList<SelectItem> getOperatingSystemsList_out() {
		return operatingSystemsList_out;
	}

	public void setOperatingSystemsList_out(
			LinkedList<SelectItem> operatingSystemsList_out) {
		this.operatingSystemsList_out = operatingSystemsList_out;
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

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public boolean isFilterNameEmpty() {
		return filterNameEmpty;
	}

	public void setFilterNameEmpty(boolean filterNameEmpty) {
		this.filterNameEmpty = filterNameEmpty;
	}

	public boolean isNameOfTheSearchAlreadyExists() {
		return nameOfTheSearchAlreadyExists;
	}

	public void setNameOfTheSearchAlreadyExists(boolean nameOfTheSearchAlreadyExists) {
		this.nameOfTheSearchAlreadyExists = nameOfTheSearchAlreadyExists;
	}
}