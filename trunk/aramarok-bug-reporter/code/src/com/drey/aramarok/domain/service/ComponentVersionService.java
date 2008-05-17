package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.util.List;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.User;

public interface ComponentVersionService {
	
	public ComponentVersion findComponentVersion(String componentVersionName);
	
	public ComponentVersion getComponentVersion(Long componentVersionId) throws PersistenceException;
	
	public List<ComponentVersion> getAllComponentVersions() throws PersistenceException;
	
	public User getUserAssignedForSubmittingBug(Long componentVersionId) throws PersistenceException;
	
	public ComponentVersion addNewComponentVersion(String componentVersionName, String componentVersionDescription, User userAssignedTo) throws PersistenceException, ComponentVersionException;
	
	public void modifyComponentVersion(Long idOfVersion, ComponentVersion newComponentVersionData) throws PersistenceException, ComponentVersionException;

	public List<ComponentVersion> getUnusedComponentVersions() throws PersistenceException;
	
}