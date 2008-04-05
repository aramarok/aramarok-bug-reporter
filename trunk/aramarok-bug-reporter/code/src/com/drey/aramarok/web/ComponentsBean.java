package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.exceptions.component.ComponentNotFoundException;
import com.drey.aramarok.domain.exceptions.component.NoComponentNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.NoProductNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.product.ProductNameAlreadyExistsException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class ComponentsBean {
	private Logger log = Logger.getLogger(ComponentsBean.class);

	private List<ProductComponent> productNameList_in = null;
	private LinkedList<SelectItem> productNameList_out = new LinkedList<SelectItem>();
	
	private String productNameSelected;
	private String name = "";
	private String description = "";
	private LinkedList<SelectItem> productComponents = new LinkedList<SelectItem>();
	private String productComponentSelected = "";
	private String newProductComponentName = "";
	private String newProductComponentDescription = "";
	
	private ProductComponent editedProductObject = null;
	
	private boolean editedProductNameAlreadyExists= false;
	private boolean editedProductNameInvalid = false;
	private boolean editedProductNameNotFound = false;
	
	private boolean newProductNameInvalid = false;
	
	private String newName = "";
	private String newDescription = "";
	
	private boolean editProduct = false;
	private boolean addAProduct = false;
	private boolean addAProductComponent = false;
	private boolean newProductComponentNameIsInvalid = false;
	private boolean newProductComponentnameAlreadyExists = false;
	
	private boolean newProductNameIsInvalid = false;
	private boolean newProductNameAlreadyExists = false;
	private boolean editedProductNameIsInvalid = false;
	
	public void addNewProductButton(){
		addAProduct = !addAProduct;
	}
	
	public void cancelAddNewProduct(){
		resetNewProductFields();
		newProductNameIsInvalid = false;
		addAProduct = false;
	}
	
	public void cancelAddOfAProductComponent(){
		resetComponentProductFields();
		addAProductComponent = false;
	}
	
	public void addNewProductToDB(){
		newProductNameAlreadyExists= false;
		newProductNameIsInvalid = false;
		
		if (isValidDataForNewProduct()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			if (facade != null) {
				try {
					facade.addNewProduct(newName, newDescription, null);
					addAProduct = false;
					loadSelectedProductsNameData();
					resetNewProductFields();
					initializeProductList();
				} catch (ProductNameAlreadyExistsException e){
					log.error("ProductNameAlreadyExistsException");
					newProductNameAlreadyExists = true;
				} catch (NoProductNameSpecifiedException e){
					log.error("NoProductNameSpecifiedException");
					newProductNameIsInvalid = true;
				} catch (ProductException e) {
					log.error("ProductException");
				} catch (FatalDomainException e) {
					log.error("FatalDomainException");
				}
			} else{
				log.error("DomainFacade is null.");
			}
		} else {
			log.error("Invalid data!");
			return; 	
		}
	}
	
	private boolean isValidDataForNewProduct(){
		boolean validData = true;
		
		newProductNameIsInvalid = false;
		
		if(	newName == null || newName.trim().equals("")) {
			validData = false;
			newProductNameIsInvalid = true;
		}		
		return validData;
	}	
	
	public void editProductButton(){
		editProduct = true;
	}
	
	public void saveEditedProduct(){
		editedProductNameAlreadyExists= false;
		editedProductNameInvalid = false;
		editedProductNameNotFound = false;
		
		if (isValidDataForEditedProduct()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			if (facade != null) {
				try {
					editedProductObject.setName(name);
					editedProductObject.setDescription(description);
					//editedProductObject.setVersions(versions);
					
					try {
						facade.modifyProductComponent(editedProductObject.getId(), editedProductObject);
						reInitializeProductList();
						editProduct = false;
						loadSelectedProductsNameData();
					} catch (ComponentNotFoundException e) {
						log.error("ComponentNotFoundException!");
					} catch (NoComponentNameSpecifiedException e) {
						log.error("NoComponentNameSpecifiedException!");
					} catch (ProductComponentException e) {
						log.error("ComponentException!");
					}
				} catch (FatalDomainException e) {
					log.error("FatalDomainException. " , e);
				}
			} else{
				log.error("DomainFacade is null.");
			}
		} else {
			log.error("Invalid data!");
			return; 	
		}
		
		editProduct = false;
	}
	
	private boolean isValidDataForEditedProduct(){
		boolean validData = true;
		
		editedProductNameInvalid = false;
		
		if(	name == null || name.trim().equals("")) {
			validData = false;
			editedProductNameInvalid = true;
		}		
		return validData;
	}
	
	public void doNotSaveEditedProduct(){
		editedProductNameAlreadyExists= false;
		editedProductNameInvalid = false;
		editedProductNameNotFound = false;
		
		cancelAddOfAProductComponent();
		editProduct = false;
		loadSelectedProductsNameData();
	}
	
	
	@SuppressWarnings("unchecked")
	private void loadSelectedProductsNameData(){
		if (productNameSelected != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			editedProductObject = facade.getProductComponent(productNameSelected);
			if (editedProductObject != null){
				name = editedProductObject.getName();
				description = editedProductObject.getDescription();
				initializeProductComponentList();
			} else {
				log.error("The 'editedProductObject' is NULL.");
			}
		} else {
			log.error("The 'productNameSelected' is NULL.");
		}
	}
	
	private void resetNewProductFields(){
		newName = "";
		newDescription = "";
	}
	
	private void resetProductFields(){
		name = "";
		description = "";
	}

	private void resetComponentProductFields(){
		newProductComponentName= "";
		newProductComponentDescription = "";
	}
	
	private void initializeProductList(){
		DomainFacade dom = WebUtil.getDomainFacade();
		if (dom != null) {
			try {
				productNameList_in = dom.getAllProductComponents();
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException!");
			}
			//Collections.sort(userNameList_in, new LocationListComparatorSortByName());
			productNameList_out = returnSelectItemLinkedListFromAProductList(productNameList_in, false);			
		}
		else {
			log.error("DomainFacade was NULL.");
		}
	}
	
	private void reInitializeProductList(){
		initializeProductList();
		loadSelectedProductsNameData();
		initializeProductComponentList();
		productNameSelected = editedProductObject.getName();
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAProductList(List<ProductComponent> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null) {
			for(Iterator<ProductComponent> i=list.iterator(); i.hasNext(); ){
				ProductComponent p = i.next();
				SelectItem item = new SelectItem(p.getName(), p.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	/*
	private ProductComponent returnProductComponentObject(String componentName, List<ProductComponent> componentList){
		if (componentList != null){
			for (Iterator<ProductComponent> i=componentList.iterator(); i.hasNext();){
				ProductComponent  c = i.next();
				if (c.getName().equals(componentName)){
					return c;
				}
			}
		}
		return null;
	}*/

	@SuppressWarnings("unchecked")
	private void initializeProductComponentList() {
		List<ComponentVersion> componentsT = new ArrayList<ComponentVersion>(0);
		if (editedProductObject.getVersions()!= null) {
			for (ComponentVersion comp : editedProductObject.getVersions())
				componentsT.add(comp);
		}
		productComponents = returnSelectItemLinkedListFromAProductComponentList(componentsT, false);
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAProductComponentList(List<ComponentVersion> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null){
			for(Iterator<ComponentVersion> i=list.iterator(); i.hasNext(); ){
				ComponentVersion pc = i.next();
				SelectItem item = new SelectItem(pc.getName() , pc.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private void selectFirstProductName(){
		if (productNameList_out != null || !productNameList_out.isEmpty()) {
			productNameSelected = productNameList_out.getFirst().getLabel();
		}
	}
	
	public void valueChangeListener(ValueChangeEvent  event) {
	   if(event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
		   event.setPhaseId(PhaseId.INVOKE_APPLICATION);
		   event.queue();
		   return;
	   }
	   if (event.getComponent().getId() != null && event.getComponent().getId().compareTo("inputComponentName")==0){
		   this.productNameSelected = (String) event.getNewValue();
		   this.loadSelectedProductsNameData();
	   }
   }
	
	public ComponentsBean() {
		resetNewProductFields();
		resetProductFields();
		
		initializeProductList();
		selectFirstProductName();
		
		loadSelectedProductsNameData();
		
		initializeProductComponentList();
	}
	
	
	
	// Getters and setters
	public String getLoadData(){
		loadSelectedProductsNameData();
		return "";
	}

	public LinkedList<SelectItem> getUserNameList_out() {
		return productNameList_out;
	}

	public void setUserNameList_out(LinkedList<SelectItem> userNameList_out) {
		this.productNameList_out = userNameList_out;
	}

	public String getUserNameSelected() {
		return productNameSelected;
	}

	public void setUserNameSelected(String userNameSelected) {
		this.productNameSelected = userNameSelected;
	}

	public String getUserName() {
		return name;
	}

	public void setUserName(String userName) {
		this.name = userName;
	}

	public String getPassword() {
		return description;
	}

	public void setPassword(String password) {
		this.description = password;
	}

	public boolean isEditedUserNameInvalid() {
		return editedProductNameInvalid;
	}

	public void setEditedUserNameInvalid(boolean editedUserNameInvalid) {
		this.editedProductNameInvalid = editedUserNameInvalid;
	}

	public boolean isNewUserNameInvalid() {
		return newProductNameInvalid;
	}

	public void setNewUserNameInvalid(boolean newUserNameInvalid) {
		this.newProductNameInvalid = newUserNameInvalid;
	}

	public String getNewUserName() {
		return newName;
	}

	public void setNewUserName(String newUserName) {
		this.newName = newUserName;
	}

	public String getNewPassword() {
		return newDescription;
	}

	public void setNewPassword(String newPassword) {
		this.newDescription = newPassword;
	}

	public boolean isEditUser() {
		return editProduct;
	}

	public void setEditUser(boolean editUser) {
		this.editProduct = editUser;
	}

	public boolean isAddAUser() {
		return addAProduct;
	}

	public void setAddAUser(boolean addAUser) {
		this.addAProduct = addAUser;
	}

	public boolean isNewUserNameIsInvalid() {
		return newProductNameIsInvalid;
	}

	public void setNewUserNameIsInvalid(boolean newUserNameIsInvalid) {
		this.newProductNameIsInvalid = newUserNameIsInvalid;
	}

	public boolean isEditedUserNameIsInvalid() {
		return editedProductNameIsInvalid;
	}

	public void setEditedUserNameIsInvalid(boolean editedUserNameIsInvalid) {
		this.editedProductNameIsInvalid = editedUserNameIsInvalid;
	}

	public boolean isEditedUserNameNotFound() {
		return editedProductNameNotFound;
	}

	public void setEditedUserNameNotFound(boolean editedUserNameNotFound) {
		this.editedProductNameNotFound = editedUserNameNotFound;
	}

	public boolean isEditedUserNameAlreadyExists() {
		return editedProductNameAlreadyExists;
	}

	public void setEditedUserNameAlreadyExists(boolean editedUserNameAlreadyExists) {
		this.editedProductNameAlreadyExists = editedUserNameAlreadyExists;
	}

	public LinkedList<SelectItem> getProductNameList_out() {
		return productNameList_out;
	}

	public void setProductNameList_out(LinkedList<SelectItem> productNameList_out) {
		this.productNameList_out = productNameList_out;
	}

	public String getProductNameSelected() {
		return productNameSelected;
	}

	public void setProductNameSelected(String productNameSelected) {
		this.productNameSelected = productNameSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LinkedList<SelectItem> getProductComponents() {
		return productComponents;
	}

	public void setProductComponents(LinkedList<SelectItem> productComponents) {
		this.productComponents = productComponents;
	}

	public ProductComponent getEditedProductObject() {
		return editedProductObject;
	}

	public void setEditedProductObject(ProductComponent editedProductObject) {
		this.editedProductObject = editedProductObject;
	}

	public boolean isEditedProductNameAlreadyExists() {
		return editedProductNameAlreadyExists;
	}

	public void setEditedProductNameAlreadyExists(
			boolean editedProductNameAlreadyExists) {
		this.editedProductNameAlreadyExists = editedProductNameAlreadyExists;
	}

	public boolean isEditedProductNameInvalid() {
		return editedProductNameInvalid;
	}

	public void setEditedProductNameInvalid(boolean editedProductNameInvalid) {
		this.editedProductNameInvalid = editedProductNameInvalid;
	}

	public boolean isEditedProductNameNotFound() {
		return editedProductNameNotFound;
	}

	public void setEditedProductNameNotFound(boolean editedProductNameNotFound) {
		this.editedProductNameNotFound = editedProductNameNotFound;
	}

	public boolean isNewProductNameInvalid() {
		return newProductNameInvalid;
	}

	public void setNewProductNameInvalid(boolean newProductNameInvalid) {
		this.newProductNameInvalid = newProductNameInvalid;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}

	public boolean isEditProduct() {
		return editProduct;
	}

	public void setEditProduct(boolean editProduct) {
		this.editProduct = editProduct;
	}

	public boolean isAddAProduct() {
		return addAProduct;
	}

	public void setAddAProduct(boolean addAProduct) {
		this.addAProduct = addAProduct;
	}

	public boolean isNewProductNameIsInvalid() {
		return newProductNameIsInvalid;
	}

	public void setNewProductNameIsInvalid(boolean newProductNameIsInvalid) {
		this.newProductNameIsInvalid = newProductNameIsInvalid;
	}

	public boolean isEditedProductNameIsInvalid() {
		return editedProductNameIsInvalid;
	}

	public void setEditedProductNameIsInvalid(boolean editedProductNameIsInvalid) {
		this.editedProductNameIsInvalid = editedProductNameIsInvalid;
	}

	public boolean isAddAProductComponent() {
		return addAProductComponent;
	}

	public void setAddAProductComponent(boolean addAProductComponent) {
		this.addAProductComponent = addAProductComponent;
	}

	public String getNewProductComponentName() {
		return newProductComponentName;
	}

	public void setNewProductComponentName(String newProductComponentName) {
		this.newProductComponentName = newProductComponentName;
	}

	public String getNewProductComponentDescription() {
		return newProductComponentDescription;
	}

	public void setNewProductComponentDescription(
			String newProductComponentDescription) {
		this.newProductComponentDescription = newProductComponentDescription;
	}

	public boolean isNewProductComponentNameIsInvalid() {
		return newProductComponentNameIsInvalid;
	}

	public void setNewProductComponentNameIsInvalid(
			boolean newProductComponentNameIsInvalid) {
		this.newProductComponentNameIsInvalid = newProductComponentNameIsInvalid;
	}

	public boolean isNewProductComponentnameAlreadyExists() {
		return newProductComponentnameAlreadyExists;
	}

	public void setNewProductComponentnameAlreadyExists(
			boolean newProductComponentnameAlreadyExists) {
		this.newProductComponentnameAlreadyExists = newProductComponentnameAlreadyExists;
	}

	public String getProductComponentSelected() {
		return productComponentSelected;
	}

	public void setProductComponentSelected(String productComponentSelected) {
		this.productComponentSelected = productComponentSelected;
	}

	public boolean isNewProductNameAlreadyExists() {
		return newProductNameAlreadyExists;
	}

	public void setNewProductNameAlreadyExists(boolean newProductNameAlreadyExists) {
		this.newProductNameAlreadyExists = newProductNameAlreadyExists;
	}
}