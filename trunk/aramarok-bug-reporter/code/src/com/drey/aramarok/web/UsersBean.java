package com.drey.aramarok.web;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.register.NoPasswordException;
import com.drey.aramarok.domain.exceptions.register.NoUserNameException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserStatus;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

/**
 * 
 * @author Tolnai Andrei
 *
 */

public class UsersBean {
	private Logger log = Logger.getLogger(UsersBean.class);

	private List<User> userNameList_in = null;
	private LinkedList<SelectItem> userNameList_out = new LinkedList<SelectItem>();
	
	private List<Role> roleList_in = null;
	private LinkedList<SelectItem> roleList_out = new LinkedList<SelectItem>();
	
	private LinkedList<SelectItem> userStatusList = new LinkedList<SelectItem>();
	
	private String userNameSelected;
	
	private String userName;
	private String password;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String middleName;
	private String registerDate;
	private String roleSelected = "";
	private String userStatusSelected = "";	
	private User editedUserObject = null;
	
	private boolean editedUserNameAlreadyExists= false;
	private boolean editedUserNameInvalid = false;
	private boolean editedUserNameNotFound = false;
	private boolean editedPasswordInvalid = false;
	private boolean editedEmailInvalid = false;
	
	private boolean newUserNameInvalid = false; 
	private boolean newPasswordInvalid = false;
	private boolean newEmailInvalid = false;
	
	private String newUserName = "";
	private String newPassword = "";
	private String newEmailAddress = "";
	private String newFirstName = "";
	private String newLastName;
	private String newMiddleName;
	private String newRoleSelected = "";
	private String newUserStatusSelected = "";
	
	private boolean editUser = false;
	private boolean addAUser = false;
	
	private boolean newUserNameIsInvalid = false;
	private boolean editedUserNameIsInvalid = false;
	
	public void addNewUserButton(){
		addAUser = !addAUser;
	}
	
	public void cancelAddNewUser(){
		resetNewUserFields();
		newUserNameIsInvalid = false;
		addAUser = false;
	}
	
	public void addANewUserToDB(){
		/*if (!newUserName.trim().equals("")){
			newUserNameIsInvalid = false;
			Country newCountrySelected = returnCountryObject(newCountry_selected, countryList_in);
			DomainFacade dom = WebUtil.getDomainFacade();
			if (dom != null) {
				try {
					dom.addNewLocation(newName, newInfo, newChannel, newCountrySelected);
					initializeUserNameList();
				} catch (FatalDomainException e) {
					log.error("FatalDomainException. " , e);
				}
			} else{
				log.error("DomainFacade is null.");
			}
		} else {
			newUserNameIsInvalid = true;
			log.error("The 'newUserName' parrameter is only made of whitespaces.");
			return;
		} 
		
		resetNewUserFields();
		addAUser = false;*/
	}
	
	public void editUserButton(){
		editUser = true;
	}
	
	public void saveEditedUser(){
		editedUserNameAlreadyExists= false;
		editedUserNameInvalid = false;
		editedUserNameNotFound = false;
		editedPasswordInvalid = false;
		editedEmailInvalid = false;
		
		if (isValidDataForEditedUser()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			if (facade != null) {
				try {
					boolean passwordModified = false;
					editedUserObject.setUserName(userName);
					if (password.compareTo("")!=0){ // else the password was not modified
						editedUserObject.setPassword(password);
						passwordModified = true;
					}
					editedUserObject.setEmailAddress(emailAddress);
					editedUserObject.setFirstName(firstName);
					editedUserObject.setMiddleName(middleName);
					editedUserObject.setLastName(lastName);
					editedUserObject.setRole(getRoleByName(roleSelected));
					editedUserObject.setStatus(UserStatus.getUserStatusByName(userStatusSelected));

					try {
						facade.modifyUser(editedUserObject.getId(), editedUserObject, passwordModified);
						reInitializeUserList();
						editUser = false;
					} catch (UserNotFoundException e) {
						log.error("UserNotFoundException!");
						editedUserNameNotFound = true;
					} catch (NoUserNameException e) {
						log.error("NoUserNameException!");
						editedUserNameInvalid = true; 
					} catch (NoPasswordException e) {
						log.error("NoPasswordException!");
						editedPasswordInvalid = true;
					} catch (UserNameAlreadyExistsException e) {
						log.error("UserNameAlreadyExistsException!");
						editedUserNameAlreadyExists= true;
					} catch (RegisterException e) {
						log.error("RegisterException!");
						editedUserNameNotFound = true;
					} catch (UserException e) {
						log.error("UserException!");
					}
				} catch (FatalDomainException e) {
					log.error("FatalDomainException. " , e);
				}
			} else{
				log.error("DomainFacade is null.");
			}
		} else {
			log.error("Invalid data!");
			return; 	
		}
	}
	
