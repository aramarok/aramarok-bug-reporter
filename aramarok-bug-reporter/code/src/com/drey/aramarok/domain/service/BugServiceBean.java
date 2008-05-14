package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.bug.BugComponentException;
import com.drey.aramarok.domain.exceptions.bug.BugDescriptionException;
import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.bug.BugProductException;
import com.drey.aramarok.domain.exceptions.bug.BugSeverityException;
import com.drey.aramarok.domain.exceptions.bug.BugStatusChangeException;
import com.drey.aramarok.domain.exceptions.bug.BugTitleException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.user.UserHasNoRightException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugAction;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.BugHistory;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.BugFilter;


@Stateless
@Local(BugService.class)
public class BugServiceBean implements BugService, Serializable {

	private static final long serialVersionUID = -261736195659108541L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private  static Logger log = Logger.getLogger(BugServiceBean.class);

	public synchronized Long commitNewBug(Bug bug, User owner) throws PersistenceException, BugException {
		if (bug.getProduct() == null){
			throw new BugProductException();
		}
		if (bug.getProductComponent() == null){
			throw new BugComponentException();
		}
		if (bug.getSeverity() == null){
			throw new BugSeverityException();
		}
		if (bug.getSummary() == null || bug.getSummary().trim() == ""){
			throw new BugTitleException();
		}
		if (bug.getDescription() == null || bug.getDescription().trim() == ""){
			throw new BugDescriptionException();
		}
		
		bug.setOpenDate(new Date());
		bug.setOwner(owner);
		
		EnumSet<BugAction> bugActions = EnumSet.noneOf(BugAction.class);
		bugActions.add(BugAction.CREATE_NEW_BUG);
		BugHistory bugHistory = new BugHistory(owner, bug.getOpenDate(), bugActions, BugGeneralStatus.NEW);
		bug.addBugHistory(bugHistory);
		
		entityManager.persist(bug);
		entityManager.flush();
		
		return bug.getId();
	}
	
