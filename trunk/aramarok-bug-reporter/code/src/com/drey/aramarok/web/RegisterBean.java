package com.drey.aramarok.web;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.register.NoPasswordException;
import com.drey.aramarok.domain.exceptions.register.NoUserNameException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNameAlreadyExistsException;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;
import com.drey.aramarok.web.util.WebUtilConstants;

public class RegisterBean {
	private Logger log = Logger.getLogger(RegisterBean.class);
	private Properties aramarokProperties = new Properties();
	
	private String userName = "";
	private String password = "";
	private String reenterPassword = "";
	private String emailAddress = "";
	private String firstName = "";
	private String lastName = "";
	private String middleName = "";
	
	private Boolean userNameAlreadyExists = false;
	private Boolean userNameInvalid = false;
	private Boolean passwordInvalid = false;
	private Boolean notSamePassword = false;
	private Boolean emailInvalid = false;
	
	public RegisterBean(){
	}
	
	public String registerUser() {
		log.info("Tring to register user name: "+ userName);
		
		if(isValidData()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			try {
				facade.registerNewUser(userName, password, emailAddress, firstName, lastName, middleName);
			} catch (UserNameAlreadyExistsException e) {
				log.info("UserNameAlreadyExistsException for user name: "+userName);
				userNameAlreadyExists = true;
				return null;
			} catch (NoPasswordException e) {
				log.info("NoPasswordException for user name: "+userName);
				passwordInvalid = true;
				return null;
			} catch (NoUserNameException e) {
				log.info("NoUserNameException for user name: "+userName);
				userNameInvalid = true;
				return null;
			} catch (RegisterException e) {
				log.info("RegisterException for user name: "+userName);
				return null;
			} catch (FatalDomainException e) {
				log.error("FatalDomainException", e);
				return WebUtilConstants.ERROR_PAGE_OUTCOME;
			}
			return WebUtilConstants.SUCCESS_REGISTER_NEW_ACCOUNT_OUTCOME;
		} else {
			log.info("Invalid data");
		}
		
		return null;
	}
	
	private boolean isValidData() {
		boolean validData = true;
		
		if(	userName == null || userName.trim().equals("")) {
			validData = false;
			userNameInvalid = true;
		}
			
		if ( (password == null || password.trim().equals("")) ) {
			validData = false;
			passwordInvalid = true;
		}
		
		if (reenterPassword.compareTo(password) != 0) {
			validData = false;
			notSamePassword = true;
		}
		
		if (emailAddress == null || emailAddress.trim().equals("")) {
			validData = false;
			emailInvalid = true;
		}
		
		return validData;
	}

	
	// Getters and setters
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


	public String getReenterPassword() {
		return reenterPassword;
	}


	public void setReenterPassword(String reenterPassword) {
		this.reenterPassword = reenterPassword;
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

	public Properties getAramarokProperties() {
		return aramarokProperties;
	}

	public void setAramarokProperties(Properties aramarokProperties) {
		this.aramarokProperties = aramarokProperties;
	}

	public Boolean getUserNameInvalid() {
		return userNameInvalid;
	}

	public void setUserNameInvalid(Boolean userNameInvalid) {
		this.userNameInvalid = userNameInvalid;
	}

	public Boolean getPasswordInvalid() {
		return passwordInvalid;
	}

	public void setPasswordInvalid(Boolean passwordInvalid) {
		this.passwordInvalid = passwordInvalid;
	}

	public Boolean getNotSamePassword() {
		return notSamePassword;
	}

	public void setNotSamePassword(Boolean notSamePassword) {
		this.notSamePassword = notSamePassword;
	}

	public Boolean getEmailInvalid() {
		return emailInvalid;
	}

	public void setEmailInvalid(Boolean emailInvalid) {
		this.emailInvalid = emailInvalid;
	}

	public Boolean getUserNameAlreadyExists() {
		return userNameAlreadyExists;
	}

	public void setUserNameAlreadyExists(Boolean userNameAlreadyExists) {
		this.userNameAlreadyExists = userNameAlreadyExists;
	}
}