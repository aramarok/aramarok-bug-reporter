package com.drey.aramarok.web;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.register.NoPasswordException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class PreferencesBean {
	
	private Logger log = Logger.getLogger(PreferencesBean.class);
	
	private List<Role> roleList_in = null;
	private LinkedList<SelectItem> roleList_out = new LinkedList<SelectItem>();
	
	private User user = null;
	private String userName;
	private String password;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String middleName;
	private String registerDate;
	private String roleSelected = "";
	private User editedUserObject = null;
	
	private boolean editedUserNameNotFound = false;
	private boolean editedEmailInvalid = false;
	
	private boolean editUser = false;
	
	public void editUserButton(){
		editUser = true;
	}
	
	public void saveEditedUser(){
		editedUserNameNotFound = false;
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
					editedUserObject.setStatus(user.getStatus());

					try {
						facade.modifyUser(editedUserObject.getId(), editedUserObject, passwordModified);
						editUser = false;
					} catch (UserNotFoundException e) {
						log.error("UserNotFoundException!");
						editedUserNameNotFound = true;
					} catch (NoPasswordException e) {
						log.error("NoPasswordException!");
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
		
		editedEmailInvalid = false;
		
		if (emailAddress == null || emailAddress.trim().equals("")) {
			validData = false;
			editedEmailInvalid = true;
		}
		
		return validData;
	}
	public void doNotSaveEditedUser(){
		editedUserNameNotFound = false;
		editedEmailInvalid = false;
		
		editUser = false;
		loadSelectedUserNamesData();
	}
	
	
	private void loadSelectedUserNamesData(){
		if (user != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			editedUserObject = facade.getUser(user.getUserName());
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
			} else {
				log.error("The 'userSelected' is NULL");
			}
		} else {
			log.error("The 'user' is NULL.");
		}
	}
	
	
	private void resetUserFields(){
		userName = "";
		password = "";
		emailAddress = "";
		firstName = "";
		lastName = "";
		middleName = "";
		roleSelected = "";
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

	
	public PreferencesBean() {
		resetUserFields();	
		
		initializeRoleList();
		loadSelectedUserNamesData();
	}
	
	
	
	// Getters and setters
	public String getLoadData(){
		user = WebUtil.getUser();
		loadSelectedUserNamesData();
		return "";
	}

	public LinkedList<SelectItem> getRoleList_out() {
		return roleList_out;
	}

	public void setRoleList_out(LinkedList<SelectItem> roleList_out) {
		this.roleList_out = roleList_out;
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

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getRoleSelected() {
		return roleSelected;
	}

	public void setRoleSelected(String roleSelected) {
		this.roleSelected = roleSelected;
	}
	
	public boolean isEditedUserNameNotFound() {
		return editedUserNameNotFound;
	}

	public void setEditedUserNameNotFound(boolean editedUserNameNotFound) {
		this.editedUserNameNotFound = editedUserNameNotFound;
	}

	public boolean isEditedEmailInvalid() {
		return editedEmailInvalid;
	}

	public void setEditedEmailInvalid(boolean editedEmailInvalid) {
		this.editedEmailInvalid = editedEmailInvalid;
	}

	public boolean isEditUser() {
		return editUser;
	}

	public void setEditUser(boolean editUser) {
		this.editUser = editUser;
	}
}
