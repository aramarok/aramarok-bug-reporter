package com.drey.aramarok.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name = "SAVED_SEARCHES")
@NamedQueries( {
		@NamedQuery(name = "SavedSearch.findSavedSearchByName", query = "SELECT s from SavedSearch as s WHERE s.name = :name"),
		@NamedQuery(name = "SavedSearch.findSavedSearchByDate", query = "SELECT s from SavedSearch as s WHERE s.savedDate = :savedDate")
		} )
		
public class SavedSearch implements Serializable {
	
	@Id
	@GeneratedValue
	@Column(name = "SAVED_SEARCH_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;
	
	@Column(name = "NAME")
	private String name = "";
	
	//@Embedded
	//private BugFilter bugFilter = null;
	
	@Column(name = "SAVED_DATE")
	private Date savedDate;
	
	@Column(name = "REMOVED")
	private boolean removed = false;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_BUG",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="bug_key", referencedColumnName="BUG_ID", unique=false)}
				)
	private Set<Bug> bugIdList = null;

	@Column(name ="SAVED_SEARCHES_BUG_STATUS", length = 1255)
	private EnumSet<BugGeneralStatus> bugStatusList;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_OWNER",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="OWNER_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> ownerList = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_USER_ASSIGNED_TO",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="user_assigned_to_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> userAssignedToList = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_USER_PRIORITY",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="priority_key", referencedColumnName="PRIORITY_ID", unique=false)}
				)
	private Set<Priority> priorityList = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_USER_SEVERITY",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="severity_key", referencedColumnName="SEVERITY_ID", unique=false)}
				)
	private Set<Severity> severityList = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_USER_OS",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="os_key", referencedColumnName="OS_ID", unique=false)}
				)
	private Set<OperatingSystem> operatingSystemList = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="SAVED_SEARCHES_PLATFORM",
				joinColumns = {@JoinColumn(name="saved_search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="platform_key", referencedColumnName="PLATFORM_ID", unique=false)}
				)
	private Set<Platform> platformList = null;
	
	@Column(name = "SORTING_MODE", nullable=false)
	private BugSortingMode sortingMode = null;
	
	public SavedSearch(){
		
	}
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(Date savedDate) {
		this.savedDate = savedDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public EnumSet<BugGeneralStatus> getBugStatusList() {
		return bugStatusList;
	}

	public void setBugStatusList(EnumSet<BugGeneralStatus> bugStatusList) {
		this.bugStatusList = bugStatusList;
	}

	public Set<User> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(Set<User> ownerList) {
		this.ownerList = ownerList;
	}

	public Set<User> getUserAssignedToList() {
		return userAssignedToList;
	}

	public void setUserAssignedToList(Set<User> userAssignedToList) {
		this.userAssignedToList = userAssignedToList;
	}

	public Set<Priority> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(Set<Priority> priorityList) {
		this.priorityList = priorityList;
	}

	public Set<Severity> getSeverityList() {
		return severityList;
	}

	public void setSeverityList(Set<Severity> severityList) {
		this.severityList = severityList;
	}

	public Set<OperatingSystem> getOperatingSystemList() {
		return operatingSystemList;
	}

	public void setOperatingSystemList(Set<OperatingSystem> operatingSystemList) {
		this.operatingSystemList = operatingSystemList;
	}

	public Set<Platform> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(Set<Platform> platformList) {
		this.platformList = platformList;
	}

	public BugSortingMode getSortingMode() {
		return sortingMode;
	}

	public void setSortingMode(BugSortingMode sortingMode) {
		this.sortingMode = sortingMode;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}