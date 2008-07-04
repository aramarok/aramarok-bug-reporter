package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
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
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserPreference;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.model.filters.CommentFilter;
import com.drey.aramarok.domain.model.filters.UserFilter;

@Stateful
@Local(DomainFacade.class)
@Remote(DomainFacade.class)
public class DomainFacadeBean implements DomainFacade, Serializable {
	
	private static final long serialVersionUID = 1234567890L;
	
	private static final String DB_ERROR_MSG = "A database error has occured.";

	private static Logger log = Logger.getLogger(DomainFacadeBean.class);

	private static Logger aramarokLog = Logger.getLogger("AramarokLog");
	
	@PersistenceContext(unitName = "Aramarok", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	private User user = null;

	@EJB
	private DomainService domainService;
	
	@EJB
	private UserService userService;
	
	@EJB
	private BugService bugService;
	
	@EJB
	private ProductService productService;
	
	@EJB
	private ProductComponentService componentService;
	
	@EJB
	private ComponentVersionService versionService;
	
	@Remove
	public synchronized void stopSessionBean() {
		aramarokLog.info("User logged off: " + user.getUserName());
	}
	
	public synchronized User login(String username, String password) throws ExternalSystemException, LoginException {
		try {
			this.user = userService.login(username, password);
			log.info("User found: " + user.getName());
			aramarokLog.info("User logged in: " + user.getUserName());
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
		return user;
	}
	
	public synchronized User getUser(String userName, boolean noLog) throws ExternalSystemException{
		try {
			return userService.findUser(userName, noLog);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized List<User> getUsers(UserFilter userFilter) throws ExternalSystemException{
		try {
			return userService.getUsers(userFilter);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized void registerNewUser(String userName, String password, String emailAddress, String homePage, String firstName, String lastName, String middleName) throws ExternalSystemException, RegisterException {
		try {
			String roleName = "Guest";
			List<Role> roleList = getAllRoles();
			Role selectedRole = null;
			for (Role r : roleList) {
				if (r.getName().compareTo(roleName) == 0)
					selectedRole = r;
			}
			userService.registerNewUser(userName, password, emailAddress, homePage, firstName, lastName, middleName, selectedRole);
			aramarokLog.info("User name '" + userName + "' was registered.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws ExternalSystemException, RegisterException, UserException {
		try {
			userService.modifyUser(idOfUser, newUserData, modifyPassword);
			aramarokLog.info("User with user name '" + newUserData.getUserName() + "' was modified.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized void modifyUserPreference(Long idOfUser, UserPreference newUserPreference)throws ExternalSystemException, UserException{
		try {
			userService.modifyUserPreference(idOfUser, newUserPreference);
			aramarokLog.info("User preference of user with id '" + idOfUser + "' was modified.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized Product getProduct(String productName)throws ExternalSystemException{
		try {
			return productService.findProduct(productName);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized ProductComponent getProductComponent(String productComponentName)throws ExternalSystemException{
		try{
			return componentService.findProductComponent(productComponentName);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized ComponentVersion getComponentVersion(String componentVersionName)throws ExternalSystemException{
		try{
			return versionService.findComponentVersion(componentVersionName);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public synchronized void addNewProduct(String productName, String productDescription, String productURL, boolean closeForBugEntry, User userAssigned, List<ProductComponent> productComponents) throws ExternalSystemException, ProductException, UserException{
		if (this.user.hasRight(Right.DEFINE_PRODUCTS)){
			try {
				productService.addNewProduct(productName, productDescription, productURL, closeForBugEntry, userAssigned, productComponents);
				aramarokLog.info("New product added: '" + productName + "'.");
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("User cannot add a new product. Does not have DEFINE_PRODUCTS right.");
			throw new UserHasNoRightException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyProduct(Long idOfProduct, Product newProductData) throws ExternalSystemException, ProductException, UserException {
		if (this.user.hasRight(Right.DEFINE_PRODUCTS)){
			try {
				productService.modifyProduct(idOfProduct, newProductData);
				aramarokLog.info("Product with id '" + idOfProduct + "' was modified.");
				// aramarokLog.info(domainService.currentDateAndTime() + " - Product with name '" + product.getName() + "' was modified."); 
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("User cannot modify an exiting product. Does not have DEFINE_PRODUCTS right.");
			throw new UserHasNoRightException();
		}
	}
	
	public synchronized void addNewProductComponent(String productComponentName, String productComponentDescription, User userAssigned, /*Product parentProduct,*/ List<ComponentVersion> componentVersions) throws ExternalSystemException, ProductComponentException, UserHasNoRightException {
		if (this.user.hasRight(Right.DEFINE_COMPONENTS)){
			/*if (parentProduct != null) {
				Product product = productService.findProduct(parentProduct.getName());
				if (product == null){
					log.error("The 'parentProduct' was not found in the DB!");
					throw new ProductComponentException("The 'parentProduct' was not found in the DB!");
				}
				
				ProductComponent newComponent = null;*/
			try {
				componentService.addNewProductComponent(productComponentName, productComponentDescription, userAssigned, componentVersions);
				aramarokLog.info("New product component added: '" + productComponentName + "'.");
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
				
				/*
				// here we add the new product component to the specified product
				product.addComponent(newComponent);
				try {
					entityManager.flush();
					//TODO: is this correct?
				} catch (PersistenceException ex) {
					throw new ExternalSystemException(DB_ERROR_MSG, ex);
				}
			} else {
				log.error("The 'parentProduct' parrameter was NULL!");
				throw new ProductComponentException("The 'parentProduct' parrameter was NULL!");
			}*/
		} else {
			log.error("User cannot add a new product component. Does not have DEFINE_COMPONENTS right.");
			throw new UserHasNoRightException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws ExternalSystemException, ProductComponentException, UserHasNoRightException {
		if (this.user.hasRight(Right.DEFINE_COMPONENTS)){
			try {
				componentService.modifyProductComponent(idOfProductComponent, newProductComponentData);
				aramarokLog.info("Product component with id '" + idOfProductComponent + "' was modified.");
			} catch (PersistenceException pe){
				throw new ExternalSystemException(DB_ERROR_MSG, pe);
			}
		} else {
			log.error("User cannot modify an exiting product component. Does not have DEFINE_COMPONENTS right.");
			throw new UserHasNoRightException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyComponentVersion(Long idOfComponentVersion, ComponentVersion newComponentVersionData) throws ExternalSystemException, ComponentVersionException, UserHasNoRightException {
		if (this.user.hasRight(Right.DEFINE_VERSIONS)){
			try {
				versionService.modifyComponentVersion(idOfComponentVersion, newComponentVersionData);
				aramarokLog.info("Component version with id '" + idOfComponentVersion + "' was modified.");
			} catch (PersistenceException pe){
				throw new ExternalSystemException(DB_ERROR_MSG, pe);
			}
		} else {
			log.error("User cannot modify an exiting component version. Does not have DEFINE_VERSIONS right.");
			throw new UserHasNoRightException();
		}
	}
	
	public synchronized void addNewComponentVersion(String componentVersionName, String componentVersionDescription, /*ProductComponent parentProductComponent,*/ User userAssignedTo) throws ExternalSystemException, ComponentVersionException ,UserHasNoRightException {
		if (this.user.hasRight(Right.DEFINE_VERSIONS)){
		/*
		if (parentProductComponent != null){
			ProductComponent component = componentService.findProductComponent(parentProductComponent.getName());
			if (component == null){
				log.error("The 'parentProductComponent' was not found in the DB!");
				throw new ComponentVersionException("The 'parentProductComponent' was not found in the DB!");
			}
			ComponentVersion newComponentVersion = null;
		*/
			
			try {
				versionService.addNewComponentVersion(componentVersionName, componentVersionDescription, userAssignedTo);
				aramarokLog.info("New component version added: '" + componentVersionName + "'.");
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		/*	
			// here we add the new version to the specified component
			component.addVersion(newComponentVersion);
			try {
				entityManager.flush();
				//TODO: is correct here?
			} catch (PersistenceException pe){
				throw new ExternalSystemException(DB_ERROR_MSG, pe);
			}
		} else {
			log.error("The 'parentProductComponent' parrameter was NULL!");
			throw new ComponentVersionException("The 'parentProductComponent' parrameter was NULL!");
		}
		*/
		} else {
			log.error("User cannot add a new component version. Does not have DEFINE_VERSIONS right.");
			throw new UserHasNoRightException();
		}
	}
	
	public synchronized Long commitBug(Bug bug) throws ExternalSystemException, BugException, UserException {
		log.info("Trying to commit a bug.");
		
		if (bug == null)
			throw new BugException();
		
		try {
			if (bug.getId() != null){ //means that a bug is being modified
				Long bugId = bugService.commitBug(bug, this.user);
				aramarokLog.info("Bug " + bugId + " was committed by user '" + this.user.getUserName() + "'.");
				return bugId;
			} else { //means that a new bug is being committed
				if (!user.hasRight(Right.ENTER_NEW_BUG)){
					log.error("User does not have the 'ENTER_NEW_BUG' right");
					throw new UserHasNoRightException();
				}
				Set<User> ccUsers = new HashSet<User>();
				if (bug.getCcUsers()!=null && !bug.getCcUsers().isEmpty()){
					for (User u: bug.getCcUsers()){
						ccUsers.add(getUser(u.getUserName(), false));
					}
				}
				Long newBugId = bugService.commitNewBug(bug, this.user, ccUsers);
				aramarokLog.info("New bug committed by user '" + this.user.getUserName() + "'.");
				return newBugId;
			}
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized Bug getBug(Long bugId) throws ExternalSystemException {
		try {
			return bugService.getBug(bugId);
		} catch (PersistenceException pe){
			throw new ExternalSystemException(DB_ERROR_MSG, pe);
		}
	}
	
	public synchronized List<Bug> getBugs(BugFilter bugFilter) throws ExternalSystemException {
		try {
			return bugService.getBugs(bugFilter);
		} catch (PersistenceException pe){
			throw new ExternalSystemException(DB_ERROR_MSG, pe);
		}
	}
	
	public synchronized List<Comment> getComments(CommentFilter commentFilter){
		return domainService.getComments(commentFilter);
	}
	
	public synchronized Priority getPriority(String priorityName){
		return domainService.getPriority(priorityName);
	}
	
	public Severity getSeverity(String severityName){
		return domainService.getSeverity(severityName);
	}
	
	public OperatingSystem getOperatingSystem(String operatingSystemName){
		return domainService.getOperatingSystem(operatingSystemName);
	}
	
	public Platform getPlatform(String platformName){
		return domainService.getPlatform(platformName);
	}
	
	public synchronized void addASavedBugFilterToUser(Long idOfUser, SavedSearch savedSearch) throws UserNotFoundException, ExternalSystemException, SearchException{
		log.info("Trying to add a saved bug filter to user with ID: " + idOfUser);
		if (idOfUser != null) {
			try {
				try {
					SavedSearch exists = (SavedSearch)entityManager.createNamedQuery("SavedSearch.findSavedSearchByName").setParameter("name", savedSearch.getName()).getSingleResult();
					if (exists!=null){
						throw new SearchException();
					}
				} catch (NoResultException nre){
					
				} catch (NonUniqueResultException nure){
					throw new SearchException();
				}
			    
				User user = entityManager.find(User.class, idOfUser);
				
				if (user == null) {
					throw new UserNotFoundException("User with id " + idOfUser.toString() + " does not exist in the DB!");
				}
				
				Set<User> ownerList = new HashSet<User>();
				Set<User> userAssignedToList = new HashSet<User>();
				Set<Priority> priorityList = new HashSet<Priority>();
				Set<Severity> severityList = new HashSet<Severity>();
				Set<OperatingSystem> operatingSystemList =  new HashSet<OperatingSystem>();
				Set<Platform> platformList =  new HashSet<Platform>();
				
				if (savedSearch.getOperatingSystemList()!=null)
					for (OperatingSystem os: savedSearch.getOperatingSystemList()){
						operatingSystemList.add(getOperatingSystem(os.getName()));
					}
				if (savedSearch.getOwnerList()!=null)
					for (User o: savedSearch.getOwnerList()){
						ownerList.add(getUser(o.getUserName(), true));
					}
				if (savedSearch.getUserAssignedToList()!=null)
					for (User o: savedSearch.getUserAssignedToList()){
						userAssignedToList.add(getUser(o.getUserName(), true));
					}
				if (savedSearch.getPlatformList()!=null)
					for (Platform p: savedSearch.getPlatformList()){
						platformList.add(getPlatform(p.getName()));
					}
				if (savedSearch.getPriorityList()!=null)
					for (Priority p: savedSearch.getPriorityList()){
						priorityList.add(getPriority(p.getName()));
					}
				if (savedSearch.getSeverityList()!=null)
					for (Severity s: savedSearch.getSeverityList()){
						severityList.add(getSeverity(s.getName()));
					}
				
				/*if (savedSearch.getBugIdList()!=null)
					for (OperatingSystem os: savedSearch.getOperatingSystemList()){
						operatingSystemList.add(getOperatingSystem(os.getName()));
					}
				*/
				
				savedSearch.setOwnerList(ownerList);
				savedSearch.setUserAssignedToList(userAssignedToList);
				savedSearch.setPriorityList(priorityList);
				savedSearch.setSeverityList(severityList);
				savedSearch.setOperatingSystemList(operatingSystemList);
				savedSearch.setPlatformList(platformList);
				
				
				entityManager.persist(savedSearch);
				SavedSearch ssFromDb = (SavedSearch)entityManager.createNamedQuery("SavedSearch.findSavedSearchByDate").setParameter("savedDate", savedSearch.getSavedDate()).getSingleResult();
				user.addASearch(ssFromDb);
				entityManager.flush();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} 
	}
	
	public List<Role> getAllRoles() throws ExternalSystemException{
		try {
			return domainService.getAllRoles();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<User> getAllUsers() throws ExternalSystemException{
		try {
			return userService.getAllUsers();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<Product> getAllProducts() throws ExternalSystemException{
		try{
			return productService.getAllProducts();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<ProductComponent> getAllProductComponents() throws ExternalSystemException{
		try {
			return componentService.getAllProductComponents();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<ComponentVersion> getAllComponentVersions() throws ExternalSystemException{
		try{
			return versionService.getAllComponentVersions();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<OperatingSystem> getAllOperatingSystems() throws ExternalSystemException{
		try {
			return domainService.getAllOperatingSystems();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<Platform> getAllPlatforms() throws ExternalSystemException{
		try {
			return domainService.getAllPlatforms();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<Priority> getAllPriorities() throws ExternalSystemException{
		try {
			return domainService.getAllPriorities();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<Severity> getAllSeverities() throws ExternalSystemException{
		try {
			return domainService.getAllSeverities();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public boolean addASavedSearch(SavedSearch search) throws ExternalSystemException, UserException, NoSearchNameException{
		try {
			return userService.addASavedSearchToUser(search, this.user);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public boolean removeASavedSearch(SavedSearch search)throws ExternalSystemException, UserException, SearchException{
		try {
			return userService.removeASavedSearchFromUser(search, this.user);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized SavedSearch getSavedSearch(Long savedSearchId) throws ExternalSystemException {
		try {
			return entityManager.find(SavedSearch.class, savedSearchId);
		} catch (PersistenceException pe){
			throw new ExternalSystemException(DB_ERROR_MSG, pe);
		}
	}

	public ComponentVersion getComponentVersion(Long componentVersionId) throws ExternalSystemException {
		try {
			return versionService.getComponentVersion(componentVersionId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public Product getProduct(Long productId) throws ExternalSystemException {
		try {
			return productService.getProduct(productId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public ProductComponent getProductComponent(Long productComponentId) throws ExternalSystemException {
		try {
			return componentService.getProductComponent(productComponentId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public ProductComponent getProductComponentForComponentVersion(Long componentVersionId) throws ExternalSystemException {
		try {
			return componentService.getProductComponentForComponentVersion(componentVersionId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public Product getProductForProductComponent(Long productComponentId) throws ExternalSystemException {
		try {
			return productService.getProductForProductComponent(productComponentId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public List<Product> getProductsForCommittingABug() throws ExternalSystemException {
		try {
			return productService.getProductsForCommittingABug();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}

	public User getUserAssignedForSubmittingBug(Long componentVersionId, Long productComponentId, Long productId) throws ExternalSystemException {
		if (componentVersionId!=null){
			return versionService.getUserAssignedForSubmittingBug(componentVersionId);
		}
		if (productComponentId!=null){
			return componentService.getUserAssignedForSubmittingBug(productComponentId);
		}
		if (productId!=null){
			return productService.getUserAssignedToProduct(productId);
		}
		return null;
	}
	
	public Comment getComment(Long commentId) throws ExternalSystemException {
		try {
			return domainService.getComment(commentId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public Quip getQuip(Long quipId) throws ExternalSystemException {
		try {
			return domainService.getQuip(quipId);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public void addQuip(String quipText) throws ExternalSystemException, QuipException {
		try {
			domainService.addQuip(quipText, this.user);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public void editQuip(Long quipId, String newQuipText, boolean visible) throws ExternalSystemException, UserHasNoRightException{
		if (this.user.hasRight(Right.EDIT_QUIPS)){
			try {
				domainService.editQuip(quipId, newQuipText, visible);
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("User cannot edit quips. Does not have EDIT_QUIPS right.");
			throw new UserHasNoRightException();
		}
	}
	
	public void approveQuip(Long quipId) throws ExternalSystemException, UserHasNoRightException{
		if (this.user.hasRight(Right.EDIT_QUIPS)){
			try {
				domainService.approveQuip(quipId);
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("User cannot approve quips. Does not have EDIT_QUIPS right.");
			throw new UserHasNoRightException();
		}
	}
	
	public void voteComment(Long commentId) throws ExternalSystemException {
		log.info("Votting comment with id: "+ commentId);
		try {
			domainService.voteComment(commentId, this.user);
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<ProductComponent> getUnusedProductComponents() throws ExternalSystemException {
		try {
			return componentService.getUnusedProductComponents();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public List<ComponentVersion> getUnusedComponentVersions() throws ExternalSystemException {
		try {
			return versionService.getUnusedComponentVersions();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
}