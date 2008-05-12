package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.util.List;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Role;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.filters.CommentFilter;

public interface DomainService {
	
	public String currentDateAndTime();
	
	public List<Comment> getComments(CommentFilter commentFilter);
	
	public Priority getPriority(String priorityName);
	
	public Severity getSeverity(String severityName);
	
	public OperatingSystem getOperatingSystem(String operatingSystemName);
	
	public Platform getPlatform(String platformName);
	
	public List<Role> getAllRoles() throws PersistenceException;
	
	public List<OperatingSystem> getAllOperatingSystems() throws PersistenceException;
	
	public List<Platform> getAllPlatforms() throws PersistenceException;
	
	public List<Priority> getAllPriorities() throws PersistenceException;
	
	public List<Severity> getAllSeverities() throws PersistenceException;
	
}