package com.drey.aramarok.web;

import javax.ejb.PostActivate;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.DomainFacade;
import com.drey.aramarok.domain.User;
import com.drey.aramarok.domain.UserStatus;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.login.DisabledAccountException;
import com.drey.aramarok.domain.exceptions.login.InvalidPasswordException;
import com.drey.aramarok.domain.exceptions.login.InvalidUserNameException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.web.util.WebUtil;
import com.drey.aramarok.web.util.WebUtilConstants;

public class LoginBean {
	private Logger log = Logger.getLogger(LoginBean.class);
	
	private String userName = null;
	private String password = null;
	
	private boolean invalidData = false;
	private boolean invalidUserName = false;
	private boolean invalidPassword = false;
	private boolean disabledAccount = false;
	
	public String login(){
		log.info("Tring to login with the username: "+ userName);
		
		if(isValidData()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			User user;
			try {
				user = facade.login(userName, password);
				
				if (user != null){
					if (user.getStatus() == UserStatus.ACTIVE){
						disabledAccount = false;
						WebUtil.setUser(user);
						// add current locale to http session // it will be used by reports.
						WebUtil.getHttpSession().setAttribute(WebUtilConstants.LOCALE, WebUtil.getLocale());
					}
					else{
						disabledAccount = true;
					}
					
				}
			} 
			catch (InvalidUserNameException e) 
			{
				log.info("InvalidUserNameException for user name: "+userName);
				setInvalidUserName(true);
				return null;
			}
			catch (InvalidPasswordException e) 
			{
				log.info("InvalidPasswordException for user name: "+userName);
				setInvalidPassword(true);
				return null;
			}
			catch (DisabledAccountException e)
			{
				log.info("DisabledAccountException for user name: "+userName);
				disabledAccount = true;
				return null;
			}
			catch (LoginException e) 
			{
				log.info("LoginException for user name: "+userName);
				return null;
			}
			catch (FatalDomainException e) 
			{
				log.error("FatalDomainException", e);
				return WebUtilConstants.ERROR_PAGE_OUTCOME;
			}
			
			return WebUtilConstants.SUCCESS_LOGIN_OUTCOME;
		} else {
			log.info("Invalid data");
			setInvalidData(true);
		}
		return null;
		
		//return WebUtilConstants.SUCCESS_LOGIN_OUTCOME;
	}

	private boolean isValidData() {
		if(	userName != null && !userName.trim().equals("") && password != null && !password.trim().equals("")) {
			return true;
		} else
			return false;
	}
	
	public String register(){
		return WebUtilConstants.REGISTER_NEW_ACCOUNT_OUTCOME;
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
	
	@PostActivate
	public void activate() {
		log = Logger.getLogger(LoginBean.class);
	}

	public boolean isDisabledAccount() {
		return disabledAccount;
	}

	public void setDisabledAccount(boolean disabledAccount) {
		this.disabledAccount = disabledAccount;
	}

	public boolean isInvalidUserName() {
		return invalidUserName;
	}

	public void setInvalidUserName(boolean invalidUserName) {
		this.invalidUserName = invalidUserName;
	}

	public boolean isInvalidPassword() {
		return invalidPassword;
	}

	public void setInvalidPassword(boolean invalidPassword) {
		this.invalidPassword = invalidPassword;
	}

	public boolean isInvalidData() {
		return invalidData;
	}

	public void setInvalidData(boolean invalidData) {
		this.invalidData = invalidData;
	}	
}