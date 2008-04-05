package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.util.List;
import java.util.Set;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.model.filters.CommentFilter;

public interface DomainFacade {
	
	
	/**
	 * Calling this function signals the container to remove this bean instance
	 * and terminates the session.
	 */
	public void stopSessionBean();
	
	
	/**
	 * Authenticates the user and binds the User instance to the context of
	 * this stateful session bean. All other methods will use the bound User
	 * instance for authorizing operations.
	 * 
	 * @param username
	 * @param password
	 * @return an instance of User, with roles & rights collections initialized.
	 */
	public User login(String userName, String password) throws ExternalSystemException, LoginException;
	
	
	/**
	 * Gets User by userName
	 * 
	 * @param userName
	 * @return
	 */
	public User getUser(String userName);
	
	
	/**
	 * Registers a new user into the system
	 * 
	 * @param userName
	 * @param password
	 * @param emailAddress
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @throws FatalDomainException
	 * @throws RegisterException
	 */
	public void registerNewUser(String userName, String password, String emailAddress, String firstName, String lastName, String middleName) throws ExternalSystemException, RegisterException;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserData
	 * @param modifyPassword
	 * @throws FatalDomainException
	 * @throws RegisterException
	 */
	public void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws ExternalSystemException, RegisterException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<Role> getAllRoles() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<User> getAllUsers() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<Product> getAllProducts() throws ExternalSystemException;	
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<Severity> getAllSeverities() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<Priority> getAllPriorities() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<Platform> getAllPlatforms() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<OperatingSystem> getAllOperatingSystems() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @param productName
	 * @return
	 */
	public Product getProduct(String productName);
	
	

	public void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws ExternalSystemException, ProductException;
	
	

	public void modifyProduct(Long idOfProduct, Product newProductData) throws ExternalSystemException, ProductException;
	
	
	
	public void addNewProductComponent(String productComponentName, String productComponentDescription, Product parentProduct, List<ComponentVersion> componentVersions) throws ExternalSystemException, ProductComponentException;
	
	
	public Long commitBug(Bug bug) throws ExternalSystemException, BugException, UserException;
	
	
	/**
	 * 
	 * @param bugId
	 * @return
	 */
	public Bug getBug(Long bugId);
	
	
	/**
	 * 
	 * @param commentFilter
	 * @return
	 */
	public List<Comment> getComments(CommentFilter commentFilter);
	
	
	/**
	 * 
	 * @param bugFilter
	 * @return
	 */
	public List<Bug> getBugs(BugFilter bugFilter);
	
	
	/**
	 * 
	 * @param priorityName
	 * @return
	 */
	public Priority getPriority(String priorityName);
	
	
	/**
	 * 
	 * @param severityName
	 * @return
	 */
	public Severity getSeverity(String severityName);
	
	
	/**
	 * 
	 * @param operatingSystemName
	 * @return
	 */
	public OperatingSystem getOperatingSystem(String operatingSystemName);
	
	
	/**
	 * 
	 * @param platformName
	 * @return
	 */
	public Platform getPlatform(String platformName);
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param savedSearch
	 * @throws UserNotFoundException
	 * @throws ExternalSystemException
	 */
	public void addASavedBugFilterToUser(Long idOfUser, SavedSearch savedSearch) throws UserNotFoundException, ExternalSystemException;
	
	

	public void addNewComponentVersion(String componentVersionName, String componentVersionDescription, ProductComponent parentProductComponent, User userAssignedTo) throws ExternalSystemException, ComponentVersionException;

	
	public void modifyComponentVersion(Long idOfComponentVersion, ComponentVersion newComponentVersionData) throws ExternalSystemException, ComponentVersionException;

	
	public void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws ExternalSystemException, ProductComponentException;
	
	
	/**
	 * 
	 * @param componentName
	 * @return
	 */
	public ProductComponent getProductComponent(String componentName);
	
	
	/**
	 * 
	 * @param versionName
	 * @return
	 */
	public ComponentVersion getComponentVersion(String componentVersionName);
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<ProductComponent> getAllProductComponents() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<ComponentVersion> getAllComponentVersions() throws ExternalSystemException;
}