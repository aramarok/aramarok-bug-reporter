package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.product.NoProductNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.product.ProductNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.product.ProductNotFoundException;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;


@Stateless
@Local(ProductService.class)
public class ProductServiceBean implements ProductService, Serializable{

	private static final long serialVersionUID = 2596447487639497154L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;

	private  static Logger log = Logger.getLogger(ProductServiceBean.class);

	public synchronized Product findProduct(String productName) {
		log.info("Find product name: " + productName);
		try {
			Product product = (Product) entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", productName).getSingleResult();
			return product;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Product> getAllProducts() throws PersistenceException {
		log.info("Get all products.");
		try {
			Query query = entityManager.createNamedQuery("Product.allProducts");
			return (List<Product>) query.getResultList();
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized void addNewProduct(String productName, String productDescription, Set<ProductComponent> productComponents) throws PersistenceException, ProductException {
		log.info("Trying to add a new product: " + productName + ".");
		Product product = findProduct(productName);
		if (product != null) {
			throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
		}
		if (productName.trim().compareTo("") == 0) {
			throw new NoProductNameSpecifiedException("No product name was specified!");
		}
		
		Product newProduct = new Product(productName, productDescription, productComponents);
		
		entityManager.persist(newProduct);
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	public synchronized void modifyProduct(Long idOfProduct, Product newProductData) throws PersistenceException, ProductException {
		log.info("Trying to modify product with ID: " + idOfProduct);
		if (idOfProduct != null) {
			Product product = entityManager.find(Product.class, idOfProduct);
			
			if (product == null) {
				throw new ProductNotFoundException("Product with id " + idOfProduct.toString() + " does not exist in the DB!");
			}
			if (product.getName().trim().compareTo("") == 0) {
				throw new NoProductNameSpecifiedException("No product name was specified!");
			}
			
			List<Product> listOfProducts = (List<Product>)entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", newProductData.getName()).getResultList();
			if (listOfProducts.size() > 0 ) {
				for (Iterator<Product> i=listOfProducts.iterator(); i.hasNext();){
					Product cProduct = i.next();
					if (cProduct.getId().compareTo(idOfProduct) != 0 ){
						throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
					}
				}
			}
			
			product.setName(newProductData.getName());
			product.setDescription(newProductData.getDescription());
			product.setComponents(newProductData.getComponents());
			entityManager.flush();
		} else {
			throw new ProductException("Specified ID was NULL.");
		}
	}
}