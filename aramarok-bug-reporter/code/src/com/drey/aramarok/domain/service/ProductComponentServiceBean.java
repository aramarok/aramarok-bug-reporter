package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.component.ComponentNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.component.ComponentNotFoundException;
import com.drey.aramarok.domain.exceptions.component.NoComponentNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.ProductComponent;


@Stateless
@Local(ProductComponentService.class)
public class ProductComponentServiceBean implements ProductComponentService, Serializable{

	private static final long serialVersionUID = -513668213833704264L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private static Logger log = Logger.getLogger(ProductComponentServiceBean.class);

	public ProductComponent findProductComponent(String productComponentName) {
		log.info("Find product component name: " + productComponentName);
		try {
			ProductComponent productComponent = (ProductComponent) entityManager.createNamedQuery("ProductComponent.findComponentByComponentName").setParameter("componentName", productComponentName).getSingleResult();
			return productComponent;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductComponent> getAllProductComponents() throws PersistenceException {
		log.info("Get all product components.");
		try {
			Query query = entityManager.createNamedQuery("ProductComponent.allComponents");
			return (List<ProductComponent>) query.getResultList();
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public ProductComponent addNewProductComponent(String productComponentName, String productComponentDescription, List<ComponentVersion> componentVersions) throws PersistenceException, ProductComponentException {
		log.info("Trying to add a new product component: " + productComponentName + ".");
		ProductComponent component = findProductComponent(productComponentName);
		if (component != null) {
			throw new ComponentNameAlreadyExistsException("Product component with specified product component name already exists!");
		}
		if (productComponentName.trim().compareTo("") == 0) {
			throw new NoComponentNameSpecifiedException("No product component name was specified!");
		}
		ProductComponent newComponent = new ProductComponent(productComponentName, productComponentDescription, componentVersions);
		
		entityManager.persist(newComponent);
		entityManager.flush();
		return newComponent;
	}

	@SuppressWarnings("unchecked")
	public void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws PersistenceException, ProductComponentException {
		log.info("Trying to modify product component with ID: " + idOfProductComponent);
		if (idOfProductComponent != null) {
			ProductComponent component = entityManager.find(ProductComponent.class, idOfProductComponent);
			
			if (component == null) {
				throw new ComponentNotFoundException("Product component with id " + idOfProductComponent.toString() + " does not exist in the DB!");
			}
			if (newProductComponentData.getName().trim().compareTo("") == 0) {
				throw new NoComponentNameSpecifiedException("No product component name was specified!");
			}
			
			List<ProductComponent> listOfComponents = null;
			try {
				listOfComponents = (List<ProductComponent>)entityManager.createNamedQuery("ProductComponent.findComponentByComponentName").setParameter("componentName", newProductComponentData.getName()).getResultList();
			} catch (NoResultException nre){
				
			}
			if (listOfComponents != null && listOfComponents.size() > 0 ) {
				for (Iterator<ProductComponent> i=listOfComponents.iterator(); i.hasNext();){
					ProductComponent cProduct = i.next();
					if (cProduct.getId().compareTo(idOfProductComponent) != 0 ){
						throw new ComponentNameAlreadyExistsException("Product component with specified product component name already exists!");
					}
				}
			}
			
			component.setName(newProductComponentData.getName());
			component.setDescription(newProductComponentData.getDescription());
			//component.setVersions(newComponentData.getVersions());
		} else {
			throw new ProductComponentException("Specified ID was NULL.");
		}
	}
}