	private boolean isValidDataForEditedUser(){
		boolean validData = true;
		
		editedUserNameInvalid = false;
		editedPasswordInvalid = false;
		editedEmailInvalid = false;
		
		if(	userName == null || userName.trim().equals("")) {
			validData = false;
			editedUserNameInvalid = true;
		}
		/*
		if ( (password == null || password.trim().equals("")) ) {
			validData = false;
			editedPasswordInvalid = true;
		}
		*/
		if (emailAddress == null || emailAddress.trim().equals("")) {
			validData = false;
			editedEmailInvalid = true;
		}
		
		return validData;
	}
	public void doNotSaveEditedUser(){
		editedUserNameAlreadyExists= false;
		editedUserNameInvalid = false;
		editedUserNameNotFound = false;
		editedPasswordInvalid = false;
		editedEmailInvalid = false;
		
		editUser = false;
		loadSelectedUserNamesData();
	}
	
	
	private void loadSelectedUserNamesData(){
		if (userNameSelected != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			editedUserObject = facade.getUser(userNameSelected);
			if (editedUserObject != null){
				userName = editedUserObject.getUserName();
				password = editedUserObject.getPassword();
				emailAddress = editedUserObject.getEmailAddress();
				firstName = editedUserObject.getFirstName();
				lastName = editedUserObject.getLastName();
				middleName = editedUserObject.getMiddleName();
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
				registerDate = formatter.format(editedUserObject.getRegisterDate());
				
				if (editedUserObject.getRole() != null){
					roleSelected = editedUserObject.getRole().getName();
				} else {
					roleSelected = "";
				}
				userStatusSelected = editedUserObject.getStatus().name();
			} else {
				log.error("The 'userSelected' is NULL");
			}
		} else {
			log.error("The 'userNameSelected' is NULL.");
		}
	}
	
	private void resetNewUserFields(){
		newUserName = "";
		newPassword = "";
		newEmailAddress = "";
		newFirstName = "";
		newLastName = "";
		newMiddleName = "";
		newRoleSelected = "";
		newUserStatusSelected = "";
	}
	
	private void resetUserFields(){
		userName = "";
		password = "";
		emailAddress = "";
		firstName = "";
		lastName = "";
		middleName = "";
		roleSelected = "";
		userStatusSelected = "";
	}

	private User returnUserObject(String userName, List<User> userList){
		if (userList != null){
			for (Iterator<User> i=userList.iterator(); i.hasNext();){
				User l = i.next();
				if (l.getUserName().compareTo(userName) == 0) {
					return l;
				}
			}
		}
		return null;
	}
	
	private void initializeUserList(){
		DomainFacade dom = WebUtil.getDomainFacade();
		if (dom != null) {
			try {
				userNameList_in = dom.getAllUsers();
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException!");
			}
			//Collections.sort(userNameList_in, new LocationListComparatorSortByName());
			userNameList_out = returnSelectItemLinkedListFromAUserList(userNameList_in, false);			
		}
		else {
			log.error("DomainFacade was NULL.");
		}
	}
	
