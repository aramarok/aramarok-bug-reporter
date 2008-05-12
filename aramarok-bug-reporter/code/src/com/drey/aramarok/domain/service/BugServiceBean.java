package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
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
import com.drey.aramarok.domain.model.BugGeneralStatus;
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

	public Long commitNewBug(Bug bug, User owner) throws PersistenceException, BugException {
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
		
		entityManager.persist(bug);
		entityManager.flush();
		
		return bug.getId();
	}
	
	public Long commitBug(Bug bug, User logedInUser) throws PersistenceException, BugException, UserException {
		
		Bug bugDb = entityManager.find(Bug.class, bug.getId());
		
		if (bugDb == null)
			throw new BugException();
		
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
			entityManager.flush();
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
			
			bugDb.setProductComponent(bug.getProductComponent());
			bugDb.setComponentVersion(bug.getComponentVersion());
			bugDb.setOperatingSystem(bug.getOperatingSystem());
			bugDb.setPlatform(bug.getPlatform());
			bugDb.setPriority(bug.getPriority());
			bugDb.setSeverity(bug.getSeverity());
			bugDb.setObservedDate(bug.getObservedDate());
			bugDb.setSummary(bug.getSummary());
			bugDb.setDescription(bug.getDescription());
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
					}
				}
			}
		}
		
		return bugDb.getId();
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
	public List<Bug> getBugs(BugFilter bugFilter) {
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
}