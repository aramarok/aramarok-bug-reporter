package com.drey.aramarok.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class NewBugEnterBean {
	private Logger log = Logger.getLogger(NewBugEnterBean.class);
	
	private Product productObject = null;
	private ProductComponent productComponentObject = null;
	private ComponentVersion componentVersionObject = null;
	private User userAssignedToObject = null;
	
	private String reporterUserName = "";
	private String productName = "";
	private String productComponent = "";
	private String componentVersion = "";
	private String operatingSystem = "";
	private String platform = "";
	private String priority = "";
	private String severity = "";
	private String bugState = "";
	private String assignedTo = "";
	private String summary = "";
	private String description = "";
	private Date dateObserved = new Date();
	
	
	private List<ProductComponent> productComponentList = null;
	private LinkedList<SelectItem> productComponentList_out = new LinkedList<SelectItem>();
	
	private List<ComponentVersion> componentVersionList = null;
	private LinkedList<SelectItem> componentVersionList_out = new LinkedList<SelectItem>();
	
	private List<OperatingSystem> operatingSystemList = null;
	private LinkedList<SelectItem> operatingSystemList_out = new LinkedList<SelectItem>();
	
	private List<Platform> platformList = null;
	private LinkedList<SelectItem> platformList_out = new LinkedList<SelectItem>();
	
	private List<Priority> priorityList = null;
	private LinkedList<SelectItem> priorityList_out = new LinkedList<SelectItem>();
	
	private List<Severity> severityList = null;
	private LinkedList<SelectItem> severityList_out = new LinkedList<SelectItem>();
	

	private boolean componentNotSelected= false;
	private boolean versionNotSelected = false;
	private boolean operatingSystemNotSelected = false;
	private boolean platformNotSelected = false;
	private boolean priorityNotSelected = false;
	private boolean severityNotSelected = false;
	private boolean summaryNotEntered = false;
	private boolean descriptionNotEntered = false;
	private boolean userHasNoRight = false;
	private boolean cannotChangeBugStatus = false;
	private boolean componentVersionHasNoSolverAssigned = false;
	
	
	public NewBugEnterBean(){
	}
	
	public String commitBug(){
		userHasNoRight = false;
		cannotChangeBugStatus = false;
		
		if (isValidData()){
			
			int componentVersionIndex = -1;
			for (SelectItem s : componentVersionList_out){
				if (s.getLabel().compareTo(componentVersion) == 0)
					componentVersionIndex = componentVersionList_out.indexOf(s);
			}
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
			
			DomainFacade facade = WebUtil.getDomainFacade();
			Bug bug = new Bug(
					WebUtil.getUser(),
					dateObserved,
					userAssignedToObject,
					summary,
					description,
					BugGeneralStatus.NEW,
					productObject,
					productComponentObject,
					componentVersionList.get(componentVersionIndex-1),
					platformList.get(platformIndex-1),
					operatingSystemList.get(operatingSystemIndex-1),
					priorityList.get(priorityIndex-1),
					severityList.get(severityIndex-1),
					null);
			try {
				Long idBug = facade.commitBug(bug);
				
				HttpSession session = WebUtil.getHttpSession();
				session.setAttribute(WebUtil.NEW_INSERED_BUG_ID, idBug);
				
				return WebUtil.NEW_BUG_SUCCESSFULLY_ENTER_OUTCOME;
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
	
	private boolean isValidData(){
		boolean validData = true;
		
		componentNotSelected= false;
		versionNotSelected = false;
		operatingSystemNotSelected = false;
		platformNotSelected = false;
		priorityNotSelected = false;
		severityNotSelected = false;
		summaryNotEntered = false;
		descriptionNotEntered = false;
		componentVersionHasNoSolverAssigned = false;
		
		if(	productComponent.trim().equals("")) {
			validData = false;
			componentNotSelected = true;
		}
		if(	componentVersion.trim().equals("")) {
			validData = false;
			versionNotSelected = true;
		}
		if(	operatingSystem.trim().equals("")) {
			validData = false;
			operatingSystemNotSelected = true;
		}
		if(	platform.trim().equals("")) {
			validData = false;
			platformNotSelected = true;
		}
		if(	priority.trim().equals("")) {
			validData = false;
			priorityNotSelected = true;
		}
		if(	severity.trim().equals("")) {
			validData = false;
			severityNotSelected = true;
		}
		if(	summary.trim().equals("")) {
			validData = false;
			summaryNotEntered = true;
		}
		if(	description.trim().equals("")) {
			validData = false;
			descriptionNotEntered = true;
		}
		if (userAssignedToObject == null){
			validData = false;
			componentVersionHasNoSolverAssigned = true;
		}
		
		return validData;
	}
	
	public String getLoadData(){
		getProductFormSession();
		
		initialize();
		//isValidData();
		return "";
	}
	
	// gets the product form the session
	private void getProductFormSession(){
		HttpSession session = WebUtil.getHttpSession();
		Object o = session.getAttribute(WebUtil.ProductToEnterNewBug);
		if (o!=null && o instanceof Product){
			this.productObject = (Product)o;
		} else {
			log.error("Product from session could not be retrieved!");
		}
		
		Object o2 = session.getAttribute(WebUtil.ENTER_NEW_BUG_FOR_PRODUCT);
		if (o2!=null && o2 instanceof Boolean){
			if ((Boolean)o2 == true){
				firstInit();
				session.setAttribute(WebUtil.ENTER_NEW_BUG_FOR_PRODUCT, new Boolean(false));
			}
		} else {
			log.error("Obj from session could not be retrieved!");
		}
	}
	
	// initializes the product component lists, both of them
	private void createProductComponentLists(){
		if (productObject != null){
			if (productObject.getComponents() != null){
				productComponentList = new ArrayList<ProductComponent>(productObject.getComponents());
				productComponentList_out = returnSelectItemLinkedListFromAProductComponentList(productComponentList, true);
			} else {
				productComponentList = null;
				productComponentList_out = new LinkedList<SelectItem>();
			}
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAProductComponentList(List<ProductComponent> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (ProductComponent pc : list) {
				SelectItem item = new SelectItem(pc.getName() , pc.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the component version lists, both of them
	private void createComponentVersionLists(){
		if (productComponentObject != null) {
			if (productComponentObject.getVersions() != null){
				componentVersionList =  new ArrayList<ComponentVersion>(productComponentObject.getVersions());
				componentVersionList_out = returnSelectItemLinkedListFromAComponentVersionList(componentVersionList, true);
			} else{
				componentVersionList = null;
				componentVersionList_out = returnSelectItemLinkedListFromAComponentVersionList(componentVersionList, true);
			}
		} else{
			componentVersionList = null;
			componentVersionList_out = returnSelectItemLinkedListFromAComponentVersionList(componentVersionList, true);
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAComponentVersionList(List<ComponentVersion> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for (ComponentVersion cv : list) {
				SelectItem item = new SelectItem(cv.getName() , cv.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	// initializes the operating system list, both of them
	private void createOperatingSystemLists(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			operatingSystemList = facade.getAllOperatingSystems();
			operatingSystemList_out = returnSelectItemLinkedListFromAOperatingSystemList(operatingSystemList, true);
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
			platformList_out = returnSelectItemLinkedListFromAPlatformList(platformList, true);
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
			priorityList_out = returnSelectItemLinkedListFromAPriorityList(priorityList, true);
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
			severityList_out = returnSelectItemLinkedListFromASeverityList(severityList, true);
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
	
	private void firstInit(){
		createProductComponentLists();
		createComponentVersionLists();
		createOperatingSystemLists();
		createPlatformLists();
		createPriorityLists();
		createSeverityLists();
		summary = "";
		description = "";
		
		if (productComponentList_out != null)
			if (!productComponentList_out.isEmpty())
				if (productComponentList_out.getFirst() != null)
					productComponent = productComponentList_out.getFirst().getLabel();
		if (componentVersionList_out != null)
			if (!componentVersionList_out.isEmpty())
				if (componentVersionList_out.getFirst() != null)
					componentVersion = componentVersionList_out.getFirst().getLabel();
		if (operatingSystemList_out != null)
			if (!operatingSystemList_out.isEmpty())
				if (operatingSystemList_out.getFirst() != null)
					operatingSystem = operatingSystemList_out.getFirst().getLabel();
		if (platformList_out != null)
			if (!platformList_out.isEmpty())
				if (platformList_out.getFirst() != null)
					platform = platformList_out.getFirst().getLabel();
		if (priorityList_out != null)
			if (!priorityList_out.isEmpty())
				if (priorityList_out.getFirst() != null)
					priority = priorityList_out.getFirst().getLabel();
		if (severityList_out != null)
			if (!severityList_out.isEmpty())
				if (severityList_out.getFirst() != null)
					severity = severityList_out.getFirst().getLabel();
	}
	
	private void initialize(){
		reporterUserName = WebUtil.getUser().getUserName();
		productName = productObject.getName();
		for (ProductComponent pc: productObject.getComponents()){
			if (pc.getName().compareTo(productComponent) == 0)
				productComponentObject = pc;
		}
		createComponentVersionLists();
		if (productComponentObject != null) {
			for (ComponentVersion cv: productComponentObject.getVersions()){
				if (cv.getName().compareTo(componentVersion) == 0)
					componentVersionObject = cv;
			}
		}
		if (componentVersionObject!=null) {
			if (componentVersionObject.getUserAssigned()!=null){
				userAssignedToObject = componentVersionObject.getUserAssigned();
				assignedTo = userAssignedToObject.getUserName();
			} else {
				userAssignedToObject = null;
				assignedTo = "";
			}
		} else {
			userAssignedToObject = null;
			assignedTo = "";
		}
		
		bugState = BugGeneralStatus.NEW.name();
	}

	public String getDateFormat() {
		SimpleDateFormat dateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, WebUtil.getLocale());
		return dateFormat.toPattern();
	}
	
	// Getters and Setters
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

	public String getProductComponent() {
		return productComponent;
	}

	public void setProductComponent(String productComponent) {
		this.productComponent = productComponent;
	}

	public String getComponentVersion() {
		return componentVersion;
	}

	public void setComponentVersion(String componentVersion) {
		this.componentVersion = componentVersion;
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

	public Date getDateObserved() {
		return dateObserved;
	}

	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}

	public LinkedList<SelectItem> getProductComponentList_out() {
		return productComponentList_out;
	}

	public void setProductComponentList_out(
			LinkedList<SelectItem> productComponentList_out) {
		this.productComponentList_out = productComponentList_out;
	}

	public LinkedList<SelectItem> getComponentVersionList_out() {
		return componentVersionList_out;
	}

	public void setComponentVersionList_out(
			LinkedList<SelectItem> componentVersionList_out) {
		this.componentVersionList_out = componentVersionList_out;
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

	public boolean isComponentNotSelected() {
		return componentNotSelected;
	}

	public void setComponentNotSelected(boolean componentNotSelected) {
		this.componentNotSelected = componentNotSelected;
	}

	public boolean isVersionNotSelected() {
		return versionNotSelected;
	}

	public void setVersionNotSelected(boolean versionNotSelected) {
		this.versionNotSelected = versionNotSelected;
	}

	public boolean isOperatingSystemNotSelected() {
		return operatingSystemNotSelected;
	}

	public void setOperatingSystemNotSelected(boolean operatingSystemNotSelected) {
		this.operatingSystemNotSelected = operatingSystemNotSelected;
	}

	public boolean isPlatformNotSelected() {
		return platformNotSelected;
	}

	public void setPlatformNotSelected(boolean platformNotSelected) {
		this.platformNotSelected = platformNotSelected;
	}

	public boolean isPriorityNotSelected() {
		return priorityNotSelected;
	}

	public void setPriorityNotSelected(boolean priorityNotSelected) {
		this.priorityNotSelected = priorityNotSelected;
	}

	public boolean isSeverityNotSelected() {
		return severityNotSelected;
	}

	public void setSeverityNotSelected(boolean severityNotSelected) {
		this.severityNotSelected = severityNotSelected;
	}

	public boolean isSummaryNotEntered() {
		return summaryNotEntered;
	}

	public void setSummaryNotEntered(boolean summaryNotEntered) {
		this.summaryNotEntered = summaryNotEntered;
	}

	public boolean isDescriptionNotEntered() {
		return descriptionNotEntered;
	}

	public void setDescriptionNotEntered(boolean descriptionNotEntered) {
		this.descriptionNotEntered = descriptionNotEntered;
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

	public boolean isComponentVersionHasNoSolverAssigned() {
		return componentVersionHasNoSolverAssigned;
	}

	public void setComponentVersionHasNoSolverAssigned(
			boolean componentVersionHasNoSolverAssigned) {
		this.componentVersionHasNoSolverAssigned = componentVersionHasNoSolverAssigned;
	}
}
