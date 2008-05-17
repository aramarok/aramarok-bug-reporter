package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import com.drey.aramarok.domain.exceptions.search.NoSearchNameException;
import com.drey.aramarok.domain.exceptions.search.SearchException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserPreference;
import com.drey.aramarok.domain.model.UserStatus;
import com.drey.aramarok.domain.model.filters.UserFilter;

@Stateless
@Local(UserService.class)
public class UserServiceBean  implements UserService, Serializable {

	private static final long serialVersionUID = -6422997018931074102L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private static Logger log = Logger.getLogger(UserServiceBean.class);
	
	public synchronized User getUser(Long userId) throws PersistenceException {
		log.info("Get user with id:" + userId);
		try {
			return entityManager.find(User.class, userId);
		} catch (NoResultException e) {
			return null; 
		}
	}
	
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
	public synchronized void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws PersistenceException, UserException, RegisterException {
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
			user.setFirstName(newUserData.getFirstName());
			user.setMiddleName(newUserData.getMiddleName());
			user.setLastName(newUserData.getLastName());
			user.setRole(newUserData.getRole());
			user.setStatus(newUserData.getStatus());
			entityManager.flush();
		} else {
			throw new UserException("Specified ID was NULL.");
		}
	}

	public synchronized void modifyUserPreference(Long idOfUser, UserPreference newUserPreference)throws PersistenceException, UserException {
		log.info("Trying to modify users preference of user with id: " + idOfUser);
		if (idOfUser != null) {
			User user = getUser(idOfUser);
			if (user!=null){
				user.setUserPreference(newUserPreference);
				entityManager.flush();
			} else {
				throw new UserException("User with specified ID was not found in the DB.");
			}
		} else {
			throw new UserException("Specified ID was NULL.");
		}
	}
	
	public synchronized void registerNewUser(String userName, String password, String emailAddress, String homePage, String firstName, String lastName, String middleName, Role selectedRole) throws PersistenceException, RegisterException {
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
		newUser.setHomePage(homePage);
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
	
	public synchronized boolean addASavedSearchToUser(SavedSearch search, User user) throws PersistenceException, UserException, NoSearchNameException{
		log.info("Trying to save a search for user " + user.getUserName());
		boolean toReturn = false;
		
		if (search!=null && user!=null){
			user = findUser(user.getUserName());
			if (user == null){
				throw new UserException();
			}
			if (search.getName()==null || search.getName().trim().compareTo("")==0){
				throw new NoSearchNameException();
			}
			entityManager.persist(search);
			entityManager.flush();
			user.addASearch(search);
			toReturn = true;
		}
		
		return toReturn;
	}
	
	public synchronized boolean removeASavedSearchFromUser(SavedSearch search, User user)throws PersistenceException, UserException, SearchException{
		log.info("Trying to remove a saved search from user " + user.getUserName());
		boolean toReturn = false;
		
		if (search!=null && user!=null){
			user = findUser(user.getUserName());
			if (user == null){
				throw new UserException();
			}
			search = entityManager.find(SavedSearch.class, search.getId());
			if (search == null){
				throw new SearchException();
			}
			user.removeASearch(search);
			entityManager.flush();
			entityManager.remove(search);
			toReturn = true;
		}
		
		return toReturn;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<User> getUsers(UserFilter userFilter) throws PersistenceException{
		log.info("Get users request");
		
		List<User> results = new ArrayList<User>();
		Query query;
		
		String and = " AND ";
		StringBuffer queryStr = new StringBuffer("SELECT u from User u ");
		boolean and_needed = false;
		
		if (userFilter != null){
			queryStr.append(" WHERE ");
			final List<Long> userIdList = userFilter.getUserIdList();
			if (userIdList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("u.id IN (:userIdList)");
				and_needed = true;
			}
			final List<UserStatus> userStatusList = userFilter.getUserStatusList();
			if (userStatusList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("u.status IN (:userStatusList)");
				and_needed = true;
			}
			final List<String> userNameList = userFilter.getUserNameList();
			if (userNameList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("u.userName IN (:userNameList)");
				and_needed = true;
			}
			final List<Right> rightList = userFilter.getRightList();
									
			if (userFilter.getSortingMode() != null){
				switch (userFilter.getSortingMode()) {
					case ID_ASC:queryStr.append(" order by u.id asc");
								break;
					case ID_DESC:	queryStr.append(" order by u.id desc");
									break;
					case USER_NAME_ASC:	queryStr.append(" order by u.userName asc");
										break;
					case USER_NAME_DESC:queryStr.append(" order by u.userName desc");
										break;
					case FIRST_NAME_ASC:queryStr.append(" order by u.firstName asc");
										break;
					case FIRST_NAME_DESC: queryStr.append(" order by u.firstName desc");
										break;
					case LAST_NAME_ASC:	queryStr.append(" order by u.lastName asc");
										break;
					case LAST_NAME_DESC:queryStr.append(" order by u.lastName desc");
										break;
					case REGISTER_DATE_ASC: queryStr.append(" order by u.registerDate asc");
											break;
					case REGISTER_DATE_DESC:queryStr.append(" order by u.registerDate desc");
											break;
					default: queryStr.append(" order by b.id asc");
				}
			}
		
			log.info(queryStr.toString());
			query = entityManager.createQuery(queryStr.toString());
						
			if (query != null){
				if (userIdList != null){
					query.setParameter("userIdList", userIdList);
				}
				if (userStatusList != null){
					query.setParameter("userStatusList", userStatusList);
				}
				if (userNameList != null){
					query.setParameter("userNameList", userNameList);
				}
				try {
					results = query.getResultList();
					List<User> results2 = new ArrayList<User>();
					if (rightList!=null){
						for (User u: results){
							for (Right r: rightList){
								if (u.hasRight(r)){
									results2.add(u);
								}
							}
						}
					}
					results = results2;
				} catch (NoResultException ex) {
					
				} catch(PersistenceException ex) {
					log.error("PersistenceException", ex);
					throw new PersistenceException();
				}
			}
		} else {
			log.info(queryStr.toString());
			query = entityManager.createQuery(queryStr.toString());
			try {
				results = query.getResultList();				
			} catch (NoResultException ex) {
				
			} catch(PersistenceException ex) {
				log.error("PersistenceException", ex);
				throw new PersistenceException();
			}
		}
		return results;
	}
}