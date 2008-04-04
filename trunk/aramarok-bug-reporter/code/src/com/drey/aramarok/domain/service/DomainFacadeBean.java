package com.drey.aramarok.domain.service;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
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
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.bug.BugStatusChangeException;
import com.drey.aramarok.domain.exceptions.component.ComponentException;
import com.drey.aramarok.domain.exceptions.component.ComponentNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.component.ComponentNotFoundException;
import com.drey.aramarok.domain.exceptions.component.NoComponentNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.login.DisabledAccountException;
import com.drey.aramarok.domain.exceptions.login.InvalidPasswordException;
import com.drey.aramarok.domain.exceptions.login.InvalidUserNameException;
import com.drey.aramarok.domain.exceptions.login.LoginException;
import com.drey.aramarok.domain.exceptions.product.NoProductNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.product.ProductNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.product.ProductNotFoundException;
import com.drey.aramarok.domain.exceptions.register.NoPasswordException;
import com.drey.aramarok.domain.exceptions.register.NoUserNameException;
import com.drey.aramarok.domain.exceptions.register.RegisterException;
import com.drey.aramarok.domain.exceptions.register.UserNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.register.UserNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.user.UserHasNoRightException;
import com.drey.aramarok.domain.exceptions.version.NoVersionNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.version.VersionException;
import com.drey.aramarok.domain.exceptions.version.VersionNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.version.VersionNotFoundException;
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
import com.drey.aramarok.domain.model.UserStatus;

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
	
	@Remove
	public synchronized void stopSessionBean() {
		aramarokLog.info("User logged off: " + user.getUserName());
	}
	
	public synchronized User login(String username, String password) throws FatalDomainException, LoginException {
		log.info("Trying to find user.");
		
		try {
			user = domainService.findUser(username);
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
			if (user.getPassword().compareTo(password) != 0){
				throw new InvalidPasswordException("Password is invalid!");
			}
			
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
		
		log.info("User found: " + user.getName());
		
		aramarokLog.info("User logged in: " + user.getUserName());
		/*
		if (user != null){
			user.preLoadRights();
		}
		*/
		return user;
	}
	
	public synchronized User getUser(String userName){
		return domainService.findUser(userName);
	}
	
	public synchronized void registerNewUser(String userName, String password, String emailAddress, String firstName, String lastName, String middleName) throws FatalDomainException, RegisterException {
		log.info("Trying to register user name: " + userName);
		try {
			user = domainService.findUser(userName);
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
			newUser.setPassword(password);
			
			String roleName = "Guest";
			List<Role> roleList = getAllRoles();
			Role selectedRole = null;
			for (Role r : roleList) {
				if (r.getName().compareTo(roleName) == 0)
					selectedRole = r;
			}
			
			newUser.setRole(selectedRole);
			
			entityManager.persist(newUser);
			aramarokLog.info("User name '" + userName + "' was registered.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyUser(Long idOfUser, User newUserData) throws FatalDomainException, RegisterException {
		log.info("Trying to modify user with ID: " + idOfUser);
		if (idOfUser != null) {
			try {
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
				user.setPassword(newUserData.getPassword());
				user.setEmailAddress(newUserData.getEmailAddress());
				user.setFirstName(newUserData.getFirstName());
				user.setMiddleName(newUserData.getMiddleName());
				user.setLastName(newUserData.getLastName());
				user.setRole(newUserData.getRole());
				user.setStatus(newUserData.getStatus());
				
				aramarokLog.info("User with user name '" + user.getUserName() + "' was modified.");
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			throw new RegisterException("Specified ID was NULL.");
		}
	}
	
	public synchronized Product getProduct(String productName){
		return domainService.findProduct(productName);
	}
	
	public synchronized ProductComponent getComponent(String componentName){
		return domainService.findProductComponent(componentName);
	}
	
	public synchronized ComponentVersion getVersion(String versionName){
		return domainService.findComponentVersion(versionName);
	}
	
	public synchronized void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws FatalDomainException, ProductException {
		log.info("Trying to add a new product: " + productName + ".");
		try {
			Product product = domainService.findProduct(productName);
			if (product != null) {
				throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
			}
			if (productName.trim().compareTo("") == 0) {
				throw new NoProductNameSpecifiedException("No product name was specified!");
			}
			
			Product newProduct = new Product(productName, productDescription, productComponents);
			
			entityManager.persist(newProduct);
			aramarokLog.info("New product added: '" + productName + "'.");
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyProduct(Long idOfProduct, Product newProductData) throws FatalDomainException, ProductException {
		log.info("Trying to modify product with ID: " + idOfProduct);
		if (idOfProduct != null) {
			try {
				Product product = entityManager.find(Product.class, idOfProduct);
				
				if (product == null) {
					throw new ProductNotFoundException("Product with id " + idOfProduct.toString() + " does not exist in the DB!");
				}
				if (product.getName().trim().compareTo("") == 0) {
					throw new NoProductNameSpecifiedException("No product name was specified!");
				}
				
				List<Product> listOfProducts = (List<Product>)entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", newProductData.getName()).getResultList();
				if (listOfProducts.size() > 0 ) {
					for (Iterator<Product> i=listOfProducts.iterator(); i.hasNext();){
						Product cProduct = i.next();
						if (cProduct.getId().compareTo(idOfProduct) != 0 ){
							throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
						}
					}
				}
				
				product.setName(newProductData.getName());
				product.setDescription(newProductData.getDescription());
				product.setComponents(newProductData.getComponents());
				aramarokLog.info("Product with name '" + product.getName() + "' was modified.");
				// aramarokLog.info(domainService.currentDateAndTime() + " - Product with name '" + product.getName() + "' was modified."); 
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			throw new ProductException("Specified ID was NULL.");
		}
	}
	
	public synchronized void addNewProductComponent(String componentName, String componentDescription, Product parentProduct, Set<ComponentVersion> componentVersions) throws FatalDomainException, ComponentException {
		log.info("Trying to add a new component: " + componentName + ".");
		if (parentProduct != null) {
			try {
				ProductComponent component = domainService.findProductComponent(componentName);
				if (component != null) {
					throw new ComponentNameAlreadyExistsException("Component with specified component name already exists!");
				}
				if (componentName.trim().compareTo("") == 0) {
					throw new NoComponentNameSpecifiedException("No component name was specified!");
				}
				
				ProductComponent newComponent = new ProductComponent(componentName, componentDescription, componentVersions);
				
				entityManager.persist(newComponent);
				aramarokLog.info("New component added: '" + componentName + "'.");
				
				// here we add the new component to the specified product
				Product product = domainService.findProduct(parentProduct.getName());
				if (product != null){
					product.addComponent(newComponent);
				}
				
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("The 'parentProduct' parrameter was NULL!");
			throw new ComponentException("The 'parentProduct' parrameter was NULL!");
		}
	}
	
	public synchronized Long commitBug(Bug bug) throws FatalDomainException, BugException, UserException {
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
		return domainService.getBug(bugId);
	}
	
	public synchronized List<Comment> getComments(CommentFilter commentFilter){
		return domainService.getComments(commentFilter);
	}
	
	public synchronized List<Bug> getBugs(BugFilter bugFilter){
		return domainService.getBugs(bugFilter);
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
	public synchronized void modifyComponent(Long idOfComponent, ProductComponent newComponentData) throws FatalDomainException, ComponentException {
		log.info("Trying to modify component with ID: " + idOfComponent);
		if (idOfComponent != null) {
			try {
				ProductComponent component = entityManager.find(ProductComponent.class, idOfComponent);
				
				if (component == null) {
					throw new ComponentNotFoundException("Component with id " + idOfComponent.toString() + " does not exist in the DB!");
				}
				if (component.getName().trim().compareTo("") == 0) {
					throw new NoComponentNameSpecifiedException("No component name was specified!");
				}
				
				List<ProductComponent> listOfComponents = (List<ProductComponent>)entityManager.createNamedQuery("Component.findComponentByComponentName").setParameter("componentName", newComponentData.getName()).getResultList();
				if (listOfComponents.size() > 0 ) {
					for (Iterator<ProductComponent> i=listOfComponents.iterator(); i.hasNext();){
						ProductComponent cProduct = i.next();
						if (cProduct.getId().compareTo(idOfComponent) != 0 ){
							throw new ComponentNameAlreadyExistsException("Component with specified component name already exists!");
						}
					}
				}
				
				component.setName(newComponentData.getName());
				component.setDescription(newComponentData.getDescription());
				//component.setVersions(newComponentData.getVersions());
				aramarokLog.info("Component with name '" + component.getName() + "' was modified.");
				// aramarokLog.info(domainService.currentDateAndTime() + " - Product with name '" + product.getName() + "' was modified."); 
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			throw new ComponentException("Specified ID was NULL.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void modifyVersion(Long idOfVersion, ComponentVersion newVersionData) throws FatalDomainException, VersionException {
		log.info("Trying to modify version with ID: " + idOfVersion);
		if (idOfVersion != null) {
			try {
				ComponentVersion version = entityManager.find(ComponentVersion.class, idOfVersion);
				
				if (version == null) {
					throw new VersionNotFoundException("Version with id " + idOfVersion.toString() + " does not exist in the DB!");
				}
				if (version.getName().trim().compareTo("") == 0) {
					throw new NoVersionNameSpecifiedException("No version name was specified!");
				}
				
				List<ComponentVersion> listOfVersions = (List<ComponentVersion>)entityManager.createNamedQuery("Version.findVersionsByVersionName").setParameter("versionName", newVersionData.getName()).getResultList();
				if (listOfVersions.size() > 0 ) {
					for (Iterator<ComponentVersion> i=listOfVersions.iterator(); i.hasNext();){
						ComponentVersion cVersion = i.next();
						if (cVersion.getId().compareTo(idOfVersion) != 0 ){
							throw new VersionNameAlreadyExistsException("Version with specified version name already exists!");
						}
					}
				}
				
				version.setName(newVersionData.getName());
				version.setDescription(newVersionData.getDescription());
				version.setUserAssigned(newVersionData.getUserAssigned());
				aramarokLog.info("Version with name '" + version.getName() + "' was modified.");
				// aramarokLog.info(domainService.currentDateAndTime() + " - Product with name '" + product.getName() + "' was modified."); 
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			throw new VersionException("Specified ID was NULL.");
		}
	}
	
	public synchronized void addNewComponentVersion(String versionName, String versionDescription, ProductComponent parentComponent, User userAssignedTo) throws FatalDomainException, VersionException {
		log.info("Trying to add a new version: " + versionName + ".");
		if (parentComponent != null) {
			try {
				ComponentVersion version = domainService.findComponentVersion(versionName);
				if (version != null) {
					throw new VersionNameAlreadyExistsException("Version with specified version name already exists!");
				}
				if (versionName.trim().compareTo("") == 0) {
					throw new NoVersionNameSpecifiedException("No version name was specified!");
				}
				
				ComponentVersion newVersion = new ComponentVersion(versionName, versionDescription, userAssignedTo);
				
				entityManager.persist(newVersion);
				aramarokLog.info("New version added: '" + versionName + "'.");
				
				// here we add the new version to the specified component
				ProductComponent component = domainService.findProductComponent(parentComponent.getName());
				if (component != null){
					component.addVersion(newVersion);
				}
				
			} catch (PersistenceException ex) {
				throw new ExternalSystemException(DB_ERROR_MSG, ex);
			}
		} else {
			log.error("The 'parentComponent' parrameter was NULL!");
			throw new VersionException("The 'parentComponent' parrameter was NULL!");
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
		log.info("Get all users.");
		try {
			Query query = entityManager.createNamedQuery("User.findAll");
			return (List<User>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getAllProducts() throws ExternalSystemException{
		log.info("Get all products.");
		try {
			Query query = entityManager.createNamedQuery("Product.allProducts");
			return (List<Product>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductComponent> getAllComponents() throws ExternalSystemException{
		log.info("Get all components.");
		try {
			Query query = entityManager.createNamedQuery("Component.allComponents");
			return (List<ProductComponent>) query.getResultList();
		} catch (PersistenceException ex) {
			throw new ExternalSystemException(DB_ERROR_MSG, ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ComponentVersion> getAllVersions() throws ExternalSystemException{
		log.info("Get all versions.");
		try {
			Query query = entityManager.createNamedQuery("Version.allVersions");
			return (List<ComponentVersion>) query.getResultList();
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