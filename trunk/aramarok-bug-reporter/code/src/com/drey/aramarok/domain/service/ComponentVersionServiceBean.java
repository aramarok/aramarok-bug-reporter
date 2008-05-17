package com.drey.aramarok.domain.service;

/**
 * @author Tolnai.Andrei 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.exceptions.version.NoVersionNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.version.VersionNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.version.VersionNotFoundException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.User;

@Stateless
@Local(ComponentVersionService.class)
public class ComponentVersionServiceBean implements ComponentVersionService, Serializable{

	private static final long serialVersionUID = 8492794486220701484L;

	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	@EJB
	private ProductComponentService productComponentService;
	
	private static Logger log = Logger.getLogger(ComponentVersionServiceBean.class);
	
	public synchronized ComponentVersion findComponentVersion(String componentVersionName) {
		log.info("Find component version name: " + componentVersionName);
		try {
			ComponentVersion version = (ComponentVersion) entityManager.createNamedQuery("ComponentVersion.findVersionsByVersionName").setParameter("versionName", componentVersionName).getSingleResult();
			return version;
		} catch (NoResultException e) {
			return null; 
		}
	}

	public synchronized ComponentVersion getComponentVersion(Long componentVersionId) throws PersistenceException {
		log.info("Get component version by id: " + componentVersionId);
		try {
			ComponentVersion componentVersion = (ComponentVersion) entityManager.createNamedQuery("ComponentVersion.findVersionsByVersionId").setParameter("versionId", componentVersionId).getSingleResult();
			return componentVersion;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<ComponentVersion> getAllComponentVersions() throws PersistenceException {
		log.info("Get all component versions.");
		try {
			Query query = entityManager.createNamedQuery("ComponentVersion.allVersions");
			return (List<ComponentVersion>) query.getResultList();
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized User getUserAssignedForSubmittingBug(Long componentVersionId) throws PersistenceException {
		log.info("Get user assigned to component version with id:" + componentVersionId);
		ComponentVersion componentVersion = getComponentVersion(componentVersionId);
		
		if (componentVersion!=null){
			if (componentVersion.getUserAssigned()!=null){
				return componentVersion.getUserAssigned();
			} else {
				ProductComponent productComponent = productComponentService.getProductComponentForComponentVersion(componentVersionId);
				if (productComponent!=null){
					return productComponentService.getUserAssignedForSubmittingBug(productComponent.getId());
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	
	public synchronized ComponentVersion addNewComponentVersion(String componentVersionName, String componentVersionDescription, User userAssignedTo) throws PersistenceException, ComponentVersionException {
		log.info("Trying to add a new component version: " + componentVersionName + ".");
		ComponentVersion version = findComponentVersion(componentVersionName);
		if (version != null) {
			throw new VersionNameAlreadyExistsException("Component version with specified component version name already exists!");
		}
		if (componentVersionName.trim().compareTo("") == 0) {
			throw new NoVersionNameSpecifiedException("No component version name was specified!");
		}
		
		ComponentVersion newVersion = new ComponentVersion(componentVersionName, componentVersionDescription, userAssignedTo);
		
		entityManager.persist(newVersion);
		entityManager.flush();
		return newVersion;
	}

	@SuppressWarnings("unchecked")
	public synchronized void modifyComponentVersion(Long idOfComponentVersion, ComponentVersion newComponentVersionData) throws PersistenceException, ComponentVersionException {
		log.info("Trying to modify component version with ID: " + idOfComponentVersion);
		if (idOfComponentVersion != null) {
			ComponentVersion version = entityManager.find(ComponentVersion.class, idOfComponentVersion);
			
			if (version == null) {
				throw new VersionNotFoundException("Component version with id " + idOfComponentVersion.toString() + " does not exist in the DB!");
			}
			if (newComponentVersionData.getName().trim().compareTo("") == 0) {
				throw new NoVersionNameSpecifiedException("No component version name was specified!");
			}
			
			List<ComponentVersion> listOfVersions = null;
			try {
				listOfVersions = (List<ComponentVersion>)entityManager.createNamedQuery("ComponentVersion.findVersionsByVersionName").setParameter("versionName", newComponentVersionData.getName()).getResultList();
			} catch (NoResultException nre){
				listOfVersions = null;
			}
			if (listOfVersions!=null && listOfVersions.size() > 0 ) {
				for (Iterator<ComponentVersion> i=listOfVersions.iterator(); i.hasNext();){
					ComponentVersion cVersion = i.next();
					if (cVersion.getId().compareTo(idOfComponentVersion) != 0 ){
						throw new VersionNameAlreadyExistsException("Component version with specified component version name already exists!");
					}
				}
			}
			
			version.setName(newComponentVersionData.getName());
			version.setDescription(newComponentVersionData.getDescription());
			version.setUserAssigned(newComponentVersionData.getUserAssigned());
			entityManager.flush();
		} else {
			throw new ComponentVersionException("Specified ID was NULL.");
		}
	}
	
	public synchronized List<ComponentVersion> getUnusedComponentVersions() throws PersistenceException {
		log.info("Get unused component versions.");
		
		List<ComponentVersion> returnList = new ArrayList<ComponentVersion>();
		List<ProductComponent> allProductComponents = productComponentService.getAllProductComponents();
		List<ComponentVersion> allComponentVersions = this.getAllComponentVersions();
		
		if (allComponentVersions==null){
			return null;
		}
		if (allProductComponents==null){
			return allComponentVersions;
		}
		
		returnList.addAll(allComponentVersions);
		
		for (ComponentVersion cv: allComponentVersions){
			for (ProductComponent pc: allProductComponents){
				if (pc.getVersions()!=null && !pc.getVersions().isEmpty()){
					for (ComponentVersion pcv: pc.getVersions()){
						if (pcv.getId().compareTo(cv.getId())==0){
							returnList.remove(cv);
						}
					}
				}
			}
		}
		
		return returnList;
	}

}