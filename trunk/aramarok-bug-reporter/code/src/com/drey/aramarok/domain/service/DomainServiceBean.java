package com.drey.aramarok.domain.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;


@Stateless
@Local(DomainService.class)
public class DomainServiceBean implements DomainService, Serializable {

	private static final long serialVersionUID = 1L;
	
	//private static final String DB_ERROR_MSG = "A database error has occured.";
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private  static Logger log = Logger.getLogger(DomainServiceBean.class);
	
	public User findUser(String userName) {
		log.info("Find userName: " + userName);
		try {
			User user = (User) entityManager.createNamedQuery("User.findUserByUserName").setParameter("userName", userName).getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public Product findProduct(String productName) {
		log.info("Find product name: " + productName);
		try {
			Product product = (Product) entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", productName).getSingleResult();
			return product;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public ProductComponent findProductComponent(String componentName) {
		log.info("Find component name: " + componentName);
		try {
			ProductComponent component = (ProductComponent) entityManager.createNamedQuery("Component.findComponentByComponentName").setParameter("componentName", componentName).getSingleResult();
			return component;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public ComponentVersion findComponentVersion(String versionName) {
		log.info("Find version name: " + versionName);
		try {
			ComponentVersion version = (ComponentVersion) entityManager.createNamedQuery("Version.findVersionsByVersionName").setParameter("versionName", versionName).getSingleResult();
			return version;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public String currentDateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
		return formatter.format(new Date());
	}
	
	public Bug getBug(Long bugId) {
		if (bugId != null){
			log.info("Get bug with ID: " + bugId);
			try {
				Bug bug = (Bug) entityManager.find(Bug.class, bugId);
				return bug;
			} catch (NoResultException e) {
				return null; 
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Comment> getComments(CommentFilter commentFilter){
		log.info("Get comments request");
		
		List<Comment> results = new ArrayList<Comment>();
		Query query;
		
		String and = " AND ";
		StringBuffer queryStr = new StringBuffer("SELECT c from Comments c ");
		boolean and_needed = false;
		
		if (commentFilter != null){
			queryStr.append(" WHERE ");
			final List<Long> bugIdList = commentFilter.getBugIdList();
			if (bugIdList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("c.bugId IN (:bugIdList)");
				and_needed = true;
			}
									
			if (commentFilter.getSortingMode() != null){
				switch (commentFilter.getSortingMode()) {
					case ID_ASC:			queryStr.append(" order by c.id asc");
											break;
					case ID_DESC:			queryStr.append(" order by c.id desc");
											break;
					default: queryStr.append(" order by c.id asc");
				}
			}
		
			log.info(queryStr.toString());
			query = entityManager.createQuery(queryStr.toString());
						
			if (query != null){
				if (bugIdList != null){
					query.setParameter("bugIdList", bugIdList);
				}		
				try {
					results = query.getResultList();				
				} catch (NoResultException ex) {
					
				} catch(PersistenceException ex) {
					log.error("PersistenceException", ex);
				}
			}
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<Bug> getBugs(BugFilter bugFilter){
		log.info("Get bug request");
		
		List<Bug> results = new ArrayList<Bug>();
		Query query;
		
		String and = " AND ";
		StringBuffer queryStr = new StringBuffer("SELECT b from Bug b ");
		boolean and_needed = false;
		
		if (bugFilter != null){
			queryStr.append(" WHERE ");
			final List<Long> bugIdList = bugFilter.getBugIdList();
			if (bugIdList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.id IN (:bugIdList)");
				and_needed = true;
			}
			final List<BugGeneralStatus> bugStatusList = bugFilter.getBugStatusList();
			if (bugStatusList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.status IN (:bugStatusList)");
				and_needed = true;
			}
			final List<User> ownerList = bugFilter.getOwnerList();
			if (ownerList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.owner IN (:ownerList)");
				and_needed = true;
			}
			final List<User> userAssignedToList = bugFilter.getUserAssignedToList();
			if (userAssignedToList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.userAssignedTo IN (:userAssignedToList)");
				and_needed = true;
			}
			final List<Priority> priorityList = bugFilter.getPriorityList();
			if (priorityList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.priority IN (:priorityList)");
				and_needed = true;
			}
			final List<Severity> severityList = bugFilter.getSeverityList();
			if (severityList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.severity IN (:severityList)");
				and_needed = true;
			}
			final List<OperatingSystem> operatingSystemList = bugFilter.getOperatingSystemList();
			if (operatingSystemList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.operatingSystem IN (:operatingSystemList)");
				and_needed = true;
			}
			final List<Platform> platformList = bugFilter.getPlatformList();
			if (platformList != null){
				if (and_needed) queryStr.append(and);
				queryStr.append("b.platform IN (:platformList)");
				and_needed = true;
			}
									
			if (bugFilter.getSortingMode() != null){
				switch (bugFilter.getSortingMode()) {
					case ID_ASC:queryStr.append(" order by b.id asc");
								break;
					case ID_DESC:	queryStr.append(" order by b.id desc");
									break;
					case OBSERVED_DATE_ASC:	queryStr.append(" order by b.observedDate asc");
											break;
					case OBSERVED_DATE_DESC:queryStr.append(" order by b.observedDate desc");
											break;
					case PRIORITY_ASC:	queryStr.append(" order by b.priority asc");
										break;
					case PRIORITY_DESC: queryStr.append(" order by b.priority desc");
										break;
					case SUMMARY_ASC:	queryStr.append(" order by b.summary asc");
										break;
					case SUMMARY_DESC: 	queryStr.append(" order by b.summary desc");
										break;
					default: queryStr.append(" order by b.id asc");
				}
			}
		
			log.info(queryStr.toString());
			query = entityManager.createQuery(queryStr.toString());
						
			if (query != null){
				if (bugIdList != null){
					query.setParameter("bugIdList", bugIdList);
				}
				if (bugStatusList != null){
					query.setParameter("bugStatusList", bugStatusList);
				}
				if (ownerList != null){
					query.setParameter("ownerList", ownerList);
				}
				if (userAssignedToList != null){
					query.setParameter("userAssignedToList", userAssignedToList);
				}
				if (priorityList != null){
					query.setParameter("priorityList", priorityList);
				}
				if (severityList != null){
					query.setParameter("severityList", severityList);
				}
				if (operatingSystemList != null){
					query.setParameter("operatingSystemList", operatingSystemList);
				}
				if (platformList != null){
					query.setParameter("platformList", platformList);
				}
				try {
					results = query.getResultList();				
				} catch (NoResultException ex) {
					
				} catch(PersistenceException ex) {
					log.error("PersistenceException", ex);
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
			}
		}
		return results;
	}
	
	public Priority getPriority(String priorityName) {
		log.info("Find priority name: " + priorityName);
		try {
			Priority priority = (Priority) entityManager.createNamedQuery("Priority.findPriorityByPriorityName").setParameter("priorityName", priorityName).getSingleResult();
			return priority;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public Severity getSeverity(String severityName) {
		log.info("Find severity name: " + severityName);
		try {
			Severity severity = (Severity) entityManager.createNamedQuery("Severity.findSeverityBySeverityName").setParameter("severityName", severityName).getSingleResult();
			return severity;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public OperatingSystem getOperatingSystem(String operatingSystemName) {
		log.info("Find operating system name: " + operatingSystemName);
		try {
			OperatingSystem operatingSystem = (OperatingSystem) entityManager.createNamedQuery("OperatingSystem.findOperatingSystemByOperatingSystemName").setParameter("operatingSystemName", operatingSystemName).getSingleResult();
			return operatingSystem;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public Platform getPlatform(String platformName) {
		log.info("Find platform name: " + platformName);
		try {
			Platform platform = (Platform) entityManager.createNamedQuery("Platform.findPlatformByPlatformName").setParameter("platformName", platformName).getSingleResult();
			return platform;
		} catch (NoResultException e) {
			return null; 
		}
	}
}