package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.BugGeneralStatus;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
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

	public Long commitBug(Bug bug) throws PersistenceException, BugException, UserException {
		// TODO: fix here
		return null;
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