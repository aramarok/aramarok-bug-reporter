package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.User;

public interface ProductService {
	
	public Product findProduct(String productName) throws PersistenceException;
	
	public Product getProduct(Long productId) throws PersistenceException;
	
	public List<Product> getAllProducts() throws PersistenceException;
	
	public List<Product> getProductsForCommittingABug() throws PersistenceException;
		
	public Product getProductForProductComponent(Long productComponentId) throws PersistenceException;
	
	public User getUserAssignedToProduct(Long productId) throws PersistenceException;
	
	public void addNewProduct(String productName, String productDescription, String productURL, boolean closeForBugEntry, User userAssigned, Set<ProductComponent> productComponents) throws PersistenceException, ProductException;
	
	public void modifyProduct(Long idOfProduct, Product newProductData) throws PersistenceException, ProductException;
	
}