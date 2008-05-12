package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.bug.BugStatusChangeException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.user.UserHasNoRightException;
import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.SavedSearch;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.BugFilter;
import com.drey.aramarok.domain.model.filters.CommentFilter;

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
			user = userService.login(username, password);
			log.info("User found: " + user.getName());
			aramarokLog.info("User logged in: " + user.getUserName());
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
		return user;
	}
	
	public synchronized User getUser(String userName){
		return userService.findUser(userName);
	}
	
	public synchronized void registerNewUser(String userName, String password, String emailAddress, String firstName, String lastName, String middleName) throws ExternalSystemException, RegisterException {
		try {
			String roleName = "Guest";
			List<Role> roleList = getAllRoles();
			Role selectedRole = null;
			for (Role r : roleList) {
				if (r.getName().compareTo(roleName) == 0)
					selectedRole = r;
			}
			userService.registerNewUser(userName, password, emailAddress, firstName, lastName, middleName, selectedRole);
			aramarokLog.info("User name '" + userName + "' was registered.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyUser(Long idOfUser, User newUserData, boolean modifyPassword) throws ExternalSystemException, RegisterException {
		try {
			userService.modifyUser(idOfUser, newUserData, modifyPassword);
			aramarokLog.info("User with user name '" + newUserData.getUserName() + "' was modified.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized Product getProduct(String productName){
		return productService.findProduct(productName);
	}
	
	public synchronized ProductComponent getProductComponent(String productComponentName){
		return componentService.findProductComponent(productComponentName);
	}
	
	public synchronized ComponentVersion getComponentVersion(String componentVersionName){
		return versionService.findComponentVersion(componentVersionName);
	}
	
	public synchronized void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws ExternalSystemException, ProductException {
		try {
			productService.addNewProduct(productName, productDescription, productComponents);
			aramarokLog.info("New product added: '" + productName + "'.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyProduct(Long idOfProduct, Product newProductData) throws ExternalSystemException, ProductException {
		try {
			productService.modifyProduct(idOfProduct, newProductData);
			aramarokLog.info("Product with name '" + newProductData.getName() + "' was modified.");
			// aramarokLog.info(domainService.currentDateAndTime() + " - Product with name '" + product.getName() + "' was modified."); 
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	public synchronized void addNewProductComponent(String productComponentName, String productComponentDescription, Product parentProduct, List<ComponentVersion> componentVersions) throws ExternalSystemException, ProductComponentException {
		if (parentProduct != null) {
			Product product = productService.findProduct(parentProduct.getName());
			if (product == null){
				log.error("The 'parentProduct' was not found in the DB!");
				throw new ProductComponentException("The 'parentProduct' was not found in the DB!");
			}
			
			ProductComponent newComponent = null;
			try {
				newComponent = componentService.addNewProductComponent(productComponentName, productComponentDescription, componentVersions);
				aramarokLog.info("New product component added: '" + productComponentName + "'.");
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
			
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
		}
	}
	
	public synchronized Long commitBug(Bug bug) throws ExternalSystemException, BugException, UserException {
		log.info("Trying to commit a bug.");
		if (bug == null)
			throw new BugException();
		
		if (bug.getId() != null){ //means that a bug is being modified
			try {
				Bug bugDb = entityManager.find(Bug.class, bug.getId());
				
				if (bugDb == null)
					throw new BugException();
				
				if (bugDb.getStatus() != bug.getStatus()){ //status changed
					if (!user.hasRight(Right.CHANGE_BUG_STATUS)){
						log.error("User does not have the 'CHANGE_BUG_STATUS' right");
						throw new UserHasNoRightException();
					}
					
					if (bugDb.getStatus() == BugGeneralStatus.NEW && bug.getStatus() == BugGeneralStatus.REOPENED){
						log.error("Cannot change bug status form NEW to REOPENED");
						throw new BugStatusChangeException();
					}
					
					if (bugDb.getStatus() == BugGeneralStatus.ASSIGNED && bug.getStatus() == BugGeneralStatus.REOPENED){
						log.error("Cannot change bug status form ASSIGNED to REOPENED");
						throw new BugStatusChangeException();
					}
					
					if ((bugDb.getStatus() == BugGeneralStatus.ASSIGNED ||
							bugDb.getStatus() == BugGeneralStatus.FIXED ||
							bugDb.getStatus() == BugGeneralStatus.INVALID ||
							bugDb.getStatus() == BugGeneralStatus.WONTFIX ||
							bugDb.getStatus() == BugGeneralStatus.LATER ||
							bugDb.getStatus() == BugGeneralStatus.WORKSFORME ||
							bugDb.getStatus() == BugGeneralStatus.REOPENED)
							&& bug.getStatus() == BugGeneralStatus.NEW){
						log.error("Cannot change bug status from ... to NEW");
						throw new BugStatusChangeException();
					}
					
					bugDb.setStatus(bug.getStatus());
				}
				
				if ( //bug edited
						bugDb.getComponentVersion().getId().compareTo(bug.getComponentVersion().getId()) !=0 ||
						bugDb.getOperatingSystem().getId().compareTo(bug.getOperatingSystem().getId()) !=0 ||
						bugDb.getPlatform().getId().compareTo(bug.getPlatform().getId()) !=0 ||
						bugDb.getPriority().getId().compareTo(bug.getPriority().getId()) !=0 ||
						bugDb.getSeverity().getId().compareTo(bug.getSeverity().getId()) !=0 ||
						bugDb.getObservedDate().compareTo(bug.getObservedDate()) != 0 ||
						bugDb.getSummary().compareTo(bug.getSummary()) != 0 ||
						bugDb.getDescription().compareTo(bug.getDescription()) != 0 ) {
					if (!user.hasRight(Right.EDIT_BUG)){
						log.error("User does not have the 'EDIT_BUG' right");
						throw new UserHasNoRightException();
					}
					
					bugDb.setProductComponent(bug.getProductComponent());
					bugDb.setComponentVersion(bug.getComponentVersion());
					bugDb.setOperatingSystem(bug.getOperatingSystem());
					bugDb.setPlatform(bug.getPlatform());
					bugDb.setPriority(bug.getPriority());
					bugDb.setSeverity(bug.getSeverity());
					bugDb.setObservedDate(bug.getObservedDate());
					bugDb.setSummary(bug.getSummary());
					bugDb.setDescription(bug.getDescription());
				}
				
				if (bug.getComments() != null && bugDb.getComments() != null){
					if (bugDb.getComments().size() < bug.getComments().size()){ //added comment
						if (!user.hasRight(Right.ADD_BUG_COMMENT)){
							log.error("User does not have the 'ADD_BUG_COMMENT' right");
							throw new UserHasNoRightException();
						}
						for (Comment c: bug.getComments()){
							if (c.getId() == null){
								entityManager.persist(c);
								Comment commFromDATABASE = (Comment)entityManager.createNamedQuery("Comment.findCommentByPostedDate").setParameter("datePosted", c.getDatePosted()).getSingleResult();
								bugDb.addComment(commFromDATABASE);
							}
						}
						//bugDb.setComments(bug.getComments());
					}
				}
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else { //means that a new bug is being commited
			if (!user.hasRight(Right.ENTER_NEW_BUG)){
				log.error("User does not have the 'ENTER_NEW_BUG' right");
				throw new UserHasNoRightException();
			}
			bug.setOpenDate(new Date());
			bug.setOwner(this.user);
			
			try{
				entityManager.persist(bug);
				log.info("Bug commited");
				return bug.getId();
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		}
		return null;
	}
	
	public synchronized Bug getBug(Long bugId){
		return bugService.getBug(bugId);
	}
	
	public synchronized List<Comment> getComments(CommentFilter commentFilter){
		return domainService.getComments(commentFilter);
	}
	
	public synchronized List<Bug> getBugs(BugFilter bugFilter){
		return bugService.getBugs(bugFilter);
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
	
	public synchronized void addASavedBugFilterToUser(Long idOfUser, SavedSearch savedSearch) throws UserNotFoundException, ExternalSystemException{
		log.info("Trying to add a saved bug filter to user with ID: " + idOfUser);
		if (idOfUser != null) {
			try {
				User user = entityManager.find(User.class, idOfUser);
				
				if (user == null) {
					throw new UserNotFoundException("User with id " + idOfUser.toString() + " does not exist in the DB!");
				}
				entityManager.persist(savedSearch);
				SavedSearch ssFromDb = (SavedSearch)entityManager.createNamedQuery("SavedSearch.findSavedSearchByDate").setParameter("savedDate", savedSearch.getSavedDate()).getSingleResult();
				user.addASearch(ssFromDb);
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} 
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws ExternalSystemException, ProductComponentException {
		try {
			componentService.modifyProductComponent(idOfProductComponent, newProductComponentData);
			aramarokLog.info("Product component version with name '" + newProductComponentData.getName() + "' was modified.");
		} catch (PersistenceException pe){
			throw new ExternalSystemException(DB_ERROR_MSG, pe);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyComponentVersion(Long idOfComponentVersion, ComponentVersion newComponentVersionData) throws ExternalSystemException, ComponentVersionException {
		try {
			versionService.modifyComponentVersion(idOfComponentVersion, newComponentVersionData);
			aramarokLog.info("Component version with name '" + newComponentVersionData.getName() + "' was modified.");
		} catch (PersistenceException pe){
			throw new ExternalSystemException(DB_ERROR_MSG, pe);
		}
	}
	
	public synchronized void addNewComponentVersion(String componentVersionName, String componentVersionDescription, ProductComponent parentProductComponent, User userAssignedTo) throws ExternalSystemException, ComponentVersionException {
		if (parentProductComponent != null){
			ProductComponent component = componentService.findProductComponent(parentProductComponent.getName());
			if (component == null){
				log.error("The 'parentProductComponent' was not found in the DB!");
				throw new ComponentVersionException("The 'parentProductComponent' was not found in the DB!");
			}
			ComponentVersion newComponentVersion = null;
			try {
				newComponentVersion = versionService.addNewComponentVersion(componentVersionName, componentVersionDescription, userAssignedTo);
				aramarokLog.info("New component version added: '" + componentVersionName + "'.");
			} catch (PersistenceException pe){
				throw new ExternalSystemException(DB_ERROR_MSG, pe);
			}
			
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
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getAllRoles() throws ExternalSystemException{
		log.info("Get all roles.");
		try {
			Query query = entityManager.createNamedQuery("Role.allRoles");
			return (List<Role>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public List<ProductComponent> getAllProductComponents() throws ExternalSystemException{
		log.info("Get all components.");
		try {
			Query query = entityManager.createNamedQuery("Component.allComponents");
			return (List<ProductComponent>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ComponentVersion> getAllComponentVersions() throws ExternalSystemException{
		try{
			return versionService.getAllComponentVersions();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<OperatingSystem> getAllOperatingSystems() throws ExternalSystemException{
		log.info("Get all operating systems.");
		try {
			Query query = entityManager.createNamedQuery("OperatingSystem.allOSs");
			return (List<OperatingSystem>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Platform> getAllPlatforms() throws ExternalSystemException{
		log.info("Get all platforms.");
		try {
			Query query = entityManager.createNamedQuery("Platform.allPlatforms");
			return (List<Platform>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Priority> getAllPriorities() throws ExternalSystemException{
		log.info("Get all priorities.");
		try {
			Query query = entityManager.createNamedQuery("Priority.allPriorities");
			return (List<Priority>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Severity> getAllSeverities() throws ExternalSystemException{
		log.info("Get all severities.");
		try {
			Query query = entityManager.createNamedQuery("Severity.allSeverities");
			return (List<Severity>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
}