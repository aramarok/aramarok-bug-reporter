package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.util.List;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.quip.QuipException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.search.NoSearchNameException;
import com.drey.aramarok.domain.exceptions.search.SearchException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.user.UserHasNoRightException;
import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Quip;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserPreference;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.model.filters.CommentFilter;
import com.drey.aramarok.domain.model.filters.UserFilter;

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
	 * @param noLog
	 * @return
	 * @throws ExternalSystemException
	 */
	public User getUser(String userName, boolean noLog) throws ExternalSystemException;
	
	
	/**
	 * Returns a list of users matching the properties set to the user filter
	 * 
	 * @param userFilter
	 * @return
	 * @throws ExternalSystemException 
	 */
	public List<User> getUsers(UserFilter userFilter) throws ExternalSystemException;
	
	
	/**
	 * Registers a new user into the system
	 * 
	 * @param userName
	 * @param password
	 * @param emailAddress
	 * @param homePage
	 * @param firstName
	 * @param lastName
	 * @param middleName
	 * @throws FatalDomainException
	 * @throws RegisterException
	 */
	public void registerNewUser(String userName, String password, String emailAddress, String homePage, String firstName, String lastName, String middleName) throws ExternalSystemException, RegisterException;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserData
	 * @param modifyPassword
	 * @throws FatalDomainException
	 * @throws RegisterException
	 * @throws UserException 
	 */
	public void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws ExternalSystemException, RegisterException, UserException;
	
	
	/**
	 * 
	 * @param idOfUser
	 * @param newUserPreference
	 * @throws ExternalSystemException
	 * @throws UserException
	 */
	public void modifyUserPreference(Long idOfUser, UserPreference newUserPreference)throws ExternalSystemException, UserException;
	
	
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
	 * @throws ExternalSystemException
	 */
	public Product getProduct(String productName) throws ExternalSystemException;
	
	

	public void addNewProduct(String productName, String productDescription, String productURL, boolean closeForBugEntry, User userAssigned, List<ProductComponent> productComponents) throws ExternalSystemException, ProductException, UserException;
	
	

	public void modifyProduct(Long idOfProduct, Product newProductData) throws ExternalSystemException, ProductException, UserException;
	
	
	
	public void addNewProductComponent(String productComponentName, String productComponentDescription, User userAssigned, /*Product parentProduct,*/ List<ComponentVersion> componentVersions) throws ExternalSystemException, ProductComponentException, UserHasNoRightException;
	
	
	public Long commitBug(Bug bug) throws ExternalSystemException, BugException, UserException;
	
	
	/**
	 * 
	 * @param bugId
	 * @return
	 * @throws ExternalSystemException 
	 */
	public Bug getBug(Long bugId) throws ExternalSystemException;
	
	
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
	 * @throws ExternalSystemException 
	 */
	public List<Bug> getBugs(BugFilter bugFilter) throws ExternalSystemException;
	
	
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
	 * @throws SearchException 
	 */
	public void addASavedBugFilterToUser(Long idOfUser, SavedSearch savedSearch) throws UserNotFoundException, ExternalSystemException, SearchException;
	
	public SavedSearch getSavedSearch(Long savedSearchId) throws ExternalSystemException;
	
	public void addNewComponentVersion(String componentVersionName, String componentVersionDescription, /*ProductComponent parentProductComponent,*/ User userAssignedTo) throws ExternalSystemException, ComponentVersionException ,UserHasNoRightException;

	
	public void modifyComponentVersion(Long idOfComponentVersion, ComponentVersion newComponentVersionData) throws ExternalSystemException, ComponentVersionException, UserHasNoRightException;

	
	public void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws ExternalSystemException, ProductComponentException, UserHasNoRightException;
	
	
	/**
	 * 
	 * @param componentName
	 * @return
	 * @throws ExternalSystemException
	 */
	public ProductComponent getProductComponent(String componentName) throws ExternalSystemException;
	
	
	/**
	 * 
	 * @param componentVersionName
	 * @return
	 * @throws ExternalSystemException
	 */
	public ComponentVersion getComponentVersion(String componentVersionName) throws ExternalSystemException;
	
	
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
	
	
	/**
	 * 
	 * @param search
	 * @return
	 * @throws ExternalSystemException
	 * @throws UserException
	 * @throws NoSearchNameException
	 */
	public boolean addASavedSearch(SavedSearch search) throws ExternalSystemException, UserException, NoSearchNameException;
	
	
	/**
	 * 
	 * @param search
	 * @return
	 * @throws ExternalSystemException
	 * @throws UserException
	 * @throws SearchException
	 */
	public boolean removeASavedSearch(SavedSearch search)throws ExternalSystemException, UserException, SearchException;
	
	
	/**
	 * Returns the user witch will be assigned to the for the selected chain of version, component, product.
	 * 
	 * @param componentVersionId
	 * @param productComponentId
	 * @param productId
	 * @return
	 * @throws ExternalSystemException
	 */
	public User getUserAssignedForSubmittingBug(Long componentVersionId, Long productComponentId, Long productId) throws ExternalSystemException;
	
	
	public Product getProduct(Long productId) throws ExternalSystemException;
	
	
	public List<Product> getProductsForCommittingABug() throws ExternalSystemException;
	
	
	public Product getProductForProductComponent(Long productComponentId) throws ExternalSystemException;
	
	
	public ProductComponent getProductComponent(Long productComponentId) throws ExternalSystemException;
	
	
	public ProductComponent getProductComponentForComponentVersion(Long componentVersionId) throws ExternalSystemException;
	
	
	public ComponentVersion getComponentVersion(Long componentVersionId) throws ExternalSystemException;
	
	
	public Comment getComment(Long commentId) throws ExternalSystemException;
	
	public Quip getQuip(Long quipId) throws ExternalSystemException;
	
	public void addQuip(String quipText) throws ExternalSystemException, QuipException;
	
	public void editQuip(Long quipId, String newQuipText, boolean visible) throws ExternalSystemException, UserHasNoRightException;
	
	public void approveQuip(Long quipId) throws ExternalSystemException, UserHasNoRightException;
	
	public void voteComment(Long commentId) throws ExternalSystemException;
	
	public List<ProductComponent> getUnusedProductComponents() throws ExternalSystemException;
	
	public List<ComponentVersion> getUnusedComponentVersions() throws ExternalSystemException;
	
}