package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

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

import com.drey.aramarok.domain.exceptions.quip.QuipException;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Quip;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.CommentFilter;


@Stateless
@Local(DomainService.class)
public class DomainServiceBean implements DomainService, Serializable {

	private static final long serialVersionUID = 1L;
	
	//private static final String DB_ERROR_MSG = "A database error has occured.";
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private static Logger log = Logger.getLogger(DomainServiceBean.class);
	
	
	public String currentDateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
		return formatter.format(new Date());
	}
	
	public synchronized Comment getComment(Long commentId) throws PersistenceException{
		log.info("Get comment with id:" + commentId);
		try {
			return entityManager.find(Comment.class, commentId);
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Comment> getComments(CommentFilter commentFilter){
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
	
	public synchronized Priority getPriority(String priorityName) {
		log.info("Find priority name: " + priorityName);
		try {
			Priority priority = (Priority) entityManager.createNamedQuery("Priority.findPriorityByPriorityName").setParameter("priorityName", priorityName).getSingleResult();
			return priority;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized Severity getSeverity(String severityName) {
		log.info("Find severity name: " + severityName);
		try {
			Severity severity = (Severity) entityManager.createNamedQuery("Severity.findSeverityBySeverityName").setParameter("severityName", severityName).getSingleResult();
			return severity;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized OperatingSystem getOperatingSystem(String operatingSystemName) {
		log.info("Find operating system name: " + operatingSystemName);
		try {
			OperatingSystem operatingSystem = (OperatingSystem) entityManager.createNamedQuery("OperatingSystem.findOperatingSystemByOperatingSystemName").setParameter("operatingSystemName", operatingSystemName).getSingleResult();
			return operatingSystem;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized Platform getPlatform(String platformName) {
		log.info("Find platform name: " + platformName);
		try {
			Platform platform = (Platform) entityManager.createNamedQuery("Platform.findPlatformByPlatformName").setParameter("platformName", platformName).getSingleResult();
			return platform;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Role> getAllRoles() throws PersistenceException{
		log.info("Get all roles.");
		Query query = entityManager.createNamedQuery("Role.allRoles");
		return (List<Role>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<OperatingSystem> getAllOperatingSystems() throws PersistenceException{
		log.info("Get all operating systems.");
		Query query = entityManager.createNamedQuery("OperatingSystem.allOSs");
		return (List<OperatingSystem>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Platform> getAllPlatforms() throws PersistenceException{
		log.info("Get all platforms.");
		Query query = entityManager.createNamedQuery("Platform.allPlatforms");
		return (List<Platform>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Priority> getAllPriorities() throws PersistenceException{
		log.info("Get all priorities.");
		Query query = entityManager.createNamedQuery("Priority.allPriorities");
		return (List<Priority>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Severity> getAllSeverities() throws PersistenceException{
		log.info("Get all severities.");
		Query query = entityManager.createNamedQuery("Severity.allSeverities");
		return (List<Severity>) query.getResultList();
	}
	
	public synchronized Quip getQuip(Long quipId) throws PersistenceException{
		log.info("Get quip with id:" + quipId);
		try {
			return entityManager.find(Quip.class, quipId);
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized void addQuip(String quipText, User userLoggedIn) throws PersistenceException, QuipException {
		log.info("Add new quip: " + quipText);
		if (quipText==null || (quipText!=null && quipText.trim().compareTo("")==0) || userLoggedIn==null){
			throw new QuipException();
		}
		Quip quip = new Quip(userLoggedIn, quipText);
		quip.setAddedDate(new Date());
		if (userLoggedIn.hasRight(Right.EDIT_QUIPS)){
			quip.setApproved(true);
		}
		
		entityManager.persist(quip);
		entityManager.flush();
	}
	
	public synchronized void editQuip(Long quipId, String newQuipText, boolean visible) throws PersistenceException{
		log.info("Edit quip with id: " + quipId);
		Quip quip = getQuip(quipId);
		if (quip!=null){
			quip.setQuipText(newQuipText);
			quip.setVisible(visible);
			entityManager.merge(quip);
			entityManager.flush();
		}
	}
	
	public synchronized void approveQuip(Long quipId) throws PersistenceException {
		log.info("Approve quip with id: " + quipId);
		Quip quip = getQuip(quipId);
		if (quip!=null){
			quip.setApproved(true);
			entityManager.flush();
		}
	}
	
	public synchronized void voteComment(Long commentId, User userLoggedIn) throws PersistenceException {
		log.info("Vote comment with id: " + commentId);
		Comment comment = getComment(commentId);
		if (comment!=null){
			comment.addVote();
			User u = entityManager.find(User.class, userLoggedIn.getId());
			u.voteComment(comment);
			entityManager.flush();
		}
	}
}