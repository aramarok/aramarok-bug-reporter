package com.drey.aramarok.web;

import javax.servlet.http.HttpSession;

import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.web.util.WebUtil;

public class ProductsWrapperBean {

	private Product product;

	public ProductsWrapperBean(Product product){
		this.product = product;
	}
	
	public String enterNewBug(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.ProductToEnterNewBug, product);
		session.setAttribute(WebUtil.ENTER_NEW_BUG_FOR_PRODUCT, new Boolean(true));
		
		return WebUtil.NEW_BUG_ENTER_OUTCOME;
	}
	
	public String getProductName(){
		return product.getName();
	}
	
	public String getProductURL(){
		if (product.getProductURL()==null || (product.getProductURL()!=null && product.getProductURL().trim().compareTo("")==0)){
			return "-";
		} else {
			return product.getProductURL();
		}
	}
	
	public boolean isURLLink(){
		if (getProductURL().compareTo("-")==0){
			return false;
		} else {
			return true;
		}
	}
	
	public String getProductDescription(){
		return product.getDescription();
	}
}