	/**
	 * Reinitialization will be done when a user was edited and new account data saved
	 */
	private void reInitializeUserList(){
		initializeUserList();
		userNameSelected = editedUserObject.getUserName();
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAUserList(List<User> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null) {
			for(Iterator<User> i=list.iterator(); i.hasNext(); ){
				User u = i.next();
				SelectItem item = new SelectItem(u.getUserName(), u.getUserName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private Role returnRoleObject(String roleName, List<Role> roleList){
		if (roleList != null){
			for (Iterator<Role> i=roleList.iterator(); i.hasNext();){
				Role  r = i.next();
				if (r.getName().equals(roleName)){
					return r;
				}
			}
		}
		return null;
	}
	
	private List<Role> getRoleList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		if (facade != null) {
			List<Role> roleList = null;
			try {
				roleList = facade.getAllRoles();
				return roleList;
			} catch (ExternalSystemException ex) {
				log.error("ExternalSystemException.", ex);
			}
		} else {
			log.error("DomainFacade was NULL.");
		}
		return null;
	}
	
	private Role getRoleByName(String roleName) {
		List<Role> roleList = getRoleList();
		for (Role r : roleList) {
			if (r.getName().compareTo(roleName) == 0) { return r; }
		}
		return null;
	}

	private void initializeRoleList(){
		DomainFacade facade = WebUtil.getDomainFacade();
		if (facade != null) {
			try {
				roleList_in = facade.getAllRoles();
				roleList_out = returnSelectItemLinkedListFromARoleList(roleList_in, true);
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException!");
			}			
		}
		else{
			log.error("DomainFacade was NULL.");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromARoleList(List<Role> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for(Iterator<Role> i=list.iterator(); i.hasNext(); ){
				Role r = i.next();
				SelectItem item = new SelectItem(r.getName() , r.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private void selectFirstUserName(){
		if (userNameList_out != null) {
			userNameSelected = userNameList_out.getFirst().getLabel();
		}
	}
	
	private void initializeUserStatusList(){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		itemList.add(new SelectItem(UserStatus.ACTIVE.name() , UserStatus.ACTIVE.name()));
		itemList.add(new SelectItem(UserStatus.DISABLED.name() , UserStatus.DISABLED.name()));
		
		userStatusList = itemList;
	}
	
	public void valueChangeListener(ValueChangeEvent  event) {
	   if(event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
		   event.setPhaseId(PhaseId.INVOKE_APPLICATION);
		   event.queue();
		   return;
	   }
	   if (event.getComponent().getId() != null && event.getComponent().getId().compareTo("inputUserName")==0){
		   this.userNameSelected = (String) event.getNewValue();
		   this.loadSelectedUserNamesData();
	   }
   }
	
	public UsersBean() {
		resetNewUserFields();
		resetUserFields();	
		
		initializeUserList();
		initializeRoleList();
		initializeUserStatusList();
		
		selectFirstUserName();
		loadSelectedUserNamesData();
	}
	
	
	
	// Getters and setters
	public String getLoadData(){
		loadSelectedUserNamesData();
		return "";
	}

	public LinkedList<SelectItem> getUserNameList_out() {
		return userNameList_out;
	}

	public void setUserNameList_out(LinkedList<SelectItem> userNameList_out) {
		this.userNameList_out = userNameList_out;
	}

	public LinkedList<SelectItem> getRoleList_out() {
		return roleList_out;
	}

	public void setRoleList_out(LinkedList<SelectItem> roleList_out) {
		this.roleList_out = roleList_out;
	}

	public String getUserNameSelected() {
		return userNameSelected;
	}

	public void setUserNameSelected(String userNameSelected) {
		this.userNameSelected = userNameSelected;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getRoleSelected() {
		return roleSelected;
	}

	public void setRoleSelected(String roleSelected) {
		this.roleSelected = roleSelected;
	}

	public String getUserStatusSelected() {
		return userStatusSelected;
	}

	public void setUserStatusSelected(String userStatusSelected) {
		this.userStatusSelected = userStatusSelected;
	}

	public boolean isEditedUserNameInvalid() {
		return editedUserNameInvalid;
	}

	public void setEditedUserNameInvalid(boolean editedUserNameInvalid) {
		this.editedUserNameInvalid = editedUserNameInvalid;
	}

	public boolean isEditedPasswordInvalid() {
		return editedPasswordInvalid;
	}

	public void setEditedPasswordInvalid(boolean editedPasswordInvalid) {
		this.editedPasswordInvalid = editedPasswordInvalid;
	}

	public boolean isEditedEmailInvalid() {
		return editedEmailInvalid;
	}

	public void setEditedEmailInvalid(boolean editedEmailInvalid) {
		this.editedEmailInvalid = editedEmailInvalid;
	}

	public boolean isNewUserNameInvalid() {
		return newUserNameInvalid;
	}

	public void setNewUserNameInvalid(boolean newUserNameInvalid) {
		this.newUserNameInvalid = newUserNameInvalid;
	}

	public boolean isNewPasswordInvalid() {
		return newPasswordInvalid;
	}

	public void setNewPasswordInvalid(boolean newPasswordInvalid) {
		this.newPasswordInvalid = newPasswordInvalid;
	}

	public boolean isNewEmailInvalid() {
		return newEmailInvalid;
	}

	public void setNewEmailInvalid(boolean newEmailInvalid) {
		this.newEmailInvalid = newEmailInvalid;
	}

	public String getNewUserName() {
		return newUserName;
	}

	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewEmailAddress() {
		return newEmailAddress;
	}

	public void setNewEmailAddress(String newEmailAddress) {
		this.newEmailAddress = newEmailAddress;
	}

	public String getNewFirstName() {
		return newFirstName;
	}

	public void setNewFirstName(String newFirstName) {
		this.newFirstName = newFirstName;
	}

	public String getNewLastName() {
		return newLastName;
	}

	public void setNewLastName(String newLastName) {
		this.newLastName = newLastName;
	}

	public String getNewMiddleName() {
		return newMiddleName;
	}

	public void setNewMiddleName(String newMiddleName) {
		this.newMiddleName = newMiddleName;
	}

	public String getNewRoleSelected() {
		return newRoleSelected;
	}

	public void setNewRoleSelected(String newRoleSelected) {
		this.newRoleSelected = newRoleSelected;
	}

	public String getNewUserStatusSelected() {
		return newUserStatusSelected;
	}

	public void setNewUserStatusSelected(String newUserStatusSelected) {
		this.newUserStatusSelected = newUserStatusSelected;
	}

	public boolean isEditUser() {
		return editUser;
	}

	public void setEditUser(boolean editUser) {
		this.editUser = editUser;
	}

	public boolean isAddAUser() {
		return addAUser;
	}

	public void setAddAUser(boolean addAUser) {
		this.addAUser = addAUser;
	}

	public boolean isNewUserNameIsInvalid() {
		return newUserNameIsInvalid;
	}

	public void setNewUserNameIsInvalid(boolean newUserNameIsInvalid) {
		this.newUserNameIsInvalid = newUserNameIsInvalid;
	}

	public boolean isEditedUserNameIsInvalid() {
		return editedUserNameIsInvalid;
	}

	public void setEditedUserNameIsInvalid(boolean editedUserNameIsInvalid) {
		this.editedUserNameIsInvalid = editedUserNameIsInvalid;
	}

	public LinkedList<SelectItem> getUserStatusList() {
		return userStatusList;
	}

	public void setUserStatusList(LinkedList<SelectItem> userStatusList) {
		this.userStatusList = userStatusList;
	}

	public boolean isEditedUserNameNotFound() {
		return editedUserNameNotFound;
	}

	public void setEditedUserNameNotFound(boolean editedUserNameNotFound) {
		this.editedUserNameNotFound = editedUserNameNotFound;
	}

	public boolean isEditedUserNameAlreadyExists() {
		return editedUserNameAlreadyExists;
	}

	public void setEditedUserNameAlreadyExists(boolean editedUserNameAlreadyExists) {
		this.editedUserNameAlreadyExists = editedUserNameAlreadyExists;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
}