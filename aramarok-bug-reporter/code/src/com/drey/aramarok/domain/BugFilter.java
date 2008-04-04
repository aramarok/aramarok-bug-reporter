package com.drey.aramarok.domain;

import java.util.List;


public class BugFilter {
	private static final long serialVersionUID = 0;
	
	private List<Long> bugIdList = null;
	private List<BugGeneralStatus> bugStatusList = null;
	private List<User> ownerList = null;
	private List<User> userAssignedToList = null;
	private List<Priority> priorityList = null;
	private List<Severity> severityList = null;
	private List<OperatingSystem> operatingSystemList = null;
	private List<Platform> platformList = null;
	private BugSortingMode sortingMode = BugSortingMode.ID_ASC;
	
	
	
	public List<Long> getBugIdList() {
		return bugIdList;
	}
	public void setBugIdList(List<Long> bugIdList) {
		this.bugIdList = bugIdList;
	}
	public List<BugGeneralStatus> getBugStatusList() {
		return bugStatusList;
	}
	public void setBugStatusList(List<BugGeneralStatus> bugStatusList) {
		this.bugStatusList = bugStatusList;
	}
	public List<User> getOwnerList() {
		return ownerList;
	}
	public void setOwnerList(List<User> ownerList) {
		this.ownerList = ownerList;
	}
	public List<User> getUserAssignedToList() {
		return userAssignedToList;
	}
	public void setUserAssignedToList(List<User> userAssignedToList) {
		this.userAssignedToList = userAssignedToList;
	}
	public List<Priority> getPriorityList() {
		return priorityList;
	}
	public void setPriorityList(List<Priority> priorityList) {
		this.priorityList = priorityList;
	}
	public List<Severity> getSeverityList() {
		return severityList;
	}
	public void setSeverityList(List<Severity> severityList) {
		this.severityList = severityList;
	}
	public List<OperatingSystem> getOperatingSystemList() {
		return operatingSystemList;
	}
	public void setOperatingSystemList(List<OperatingSystem> operatingSystemList) {
		this.operatingSystemList = operatingSystemList;
	}
	public List<Platform> getPlatformList() {
		return platformList;
	}
	public void setPlatformList(List<Platform> platformList) {
		this.platformList = platformList;
	}
	public BugSortingMode getSortingMode() {
		return sortingMode;
	}
	public void setSortingMode(BugSortingMode sortingMode) {
		this.sortingMode = sortingMode;
	}
}
