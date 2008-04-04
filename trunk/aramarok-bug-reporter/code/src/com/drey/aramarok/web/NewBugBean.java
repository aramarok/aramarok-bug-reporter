package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.DomainFacade;
import com.drey.aramarok.domain.Product;
import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.web.util.WebUtil;

public class NewBugBean {

	private Logger log = Logger.getLogger(NewBugBean.class);
	
	private ArrayList<ProductsWrapperBean> productsList = new ArrayList<ProductsWrapperBean>();

	private boolean productListEmpty = false;
	
	public NewBugBean(){
		createProductRows();
	}
	
	private void createProductRows(){
		log.info("Create products rows");
		
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			List<Product> prod = facade.getAllProducts();
			if (prod != null){
				for (Product p : prod) {
					productsList.add(new ProductsWrapperBean(p));
				}
			}
			productListEmpty = productsList.isEmpty();
			
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException! ", e);	
		}
	}

	
	// Getters and Setters
	public ArrayList<ProductsWrapperBean> getProductsList() {
		return productsList;
	}

	public void setProductsList(ArrayList<ProductsWrapperBean> productsList) {
		this.productsList = productsList;
	}

	public boolean isProductListEmpty() {
		return productListEmpty;
	}

	public void setProductListEmpty(boolean productListEmpty) {
		this.productListEmpty = productListEmpty;
	}
}
