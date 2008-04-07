package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.login.DisabledAccountException;
import com.drey.aramarok.domain.exceptions.login.InvalidPasswordException;
import com.drey.aramarok.domain.exceptions.login.InvalidUserNameException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.register.NoPasswordException;
import com.drey.aramarok.domain.exceptions.register.NoUserNameException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserStatus;

@Stateless
@Local(UserService.class)
public class UserServiceBean  implements UserService, Serializable {

	private static final long serialVersionUID = -6422997018931074102L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private static Logger log = Logger.getLogger(UserServiceBean.class);
	
	public synchronized User findUser(String userName) {
		log.info("Find userName: " + userName);
		try {
			User user = (User) entityManager.createNamedQuery("User.findUserByUserName").setParameter("userName", userName).getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null; 
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized List<User> getAllUsers() throws PersistenceException{
		log.info("Get all users.");
		try {
			Query query = entityManager.createNamedQuery("User.findAll");
			return (List<User>) query.getResultList();
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized User login(String username, String password) throws LoginException {
		log.info("Trying to login user.");
		User user = findUser(username);
		if (user == null) {
			throw new InvalidUserNameException("Invalid userName!");
		}
		if (user.getStatus() == null){
			user.setStatus(UserStatus.ACTIVE);
		}
		if (user.getStatus() == UserStatus.DISABLED){
			throw new DisabledAccountException("The account is disabled!");
		}
		if (user.getRole() == null){ //if no role was selected for the user, we assume the account is disabled 
			throw new DisabledAccountException("The account is disabled!");
		}
		if (user.getPassword().compareTo(generateMD5Password(password)) != 0){
			throw new InvalidPasswordException("Password is invalid!");
		}
		/*
		if (user != null){
			user.preLoadRights();
		}
		*/
		return user;
	}

	@SuppressWarnings("unchecked")
	public synchronized void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws PersistenceException, RegisterException {
		log.info("Trying to modify user with ID: " + idOfUser);
		if (idOfUser != null) {
			User user = entityManager.find(User.class, idOfUser);
			
			if (user == null) {
				throw new UserNotFoundException("User with id " + idOfUser.toString() + " does not exist in the DB!");
			}
			if (newUserData.getUserName().trim().compareTo("") == 0) {
				throw new NoUserNameException("No user name was specified!");
			}
			if (newUserData.getPassword().trim().compareTo("") == 0) {
				throw new NoPasswordException("No password was specified!");
			}
			
			List<User> listOfUsers = (List<User>)entityManager.createNamedQuery("User.findUserByUserName").setParameter("userName", newUserData.getUserName()).getResultList();
			if (listOfUsers.size() > 0 ) {
				for (Iterator<User> i=listOfUsers.iterator(); i.hasNext();){
					User cUser = i.next();
					if (cUser.getId().compareTo(idOfUser) != 0 ){
						throw new UserNameAlreadyExistsException("User with specified user name already exists!");
					}
				}
			}
			
			user.setUserName(newUserData.getUserName());
			if (modifyPassword){
				user.setPassword(generateMD5Password(newUserData.getPassword()));
			}
			user.setEmailAddress(newUserData.getEmailAddress());
			user.setHomePage(newUserData.getHomePage());
			user.setHideEmail(newUserData.isHideEmail());
			user.setFirstName(newUserData.getFirstName());
			user.setMiddleName(newUserData.getMiddleName());
			user.setLastName(newUserData.getLastName());
			user.setRole(newUserData.getRole());
			user.setStatus(newUserData.getStatus());
			entityManager.flush();
		} else {
			throw new RegisterException("Specified ID was NULL.");
		}
	}

	public synchronized void registerNewUser(String userName, String password, String emailAddress, String firstName, String lastName, String middleName, Role selectedRole) throws PersistenceException, RegisterException {
		log.info("Trying to register user name: " + userName);
		User user = findUser(userName);
		if (user != null) {
			throw new UserNameAlreadyExistsException("User with specified user name already exists!");
		}
		if (userName.trim().compareTo("") == 0) {
			throw new NoUserNameException("No user name was specified!");
		}
		if (password.trim().compareTo("") == 0) {
			throw new NoPasswordException("No password was specified!");
		}
		
		User newUser = new User(userName, UserStatus.ACTIVE, firstName, lastName, middleName, emailAddress, new Date());
		newUser.setPassword(generateMD5Password(password));
		newUser.setRole(selectedRole);
		
		entityManager.persist(newUser);
		entityManager.flush();
	}
	
	/**
	 * Generates an MD5 encrypted password
	 * 
	 * @param password
	 * @return
	 */
	private String generateMD5Password(String password){
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(password.getBytes(),0,password.length());
		} catch (NoSuchAlgorithmException e) {
		}
		
		return (new BigInteger(1, m.digest()).toString(16));
	}

}