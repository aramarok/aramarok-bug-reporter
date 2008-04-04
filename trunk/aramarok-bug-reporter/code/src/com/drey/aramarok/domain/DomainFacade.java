package com.drey.aramarok.domain;

import java.util.List;
import java.util.Set;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.component.ComponentException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.version.VersionException;

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
	public User login(String userName, String password) throws FatalDomainException, LoginException;
	
	
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
	public void registerNewUser(String userName, String password, String emailAddress, String firstName, String lastName, String middleName) throws FatalDomainException, RegisterException;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserData
	 * @throws FatalDomainException
	 * @throws RegisterException
	 */
	public void modifyUser(Long idOfUser, User newUserData) throws FatalDomainException, RegisterException;
	
	
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
	
	
	/**
	 * 
	 * @param productName
	 * @param productDescription
	 * @param productComponents
	 * @throws FatalDomainException
	 * @throws ProductException
	 */
	public void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws FatalDomainException, ProductException;
	
	
	/**
	 * 
	 * @param idOfProduct
	 * @param newProductData
	 * @throws FatalDomainException
	 * @throws ProductException
	 */
	public void modifyProduct(Long idOfProduct, Product newProductData) throws FatalDomainException, ProductException;
	
	
	/**
	 * 
	 * @param componentName
	 * @param componentDescription
	 * @param parentProduct
	 * @param componentVersions
	 * @throws FatalDomainException
	 * @throws ComponentException
	 */
	public void addNewProductComponent(String componentName, String componentDescription, Product parentProduct, Set<ComponentVersion> componentVersions) throws FatalDomainException, ComponentException;
	
	
	/**
	 * 
	 * @param bug
	 * @throws FatalDomainException
	 * @throws BugException
	 * @throws UserException
	 */
	public Long commitBug(Bug bug) throws FatalDomainException, BugException, UserException;
	
	
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
	
	
	/**
	 * 
	 * @param versionName
	 * @param versionDescription
	 * @param parentComponent
	 * @param userAssignedTo
	 * @throws FatalDomainException
	 * @throws VersionException
	 */
	public void addNewComponentVersion(String versionName, String versionDescription, ProductComponent parentComponent, User userAssignedTo) throws FatalDomainException, VersionException;

	
	/**
	 * 
	 * @param idOfVersion
	 * @param newVersionData
	 * @throws FatalDomainException
	 * @throws VersionException
	 */
	public void modifyVersion(Long idOfVersion, ComponentVersion newVersionData) throws FatalDomainException, VersionException;

	
	/**
	 * 
	 * @param idOfComponent
	 * @param newComponentData
	 * @throws FatalDomainException
	 * @throws ComponentException
	 */
	public void modifyComponent(Long idOfComponent, ProductComponent newComponentData) throws FatalDomainException, ComponentException;
	
	
	/**
	 * 
	 * @param componentName
	 * @return
	 */
	public ProductComponent getComponent(String componentName);
	
	
	/**
	 * 
	 * @param versionName
	 * @return
	 */
	public ComponentVersion getVersion(String versionName);
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<ProductComponent> getAllComponents() throws ExternalSystemException;
	
	
	/**
	 * 
	 * @return
	 * @throws ExternalSystemException
	 */
	public List<ComponentVersion> getAllVersions() throws ExternalSystemException;
}