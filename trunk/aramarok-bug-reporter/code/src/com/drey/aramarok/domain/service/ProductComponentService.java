package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.util.List;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.ProductComponent;

public interface ProductComponentService {

	public ProductComponent findProductComponent(String productComponentName);
	
	public List<ProductComponent> getAllProductComponents() throws PersistenceException;
	
	public ProductComponent addNewProductComponent(String productComponentName, String productComponentDescription, List<ComponentVersion> componentVersions) throws PersistenceException, ProductComponentException;
	
	public void modifyProductComponent(Long idOfProductComponent, ProductComponent newProductComponentData) throws PersistenceException, ProductComponentException;
	
}