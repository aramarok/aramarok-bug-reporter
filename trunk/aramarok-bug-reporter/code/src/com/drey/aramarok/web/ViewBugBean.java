package com.drey.aramarok.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.bug.BugStatusChangeException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.user.UserHasNoRightException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class ViewBugBean {
	private Logger log = Logger.getLogger(ViewBugBean.class);
	
	private Bug bug = null;
	
	private Long bugId = null; 
	private Date dateReported = null;
	private String reporterUserName = "";
	private String productName = "";
	private String componentName = "";
	private String versionName = "";
	private String operatingSystem = "";
	private String platform = "";
	private String priority = "";
	private String severity = "";
	private Date dateObserved = null;
	private String bugState = "";
	private String assignedTo = "";
	private String summary = "";
	private String description = "";
	private String additionalComment = "";
	private String ccUsers = "";
	private List<User> ccBugUsers = null;
	
	private List<OperatingSystem> operatingSystemList = null;
	private LinkedList<SelectItem> operatingSystemList_out = new LinkedList<SelectItem>();
	
	private List<Platform> platformList = null;
	private LinkedList<SelectItem> platformList_out = new LinkedList<SelectItem>();
	
	private List<Priority> priorityList = null;
	private LinkedList<SelectItem> priorityList_out = new LinkedList<SelectItem>();
	
	private List<Severity> severityList = null;
	private LinkedList<SelectItem> severityList_out = new LinkedList<SelectItem>();
	
	private List<BugGeneralStatus> bugGeneralStatusList = null;
	private LinkedList<SelectItem> bugGeneralStatusList_out = new LinkedList<SelectItem>();
	
	private ArrayList<CommentsWrapperBean> comments = new ArrayList<CommentsWrapperBean>();
	
	private boolean summaryNotEntered = false;
	private boolean observedDateNotEnterd = false;
	private boolean userHasNoRight = false;
	private boolean cannotChangeBugStatus = false;
	
	private boolean commentsListEmpty = false;
	
	private boolean bugIdInvalid = false;
	private boolean bugIdNullOrNotFound = false;
	private boolean bugFromSessionNull = false;
	private boolean bugNotFoundInTheDataBase = false;
	
	private boolean ccUsersNotValid = false;
	private String invalidUserNames = "";
	private boolean assignedUserSameAsCCUser = false;
	private String userNameAssignedSameAsCCUser = ""; 
	
	public ViewBugBean(){
		
	}
	
	public String viewBugActivity(){
		return WebUtil.VIEW_BUG_ACTIVITY_OUTCOME;
	}
	
	public String formatBugForPrinting(){
		return null;
	}
	
	public String commitBug(){
		userHasNoRight = false;
		cannotChangeBugStatus = false;
		
		if (isValidData()){
			
			int platformIndex = -1;
			for (SelectItem s : platformList_out){
				if (s.getLabel().compareTo(platform) == 0)
					platformIndex = platformList_out.indexOf(s);
			}
			int operatingSystemIndex = -1;
			for (SelectItem s : operatingSystemList_out){
				if (s.getLabel().compareTo(operatingSystem) == 0)
					operatingSystemIndex = operatingSystemList_out.indexOf(s);
			}
			int priorityIndex = -1;
			for (SelectItem s : priorityList_out){
				if (s.getLabel().compareTo(priority) == 0)
					priorityIndex = priorityList_out.indexOf(s);
			}
			int severityIndex = -1;
			for (SelectItem s : severityList_out){
				if (s.getLabel().compareTo(severity) == 0)
					severityIndex = severityList_out.indexOf(s);
			}
			int bugStatusIndex = -1;
			for (SelectItem s : bugGeneralStatusList_out){
				if (s.getLabel().compareTo(bugState) == 0)
					bugStatusIndex = bugGeneralStatusList_out.indexOf(s);
			}
			
			Set<Comment> c = new HashSet<Comment>();
			if (bug.getComments() != null)
				c.addAll(bug.getComments());
			if (additionalComment.trim().compareTo("") != 0){
				c.add(new Comment(WebUtil.getUser(), new Date(), additionalComment));
			}
			
			DomainFacade facade = WebUtil.getDomainFacade();
			Bug bugToCommit = new Bug(
				bug.getOwner(),
				dateObserved,
				null,//bug.getUserAssignedTo(),
				summary,
				bug.getDescription(),
				bugGeneralStatusList.get(bugStatusIndex),
				bug.getProduct(),
				bug.getProductComponent(),
				bug.getComponentVersion(),
				platformList.get(platformIndex),
				operatingSystemList.get(operatingSystemIndex),
				priorityList.get(priorityIndex),
				severityList.get(severityIndex),
				c);
			bugToCommit.setId(bug.getId());
			//bugToCommit.setStatus(bug.getStatus());
			bugToCommit.setStatus(bugGeneralStatusList.get(bugStatusIndex));
			bugToCommit.setUserAssignedTo(bug.getUserAssignedTo());
			bugToCommit.setOpenDate(bug.getOpenDate());
			Set<User> ccUsers = new HashSet<User>();
			ccUsers.addAll(ccBugUsers);
			bugToCommit.setCcUsers(ccUsers);
			
			try {
				facade.commitBug(bugToCommit);
				additionalComment = "";
				return null;
				//return WebUtil.HOME_OUTCOME;
			} catch (UserHasNoRightException e){
				userHasNoRight = true;
				log.error("UserHasNoRightException");
			} catch (BugStatusChangeException e){
				cannotChangeBugStatus = true;
				log.error("BugStatusChangeException");
			} catch (BugException e) {
				log.error("BugException");
			} catch (UserException e) {
				log.error("UserException");
			} catch (FatalDomainException e) {
				log.error("FatalDomainException");
			}
		} 
		return null;
	}
		
	public boolean isValidData(){
		boolean validData = true;
		
		summaryNotEntered = false;
		observedDateNotEnterd = false;
		ccUsersNotValid = false;
		invalidUserNames = "";
		assignedUserSameAsCCUser = false;
		userNameAssignedSameAsCCUser = "";
		
		if (summary.trim().equals("")){
			validData = false;
			summaryNotEntered = true;
		}
		if (dateObserved == null){
			validData = false;
			observedDateNotEnterd = true;
		}
		if (usersAreValid() == false){
			validData = false;
			ccUsersNotValid = true;
		}
		if (assignedUserSameAsCCUser==true){
			validData = false;
		}
		return validData;
	}
	
	private boolean usersAreValid(){
		
		boolean validUsers = true;
		
		if (ccUsers!= null && ccUsers.trim().compareTo("")!=0){
			String[] tokens = ccUsers.split(",");
			int length = tokens.length;
			if (length>0){
				this.ccBugUsers = new ArrayList<User>();
				DomainFacade facade= WebUtil.getDomainFacade();
				for (int i=0; i<length; i++){
					String userName = tokens[i].trim();
					try {
						User u = facade.getUser(userName, false);
						if (u==null){
							validUsers = false;
							if (this.invalidUserNames.trim().compareTo("")==0){
								this.invalidUserNames +=userName;
							} else {
								this.invalidUserNames += ", " +userName;
							}
						} else {
							if (assignedTo.compareTo(u.getUserName())==0){
								this.assignedUserSameAsCCUser = true;
								this.userNameAssignedSameAsCCUser = u.getUserName();
							} else {
								this.ccBugUsers.add(u);
							}
						}
					} catch (ExternalSystemException e){
						log.error("ExternalSystemException");
						validUsers = false;
					}
				}
			}
		}
		
		return validUsers;
	}

	public String getLoadData(){
		if (getBugForViewingFormSession()== 0){
			loadSomeDataFromDb();
			initializeBugDetails();
		}
		return null;
	}
	
	private int getBugForViewingFormSession(){
		/* 0 - bug id from session was not null; bug id found in the database 
		 * 1 - bug id from session was not null; bug id was not found in the database
		 * 2 - bug id from session was null
		 * 3 - bug id from session was invalid
		*/
		bugIdInvalid = false;
		bugIdNullOrNotFound = false;
		bugFromSessionNull = false;
		bugNotFoundInTheDataBase = false;
		
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
				bugFromSessionNull = false;
				if (bug == null){
					bugIdNullOrNotFound = true;
					bugNotFoundInTheDataBase = true;
					return 1;
				}
				return 0;
				
			} catch (NumberFormatException nfe){
				bugIdInvalid = true;
				return 3;
			} catch (IllegalArgumentException iar){
				bugIdInvalid = true;
				return 3;
			}
		} else {
			log.error("Bug from session could not be retrieved!");
			bugFromSessionNull = true;
			bugIdNullOrNotFound = true;
			return 2;
		}
	}
	
	private void loadSomeDataFromDb(){
		createOperatingSystemLists();
		createPlatformLists();
		createPriorityLists();
		createSeverityLists();
		createBugGeneralStatusLists();
		createCommentsRows();
	}
	
	private void initializeBugDetails(){
		if (bug != null){
			bugId = bug.getId();
			dateReported = bug.getOpenDate(); 
			reporterUserName = bug.getOwner().getUserName();
			productName = bug.getProduct().getName();
			if (bug.getProductComponent()!=null) {
				componentName = bug.getProductComponent().getName();
			} else {
				componentName = "-";
			}
			if (bug.getComponentVersion()!=null) {
				versionName = bug.getComponentVersion().getName();
			} else {
				versionName = "-";
			}
			
			operatingSystem = bug.getOperatingSystem().getName();
			platform = bug.getPlatform().getName();
			priority = bug.getPriority().getName();
			severity = bug.getSeverity().getName();
			dateObserved = bug.getObservedDate();
			bugState = bug.getStatus().name();
			assignedTo = bug.getUserAssignedTo().getUserName();
			summary = bug.getSummary();
			description = bug.getDescription();
			
			ccBugUsers = new ArrayList<User>();
			ccUsers = "";
			if (bug.getCcUsers()!=null && !bug.getCcUsers().isEmpty()){
				ccBugUsers = new ArrayList<User>();
				ccBugUsers.addAll(bug.getCcUsers());
				for (User us: ccBugUsers){
					if (ccUsers==null || ccUsers.trim().compareTo("")==0){
						ccUsers = us.getUserName();
					} else {
						ccUsers += ","+us.getUserName();
					}
				}
			}
		}
	}
	
	public boolean isCanChangeStatus(){
		if (WebUtil.getUser().hasRight(Right.EDIT_BUGS)) //has the right to edit anything
			return true;
		
		if (WebUtil.getUser().getId().compareTo(this.bug.getOwner().getId())==0) //is the owner
			return true;
		
		if (WebUtil.getUser().getId().compareTo(this.bug.getUserAssignedTo().getId())==0) //is the user assigned to the bug
			return true;
		/*
		if (WebUtil.getUser().hasRight(Right.CHANGE_BUG_STATUS))
			return true;
		else
		*/
		
		return false;
	}
	public boolean isCanEditBug(){
		if (WebUtil.getUser().hasRight(Right.EDIT_BUGS)) //has the right to edit anything
			return true;
		
		if (WebUtil.getUser().getId().compareTo(this.bug.getOwner().getId())==0) //is the owner
			return true;
		
		if (WebUtil.getUser().getId().compareTo(this.bug.getUserAssignedTo().getId())==0 && WebUtil.getUser().hasRight(Right.EDIT_BUG)) //is the user assigned to the bug and has EDIT_BUG right
			return true;
		/*
		if (WebUtil.getUser().hasRight(Right.EDIT_BUG))
			return true;
		else
		 */
		
		return false;
	}
	public boolean isCanAddComment(){
		if (WebUtil.getUser().hasRight(Right.ADD_BUG_COMMENT))
			return true;
		else 
			return false;
	}

	// initializes the operating system list, both of them
	private void createOperatingSystemLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			operatingSystemList = facade.getAllOperatingSystems();
			operatingSystemList_out = returnSelectItemLinkedListFromAOperatingSystemList(operatingSystemList, false);
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException! ");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAOperatingSystemList(List<OperatingSystem> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (OperatingSystem op : list) {
				SelectItem item = new SelectItem(op.getName() , op.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the platform list, both of them
	@SuppressWarnings("unchecked")
	private void createPlatformLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			platformList = facade.getAllPlatforms();
			platformList_out = returnSelectItemLinkedListFromAPlatformList(platformList, false);
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException! ");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAPlatformList(List<Platform> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (Platform p : list) {
				SelectItem item = new SelectItem(p.getName() , p.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the priority list, both of them
	@SuppressWarnings("unchecked")
	private void createPriorityLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			priorityList = facade.getAllPriorities();
			priorityList_out = returnSelectItemLinkedListFromAPriorityList(priorityList, false);
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException! ");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAPriorityList(List<Priority> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (Priority p : list) {
				SelectItem item = new SelectItem(p.getName() , p.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the severity list, both of them
	@SuppressWarnings("unchecked")
	private void createSeverityLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			severityList = facade.getAllSeverities();
			severityList_out = returnSelectItemLinkedListFromASeverityList(severityList, false);
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException! ");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromASeverityList(List<Severity> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (Severity s : list) {
				SelectItem item = new SelectItem(s.getName() , s.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the bug general status list, both of them
	@SuppressWarnings("unchecked")
	private void createBugGeneralStatusLists(){
		bugGeneralStatusList = new ArrayList<BugGeneralStatus>();
		bugGeneralStatusList.add(BugGeneralStatus.ASSIGNED);
		bugGeneralStatusList.add(BugGeneralStatus.FIXED);
		bugGeneralStatusList.add(BugGeneralStatus.INVALID);
		bugGeneralStatusList.add(BugGeneralStatus.LATER);
		bugGeneralStatusList.add(BugGeneralStatus.NEW);
		bugGeneralStatusList.add(BugGeneralStatus.REOPENED);
		bugGeneralStatusList.add(BugGeneralStatus.WONTFIX);
		bugGeneralStatusList.add(BugGeneralStatus.WORKSFORME);
		bugGeneralStatusList_out = returnSelectItemLinkedListFromABugGeneralStatusList(bugGeneralStatusList, false);
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromABugGeneralStatusList(List<BugGeneralStatus> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (BugGeneralStatus s : list) {
				SelectItem item = new SelectItem(s.name() , s.name());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private void createCommentsRows(){
		log.info("Create additional comments rows");
		
		Set<Comment> commsSet = bug.getComments();
		comments = new ArrayList<CommentsWrapperBean>();
		for (Comment c : commsSet){
			comments.add(new CommentsWrapperBean(c));
		}
		Collections.sort(comments, new CommentsWrapperBeanComparator());

		if (comments.isEmpty())
			commentsListEmpty = true;
		else 
			commentsListEmpty = false;
	}
	
	public String getDateFormat() {
		SimpleDateFormat dateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, WebUtil.getLocale());
		return dateFormat.toPattern();
	}
	
	public boolean isBugIdInvalidOrNullOrNotFound(){
		return bugIdInvalid || bugIdNullOrNotFound;
	}
	// Getters and Setters
	public Long getBugId() {
		return bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}

	public Date getDateReported() {
		return dateReported;
	}

	public void setDateReported(Date dateReported) {
		this.dateReported = dateReported;
	}

	public String getReporterUserName() {
		return reporterUserName;
	}

	public void setReporterUserName(String reporterUserName) {
		this.reporterUserName = reporterUserName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Date getDateObserved() {
		return dateObserved;
	}

	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}

	public String getBugState() {
		return bugState;
	}

	public void setBugState(String bugState) {
		this.bugState = bugState;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdditionalComment() {
		return additionalComment;
	}

	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}

	public LinkedList<SelectItem> getOperatingSystemList_out() {
		return operatingSystemList_out;
	}

	public void setOperatingSystemList_out(
			LinkedList<SelectItem> operatingSystemList_out) {
		this.operatingSystemList_out = operatingSystemList_out;
	}

	public LinkedList<SelectItem> getPlatformList_out() {
		return platformList_out;
	}

	public void setPlatformList_out(LinkedList<SelectItem> platformList_out) {
		this.platformList_out = platformList_out;
	}

	public LinkedList<SelectItem> getPriorityList_out() {
		return priorityList_out;
	}

	public void setPriorityList_out(LinkedList<SelectItem> priorityList_out) {
		this.priorityList_out = priorityList_out;
	}

	public LinkedList<SelectItem> getSeverityList_out() {
		return severityList_out;
	}

	public void setSeverityList_out(LinkedList<SelectItem> severityList_out) {
		this.severityList_out = severityList_out;
	}

	public LinkedList<SelectItem> getBugGeneralStatusList_out() {
		return bugGeneralStatusList_out;
	}

	public void setBugGeneralStatusList_out(
			LinkedList<SelectItem> bugGeneralStatusList_out) {
		this.bugGeneralStatusList_out = bugGeneralStatusList_out;
	}

	public boolean isSummaryNotEntered() {
		return summaryNotEntered;
	}

	public void setSummaryNotEntered(boolean summaryNotEntered) {
		this.summaryNotEntered = summaryNotEntered;
	}

	public boolean isObservedDateNotEnterd() {
		return observedDateNotEnterd;
	}

	public void setObservedDateNotEnterd(boolean observedDateNotEnterd) {
		this.observedDateNotEnterd = observedDateNotEnterd;
	}

	public boolean isUserHasNoRight() {
		return userHasNoRight;
	}

	public void setUserHasNoRight(boolean userHasNoRight) {
		this.userHasNoRight = userHasNoRight;
	}

	public boolean isCannotChangeBugStatus() {
		return cannotChangeBugStatus;
	}

	public void setCannotChangeBugStatus(boolean cannotChangeBugStatus) {
		this.cannotChangeBugStatus = cannotChangeBugStatus;
	}

	public ArrayList<CommentsWrapperBean> getComments() {
		return comments;
	}

	public void setComments(ArrayList<CommentsWrapperBean> comments) {
		this.comments = comments;
	}

	public boolean isCommentsListEmpty() {
		return commentsListEmpty;
	}

	public void setCommentsListEmpty(boolean commentsListEmpty) {
		this.commentsListEmpty = commentsListEmpty;
	}

	public boolean isBugFromSessionNull() {
		return bugFromSessionNull;
	}

	public void setBugFromSessionNull(boolean bugFromSessionNull) {
		this.bugFromSessionNull = bugFromSessionNull;
	}

	public boolean isBugNotFoundInTheDataBase() {
		return bugNotFoundInTheDataBase;
	}

	public void setBugNotFoundInTheDataBase(boolean bugNotFoundInTheDataBase) {
		this.bugNotFoundInTheDataBase = bugNotFoundInTheDataBase;
	}

	public boolean isBugIdNullOrNotFound() {
		return bugIdNullOrNotFound;
	}

	public void setBugIdNullOrNotFound(boolean bugIdNullOrNotFound) {
		this.bugIdNullOrNotFound = bugIdNullOrNotFound;
	}

	public boolean isBugIdInvalid() {
		return bugIdInvalid;
	}

	public void setBugIdInvalid(boolean bugIdInvalid) {
		this.bugIdInvalid = bugIdInvalid;
	}

	public String getCcUsers() {
		return ccUsers;
	}

	public void setCcUsers(String ccUsers) {
		this.ccUsers = ccUsers;
	}

	public boolean isCcUsersNotValid() {
		return ccUsersNotValid;
	}

	public void setCcUsersNotValid(boolean ccUsersNotValid) {
		this.ccUsersNotValid = ccUsersNotValid;
	}

	public String getInvalidUserNames() {
		return invalidUserNames;
	}

	public void setInvalidUserNames(String invalidUserNames) {
		this.invalidUserNames = invalidUserNames;
	}

	public boolean isAssignedUserSameAsCCUser() {
		return assignedUserSameAsCCUser;
	}

	public void setAssignedUserSameAsCCUser(boolean assignedUserSameAsCCUser) {
		this.assignedUserSameAsCCUser = assignedUserSameAsCCUser;
	}

	public String getUserNameAssignedSameAsCCUser() {
		return userNameAssignedSameAsCCUser;
	}

	public void setUserNameAssignedSameAsCCUser(String userNameAssignedSameAsCCUser) {
		this.userNameAssignedSameAsCCUser = userNameAssignedSameAsCCUser;
	}
}

class CommentsWrapperBeanComparator implements Comparator<CommentsWrapperBean> {
	public int compare(CommentsWrapperBean item1, CommentsWrapperBean item2) {
		if (item1 instanceof CommentsWrapperBean && item2 instanceof CommentsWrapperBean) {
			return ((CommentsWrapperBean) item1).getCommentId().compareTo((((CommentsWrapperBean) item2).getCommentId()));
		}
		return 0;
	}
}