	public synchronized Long commitBug(Bug bug, User logedInUser) throws PersistenceException, BugException, UserException {
		
		Bug bugDb = entityManager.find(Bug.class, bug.getId());
		
		if (bugDb == null)
			throw new BugException();
		
		EnumSet<BugAction> bugActions = EnumSet.noneOf(BugAction.class);
		
		if (bugDb.getStatus() != bug.getStatus()){ //status changed
			if (	!logedInUser.hasRight(Right.EDIT_BUGS) ||
					!(bug.getOwner().getId().compareTo(logedInUser.getId())==0) || 
					!(bug.getUserAssignedTo().getId().compareTo(logedInUser.getId())==0) || 
					( (bug.getUserAssignedTo().getId().compareTo(logedInUser.getId())==0) && !logedInUser.hasRight(Right.CHANGE_BUG_STATUS))){
				log.error("User does cannot change status of the bug. No enought rights.");
				throw new UserHasNoRightException();
			}
			
			if (bugDb.getStatus() == BugGeneralStatus.NEW && bug.getStatus() == BugGeneralStatus.REOPENED){
				log.error("Cannot change bug status form NEW to REOPENED");
				throw new BugStatusChangeException();
			}
			
			if (bugDb.getStatus() == BugGeneralStatus.NEW && bug.getStatus() == BugGeneralStatus.CLOSED){
				log.error("Cannot change bug status form NEW to CLOSED");
				throw new BugStatusChangeException();
			}
			
			if (bugDb.getStatus() == BugGeneralStatus.ASSIGNED && bug.getStatus() == BugGeneralStatus.REOPENED){
				log.error("Cannot change bug status form ASSIGNED to REOPENED");
				throw new BugStatusChangeException();
			}
			
			if (bugDb.getStatus() == BugGeneralStatus.ASSIGNED && bug.getStatus() == BugGeneralStatus.CLOSED){
				log.error("Cannot change bug status form ASSIGNED to CLOSED");
				throw new BugStatusChangeException();
			}
			
			if (bugDb.getStatus() == BugGeneralStatus.REOPENED && bug.getStatus() == BugGeneralStatus.CLOSED){
				log.error("Cannot change bug status form REOPENED to CLOSED");
				throw new BugStatusChangeException();
			}
			
			if ((bugDb.getStatus() == BugGeneralStatus.ASSIGNED ||
					bugDb.getStatus() == BugGeneralStatus.FIXED ||
					bugDb.getStatus() == BugGeneralStatus.INVALID ||
					bugDb.getStatus() == BugGeneralStatus.WONTFIX ||
					bugDb.getStatus() == BugGeneralStatus.LATER ||
					bugDb.getStatus() == BugGeneralStatus.WORKSFORME ||
					bugDb.getStatus() == BugGeneralStatus.REOPENED ||
					bugDb.getStatus() == BugGeneralStatus.CLOSED)
				&& bug.getStatus() == BugGeneralStatus.NEW){
				log.error("Cannot change bug status from " + bugDb.getStatus().name() + " to NEW");
				throw new BugStatusChangeException();
			}
			
			if ((bugDb.getStatus() == BugGeneralStatus.CLOSED ||
					bugDb.getStatus() == BugGeneralStatus.FIXED ||
					bugDb.getStatus() == BugGeneralStatus.INVALID ||
					bugDb.getStatus() == BugGeneralStatus.WONTFIX ||
					bugDb.getStatus() == BugGeneralStatus.LATER ||
					bugDb.getStatus() == BugGeneralStatus.WORKSFORME ||
					bugDb.getStatus() == BugGeneralStatus.CLOSED)
				&& bug.getStatus() == BugGeneralStatus.ASSIGNED){
				log.error("Cannot change bug status from " + bugDb.getStatus().name() + " to ASSIGNED");
				throw new BugStatusChangeException();
			}
			
			if ((bugDb.getStatus() == BugGeneralStatus.CLOSED) &&
					(bug.getStatus() == BugGeneralStatus.FIXED ||
					bug.getStatus() == BugGeneralStatus.INVALID ||
					bug.getStatus() == BugGeneralStatus.WONTFIX ||
					bug.getStatus() == BugGeneralStatus.LATER ||
					bug.getStatus() == BugGeneralStatus.WORKSFORME ||
					bug.getStatus() == BugGeneralStatus.ASSIGNED)){
				log.error("Cannot change bug status from CLOSED to " + bug.getStatus().name());
				throw new BugStatusChangeException();
			}
			
			bugDb.setStatus(bug.getStatus());
			bugActions.add(BugAction.CHANGE_BUG_GENERAL_STATUS);
			entityManager.flush();
		}
		
		boolean addedToCC = false;
		boolean removedFromCC = false;
		
		if ((bugDb.getCcUsers() != null && bugDb.getCcUsers().size()>0) && bug.getCcUsers() == null){
			removedFromCC = true;
		} else if (bugDb.getCcUsers() == null && (bug.getCcUsers()!=null && bug.getCcUsers().size()>0)){
			addedToCC = true;
		} else if (bugDb.getCcUsers() != null && bug.getCcUsers() != null){
			List<User> l1 = new ArrayList<User>(bugDb.getCcUsers());
			List<User> l2 = new ArrayList<User>(bug.getCcUsers());
			if (!l1.equals(l2)){
				if (l1.size()>l2.size()){
					removedFromCC = true;
				} else if (l1.size()<l2.size()){
					addedToCC = true;
				}
			}
		}
		
		/*
		if (bugDb.getCcUsers()==null && (bug.getCcUsers()!=null && bug.getCcUsers().size()>0)){
			ccWasEdited = true;
		} else if (bugDb.getCcUsers()!=null && (bug.getCcUsers()!=null && bug.getCcUsers().size()>0) ){
			ccWasEdited = true;
		} else if ((bugDb.getCcUsers()!=null && ){
			
		}
		*/
		
		if ( //bug edited
				addedToCC || removedFromCC ||
				bugDb.getProductComponent().getId().compareTo(bug.getProductComponent().getId()) !=0 ||
				bugDb.getComponentVersion().getId().compareTo(bug.getComponentVersion().getId()) !=0 ||
				bugDb.getOperatingSystem().getId().compareTo(bug.getOperatingSystem().getId()) !=0 ||
				bugDb.getPlatform().getId().compareTo(bug.getPlatform().getId()) !=0 ||
				bugDb.getPriority().getId().compareTo(bug.getPriority().getId()) !=0 ||
				bugDb.getSeverity().getId().compareTo(bug.getSeverity().getId()) !=0 ||
				bugDb.getObservedDate().compareTo(bug.getObservedDate()) != 0 ||
				bugDb.getSummary().compareTo(bug.getSummary()) != 0 ||
				bugDb.getDescription().compareTo(bug.getDescription()) != 0 ) {
			if (	!logedInUser.hasRight(Right.EDIT_BUGS) ||
					!(bug.getOwner().getId().compareTo(logedInUser.getId())==0) ||
					( (bug.getOwner().getId().compareTo(logedInUser.getId())==0) && !logedInUser.hasRight(Right.EDIT_BUG) ) ||
					!(bug.getUserAssignedTo().getId().compareTo(logedInUser.getId())==0) || 
					( (bug.getUserAssignedTo().getId().compareTo(logedInUser.getId())==0) && !logedInUser.hasRight(Right.EDIT_BUG))){
				log.error("User does cannot change status of the bug. No enought rights.");
				throw new UserHasNoRightException();
			}
			
			if (bug.getProductComponent() == null){
				throw new BugComponentException();
			}
			if (bug.getSeverity() == null){
				throw new BugSeverityException();
			}
			if (bug.getSummary() == null || bug.getSummary().trim() == ""){
				throw new BugTitleException();
			}
			if (bug.getDescription() == null || bug.getDescription().trim() == ""){
				throw new BugDescriptionException();
			}
			
			bugDb.setCcUsers(bug.getCcUsers());
			if (addedToCC){
				bugActions.add(BugAction.ADD_USER_TO_CC);
			} else if (removedFromCC){
				bugActions.add(BugAction.REMOVE_USER_FROM_CC);
			}
			if (bugDb.getProductComponent().getId().compareTo(bug.getProductComponent().getId()) !=0){
				bugDb.setProductComponent(bug.getProductComponent());
				bugActions.add(BugAction.CHANGE_PRODUCT_COMPONENT);
			}
			if (bugDb.getComponentVersion().getId().compareTo(bug.getComponentVersion().getId()) !=0){
				bugDb.setComponentVersion(bug.getComponentVersion());
				bugActions.add(BugAction.CHANGE_COMPONENT_VERSION);
			}
			if (bugDb.getOperatingSystem().getId().compareTo(bug.getOperatingSystem().getId()) !=0){
				bugDb.setOperatingSystem(bug.getOperatingSystem());
				bugActions.add(BugAction.CHANGE_BUG_OPERATING_SYSTEM);
			}
			if (bugDb.getPlatform().getId().compareTo(bug.getPlatform().getId()) !=0){
				bugDb.setPlatform(bug.getPlatform());
				bugActions.add(BugAction.CHANGE_BUG_PLATFORM);
			}
			if (bugDb.getPriority().getId().compareTo(bug.getPriority().getId()) !=0){
				bugDb.setPriority(bug.getPriority());
				bugActions.add(BugAction.CHANGE_BUG_PRIORITY);
			}
			if (bugDb.getSeverity().getId().compareTo(bug.getSeverity().getId()) !=0){
				bugDb.setSeverity(bug.getSeverity());
				bugActions.add(BugAction.CHANGE_BUG_SEVERITY);
			}
			if (bugDb.getObservedDate().compareTo(bug.getObservedDate()) != 0){
				bugDb.setObservedDate(bug.getObservedDate());
				bugActions.add(BugAction.CHANGE_BUG_OBSERVED_DATE);
			}
			if (bugDb.getSummary().compareTo(bug.getSummary()) != 0){
				bugDb.setSummary(bug.getSummary());
				bugActions.add(BugAction.CHANGE_BUG_SUMMARY);
			}
			if (bugDb.getDescription().compareTo(bug.getDescription()) != 0){
				bugDb.setDescription(bug.getDescription());
				bugActions.add(BugAction.CHANGE_BUG_DESCRIPTION);
			}
			
			entityManager.flush();
		}
		
		if (bug.getComments() != null){
			boolean commentsDifference = false;
			
			if (bugDb.getComments() != null){
				if (bug.getComments().size() > bugDb.getComments().size())
					commentsDifference = true;
			} else if (bug.getComments().size()>0){
				commentsDifference = true;
			}
			
			if (commentsDifference){
				if (!logedInUser.hasRight(Right.ADD_BUG_COMMENT)){
					log.error("User does not have the 'ADD_BUG_COMMENT' right");
					throw new UserHasNoRightException();
				}
				for (Comment c: bug.getComments()){
					if (c.getId() == null){
						entityManager.persist(c);
						entityManager.flush();
						//Comment commFromDATABASE = (Comment)entityManager.createNamedQuery("Comment.findCommentByPostedDate").setParameter("datePosted", c.getDatePosted()).getSingleResult();
						//bugDb.addComment(commFromDATABASE);
						bugDb.addComment(c);
						bugActions.add(BugAction.ADD_COMMENT_TO_BUG);
					}
				}
			}
		}
		
		BugHistory bugHistory = new BugHistory(logedInUser, new Date(), bugActions, bug.getStatus());
		bug.addBugHistory(bugHistory);
		
		entityManager.flush();
		
		return bugDb.getId();
	}

