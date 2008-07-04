package com.drey.aramarok.domain.service;

/**
 *  @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.ArrayList;
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

import com.drey.aramarok.domain.exceptions.product.NoProductNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.product.ProductNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.product.ProductNotFoundException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.User;


@Stateless
@Local(ProductService.class)
public class ProductServiceBean implements ProductService, Serializable{

	private static final long serialVersionUID = 2596447487639497154L;
	
	@PersistenceContext( name = "Aramarok")
	private EntityManager entityManager;
	
	private static Logger log = Logger.getLogger(ProductServiceBean.class);

	public synchronized Product findProduct(String productName) throws PersistenceException {
		log.info("Find product name: " + productName);
		try {
			Product product = (Product) entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", productName).getSingleResult();
			return product;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized Product getProduct(Long productId) throws PersistenceException {
		log.info("Get product by id: " + productId);
		try {
			Product product = (Product) entityManager.createNamedQuery("Product.findProductByProductId").setParameter("productId", productId).getSingleResult();
			return product;
		} catch (NoResultException e) {
			return null; 
		}
	}
	
	public synchronized Product getProductForProductComponent(Long productComponentId) throws PersistenceException {
		log.info("Find product for product component with id: " + productComponentId);
		List<Product> allProducts = getAllProducts();
		
		if (allProducts!=null){
			for (Product p: allProducts){
				if (p.getProductComponents()!=null){
					for (ProductComponent pc : p.getProductComponents()){
						if (pc.getId().compareTo(productComponentId)==0){
							return p;
						}
					}
				}
			}
		}
		return null;
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
	
	/**
	 * Returns a list with the product for witch the users can report bugs.
	 * 
	 * For a product can be reported a bug only if that product has a user assigned to it,
	 * or any of it's components has at least one user assigned to it or if the components versions 
	 * has at least one user assigned to it.
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public synchronized List<Product> getProductsForCommittingABug() throws PersistenceException{
		log.info("Get products for committing a bug.");
		List<Product> results = new ArrayList<Product>();
		List<Product> allProducts = getAllProducts();
		
		
		for (Product p : allProducts){
			boolean aPcHasAUserAssigned = false;
			if (!p.isCloseForBugEntry()){
				if (p.getUserAssigned()!=null){
					results.add(p);
				} else if (aPcHasAUserAssigned==false){
					if (p.getProductComponents()!=null){
						boolean aCvHasAUserAssigned = false;
						for (ProductComponent pc: p.getProductComponents()){
							if (aPcHasAUserAssigned==false){
								if (pc.getUserAssigned()!=null){
									results.add(p);
									aPcHasAUserAssigned = true;
								} else if (aCvHasAUserAssigned==false){
									if (pc.getVersions()!=null){
										for (ComponentVersion cv: pc.getVersions()){
											if (aCvHasAUserAssigned==false){
												if (cv.getUserAssigned()!=null){
													results.add(p);
													aCvHasAUserAssigned = true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return results;
	}
	
	public synchronized User getUserAssignedToProduct(Long productId) throws PersistenceException{
		log.info("Get user assigned to product with id:" + productId);
		Product product = getProduct(productId);
		
		if (product!=null){
			return product.getUserAssigned();
		} else {
			return null;
		}
	}
	
	public synchronized void addNewProduct(String productName, String productDescription, String productURL, boolean closeForBugEntry, User userAssigned , List<ProductComponent> productComponents) throws PersistenceException, ProductException {
		log.info("Trying to add a new product: " + productName + "...");
		Product product = findProduct(productName);
		if (product != null) {
			throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
		}
		if (productName.trim().compareTo("") == 0) {
			throw new NoProductNameSpecifiedException("No product name was specified!");
		}
		
		Product newProduct = new Product(productName, productDescription, productURL, userAssigned, null, closeForBugEntry);
		if (productComponents!=null && !productComponents.isEmpty()){
			for (ProductComponent pc :productComponents){
				ProductComponent c = entityManager.find(ProductComponent.class, pc.getId());
				newProduct.addComponent(c);
			}
		}
		
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
			if (newProductData.getName()==null || (newProductData.getName()!=null && newProductData.getName().trim().compareTo("") == 0)) {
				throw new NoProductNameSpecifiedException("No product name was specified!");
			}
			
			List<Product> listOfProducts = null;
			try {
				listOfProducts = (List<Product>)entityManager.createNamedQuery("Product.findProductByProductName").setParameter("productName", newProductData.getName()).getResultList();
			} catch (NoResultException nre){
				listOfProducts = null;
			}
			if (listOfProducts!=null && listOfProducts.size() > 0 ) {
				for (Iterator<Product> i=listOfProducts.iterator(); i.hasNext();){
					Product cProduct = i.next();
					if (cProduct.getId().compareTo(idOfProduct) != 0 ){
						throw new ProductNameAlreadyExistsException("Product with specified product name already exists!");
					}
				}
			}
			
			product.setName(newProductData.getName());
			product.setDescription(newProductData.getDescription());
			product.setProductURL(newProductData.getProductURL());
			product.setUserAssigned(newProductData.getUserAssigned());
			product.setCloseForBugEntry(newProductData.isCloseForBugEntry());
			
			product.setProductComponents(null);
			if (newProductData.getProductComponents()!=null && !newProductData.getProductComponents().isEmpty()){
				for (ProductComponent pc :newProductData.getProductComponents()){
					ProductComponent c = entityManager.find(ProductComponent.class, pc.getId());
					product.addComponent(c);
				}
			}
			
			entityManager.flush();
		} else {
			throw new ProductException("Specified ID was NULL.");
		}
	}
}