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

public interface ProductService {
	
	public Product findProduct(String productName);
	
	public List<Product> getAllProducts() throws PersistenceException;
	
	public void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws PersistenceException, ProductException;
	
	public void modifyProduct(Long idOfProduct, Product newProductData) throws PersistenceException, ProductException;
}