	public synchronized Bug getBug(Long bugId) throws PersistenceException {
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
	public synchronized List<Bug> getBugs(BugFilter bugFilter) throws PersistenceException {
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
					case PRIORITY_ASC:	queryStr.append(" order by b.priority.name asc");
										break;
					case PRIORITY_DESC: queryStr.append(" order by b.priority.name desc");
										break;
					case SUMMARY_ASC:	queryStr.append(" order by b.summary asc");
										break;
					case SUMMARY_DESC: 	queryStr.append(" order by b.summary desc");
										break;
					case OPEN_DATE_ASC: queryStr.append(" order by b.openDate asc");
										break;
					case OPEN_DATE_DESC:queryStr.append(" order by b.openDate desc");
										break;
					case OPERATING_SYSTEM_NAME_ASC:	queryStr.append(" order by b.operatingSystem.name asc");
													break;
					case OPERATING_SYSTEM_NAME_DESC:queryStr.append(" order by b.operatingSystem.name desc");
													break;
					case OWNER_USER_NAME_ASC:	queryStr.append(" order by b.owner.userName asc");
												break;
					case OWNER_USER_NAME_DESC:	queryStr.append(" order by b.owner.userName desc");
												break;
					case PLATFORM_NAME_ASC:	queryStr.append(" order by b.platform.name asc");
											break;
					case PLATFORM_NAME_DESC:queryStr.append(" order by b.platform.name desc");
											break;
					case SEVERITY_ASC:	queryStr.append(" order by b.severity.name asc");
										break;
					case SEVERITY_DESC:	queryStr.append(" order by b.severity.name desc");
										break;
					case STATUS_ASC:queryStr.append(" order by b.status asc");
									break;
					case STATUS_DESC:	queryStr.append(" order by b.status desc");
										break;
					case VOTES_ASC:	queryStr.append(" order by b.votes asc");
									break;
					case VOTES_DESC:queryStr.append(" order by b.votes desc");
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
				results = new ArrayList<Bug>();
			} catch(PersistenceException ex) {
				log.error("PersistenceException", ex);
				throw new PersistenceException();
			}
		}
		return results;
	}
}