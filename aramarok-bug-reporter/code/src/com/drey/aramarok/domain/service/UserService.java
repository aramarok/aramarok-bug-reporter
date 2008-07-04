package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.util.List;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.search.NoSearchNameException;
import com.drey.aramarok.domain.exceptions.search.SearchException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserPreference;
import com.drey.aramarok.domain.model.filters.UserFilter;

public interface UserService {

	/**
	 * 
	 * @param userId
	 * @param noLog
	 * @return
	 * @throws PersistenceException
	 */
	public User getUser(Long userId, boolean noLog) throws PersistenceException;
	
	
	/**
	 * 
	 * @param userName
	 * @param noLog
	 * @return
	 * @throws ExternalSystemException
	 */
	public User findUser(String userName, boolean noLog) throws PersistenceException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<User> getAllUsers() throws PersistenceException;
	
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws FatalDomainException
	 * @throws LoginException
	 */
	public User login(String username, String password) throws LoginException;
	
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @param emailAddress
	 * @param homePage
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @param selectedRole
	 * @throws FatalDomainException
	 * @throws RegisterException
	 */
	public void registerNewUser(String userName, String password, String emailAddress, String homePage, String firstName, String lastName, String middleName, Role selectedRole) throws PersistenceException, RegisterException ;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserData
	 * @param modifyPassword
	 * @throws FatalDomainException
	 * @throws RegisterException
	 * @throws UserException 
	 */
	public void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws PersistenceException, RegisterException, UserException;
	
	
	/**
	 * 
	 * @param search
	 * @param user
	 * @return
	 * @throws PersistenceException
	 * @throws UserException
	 * @throws NoSearchNameException
	 */
	public boolean addASavedSearchToUser(SavedSearch search, User user) throws PersistenceException, UserException, NoSearchNameException;
	
	
	/**
	 * 
	 * @param search
	 * @param user
	 * @return
	 * @throws PersistenceException
	 * @throws UserException
	 * @throws SearchException
	 */
	public boolean removeASavedSearchFromUser(SavedSearch search, User user) throws PersistenceException, UserException, SearchException;
	
	
	/**
	 * 
	 * @param userFilter
	 * @return
	 */
	public List<User> getUsers(UserFilter userFilter) throws PersistenceException;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserPreference
	 * @throws PersistenceException
	 * @throws UserException
	 */
	public void modifyUserPreference(Long idOfUser, UserPreference newUserPreference)throws PersistenceException, UserException;

}