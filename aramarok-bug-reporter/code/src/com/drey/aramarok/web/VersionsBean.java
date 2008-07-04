package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.exceptions.version.ComponentVersionException;
import com.drey.aramarok.domain.exceptions.version.NoVersionNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.version.VersionNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.version.VersionNotFoundException;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.UserStatus;
import com.drey.aramarok.domain.model.filters.UserFilter;
import com.drey.aramarok.domain.model.filters.UserSortingMode;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

/**
 * 
 * @author Tolnai Andrei
 *
 */

public class VersionsBean {
	private Logger log = Logger.getLogger(VersionsBean.class);

	private List<ComponentVersion> productNameList_in = null;
	private LinkedList<SelectItem> productNameList_out = new LinkedList<SelectItem>();

	private List<User> userNameList_in = null;
	private LinkedList<SelectItem> userNameList_out = new LinkedList<SelectItem>();
		
	private String productNameSelected;
	private String name = "";
	private String description = "";
	private String userAsssignedSelected = "";
	private ComponentVersion editedProductObject = null;
	
	private boolean editedProductNameAlreadyExists= false;
	private boolean editedProductNameInvalid = false;
	private boolean editedProductNameNotFound = false;
	
	
	private boolean newProductNameInvalid = false;
	
	private String newName = "";
	private String newDescription = "";
	private String newUserAsssignedSelected = "";
	
	private boolean editProduct = false;
	private boolean addAProduct = false;
	
	private boolean newProductNameIsInvalid = false;
	private boolean newProductNameAlreadyExists = false;
	
	
	private void versionListWasModified(){
		HttpSession session = WebUtil.getHttpSession();
		session.setAttribute(WebUtil.VERSION_LIST_MODIFIED, new Boolean(true));
	}
	
	public void addNewProductButton(){
		resetNewProductFields();
		addAProduct = !addAProduct;
	}
	
	public void cancelAddNewProduct(){
		resetNewProductFields();
		newProductNameIsInvalid = false;
		newProductNameAlreadyExists = false;
		addAProduct = false;
	}
	
	public void addNewProductToDB(){
		newProductNameAlreadyExists= false;
		newProductNameIsInvalid = false;
		
		if (isValidDataForNewProduct()) {
			DomainFacade facade = WebUtil.getDomainFacade();
			if (facade != null) {
				try {
					User newUserAssignedSelected = null;
					if (newUserAsssignedSelected!=null && newUserAsssignedSelected.trim().compareTo("")!=0){
						newUserAssignedSelected = facade.getUser(newUserAsssignedSelected, false);
					}
					
					facade.addNewComponentVersion(newName, newDescription, newUserAssignedSelected);
					addAProduct = false;
					productNameSelected = newName;
					loadSelectedProductsNameData();
					resetNewProductFields();
					initializeProductList();
					versionListWasModified();
				} catch (VersionNameAlreadyExistsException e){
					log.error("VersionNameAlreadyExistsException");
					newProductNameAlreadyExists = true;
				} catch (NoVersionNameSpecifiedException e){
					log.error("NoVersionNameSpecifiedException");
					newProductNameIsInvalid = true;
				} catch (ComponentVersionException e) {
					log.error("ComponentVersionException");
				} catch (FatalDomainException e) {
					log.error("FatalDomainException");
				} catch (UserException e) {
					log.error("UserException");
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
					User userSelected = null;
					if (userAsssignedSelected!=null && userAsssignedSelected.trim().compareTo("")!=0){
						userSelected = facade.getUser(userAsssignedSelected, false);
					}
					editedProductObject.setUserAssigned(userSelected);
					
					try {
						facade.modifyComponentVersion(editedProductObject.getId(), editedProductObject);
						productNameSelected = editedProductObject.getName();
						reInitializeProductList();
						editProduct = false;
						versionListWasModified();
					} catch (VersionNotFoundException e) {
						log.error("VersionNotFoundException!");
					} catch (NoVersionNameSpecifiedException e) {
						log.error("NoVersionNameSpecifiedException!");
					} catch (VersionNameAlreadyExistsException e){
						editedProductNameAlreadyExists = true;
						log.error("VersionNameAlreadyExistsException!");
					} catch (ComponentVersionException e) {
						log.error("ComponentVersionException!");
					} catch (UserException e) {
						log.error("UserException!");
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
		
		editProduct = false;
		loadSelectedProductsNameData();
	}
	
	
	@SuppressWarnings("unchecked")
	private void loadSelectedProductsNameData(){
		if (productNameSelected != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			try {
				editedProductObject = facade.getComponentVersion(productNameSelected);
				if (editedProductObject != null){
					name = editedProductObject.getName();
					description = editedProductObject.getDescription();
					if (editedProductObject.getUserAssigned()!=null){
						userAsssignedSelected = editedProductObject.getUserAssigned().getUserName();
					} else {
						userAsssignedSelected = "";
					}
				} else {
					log.error("The 'editedProductObject' is NULL.");
				}
			} catch (ExternalSystemException e) {
				e.printStackTrace();
			}
			
		} else {
			log.error("The 'productNameSelected' is NULL.");
		}
	}
	
	private void initializeUserList(){
		DomainFacade dom = WebUtil.getDomainFacade();
		if (dom != null) {
			try {
				UserFilter uf = new UserFilter();
				List<Right>rightList = new ArrayList<Right>();
				rightList.add(Right.CHANGE_BUG_STATUS);
				List<UserStatus> userStatusList = new ArrayList<UserStatus>();
				userStatusList.add(UserStatus.ACTIVE);
				uf.setRightList(rightList);
				uf.setUserStatusList(userStatusList);
				uf.setSortingMode(UserSortingMode.USER_NAME_ASC);
				userNameList_in = dom.getUsers(uf);
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException!");
			}
			//Collections.sort(userNameList_in, new LocationListComparatorSortByName());
			userNameList_out = returnSelectItemLinkedListFromAUserList(userNameList_in, true);			
		}
		else {
			log.error("DomainFacade was NULL.");
		}
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAUserList(List<User> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null) {
			for(Iterator<User> i=list.iterator(); i.hasNext(); ){
				User u = i.next();
				SelectItem item = new SelectItem(u.getUserName(), u.getUserName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	private void resetNewProductFields(){
		newName = "";
		newDescription = "";
		newUserAsssignedSelected = "";
	}
	
	private void resetProductFields(){
		name = "";
		description = "";
		userAsssignedSelected = "";
	}
	
	private void initializeProductList(){
		DomainFacade dom = WebUtil.getDomainFacade();
		if (dom != null) {
			try {
				productNameList_in = dom.getAllComponentVersions();
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
		productNameSelected = editedProductObject.getName();
	}
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAProductList(List<ComponentVersion> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null) {
			for(Iterator<ComponentVersion> i=list.iterator(); i.hasNext(); ){
				ComponentVersion p = i.next();
				SelectItem item = new SelectItem(p.getName(), p.getName());
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
	   if (event.getComponent().getId() != null && event.getComponent().getId().compareTo("inputProductName")==0){
		   this.productNameSelected = (String) event.getNewValue();
		   this.loadSelectedProductsNameData();
	   }
   }
	
	public VersionsBean() {
		resetNewProductFields();
		resetProductFields();
		
		initializeProductList();
		selectFirstProductName();
		
		loadSelectedProductsNameData();
		
		initializeUserList();
	}
	
	public boolean isEditOrAddProduct(){
		return isEditProduct() || isAddAProduct();
	}

	public String getParentComponentOfVersion(){
		if (editedProductObject!=null ){
			try {
				ProductComponent p = WebUtil.getDomainFacade().getProductComponentForComponentVersion(editedProductObject.getId());
				if (p!=null){
					return p.getName();
				} else {
					return "-";
				}
			} catch (ExternalSystemException e) {
				log.error("ExternalSystemException" + e);
				return "-";
			}
		} else {
			return "-";
		}
	}



	
	// Getters and setters
	
	
	public LinkedList<SelectItem> getProductNameList_out() {
		return productNameList_out;
	}


	public void setProductNameList_out(LinkedList<SelectItem> productNameList_out) {
		this.productNameList_out = productNameList_out;
	}


	public LinkedList<SelectItem> getUserNameList_out() {
		return userNameList_out;
	}


	public void setUserNameList_out(LinkedList<SelectItem> userNameList_out) {
		this.userNameList_out = userNameList_out;
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


	public String getUserAsssignedSelected() {
		return userAsssignedSelected;
	}


	public void setUserAsssignedSelected(String userAsssignedSelected) {
		this.userAsssignedSelected = userAsssignedSelected;
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


	public String getNewUserAsssignedSelected() {
		return newUserAsssignedSelected;
	}


	public void setNewUserAsssignedSelected(String newUserAsssignedSelected) {
		this.newUserAsssignedSelected = newUserAsssignedSelected;
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


	public boolean isNewProductNameAlreadyExists() {
		return newProductNameAlreadyExists;
	}


	public void setNewProductNameAlreadyExists(boolean newProductNameAlreadyExists) {
		this.newProductNameAlreadyExists = newProductNameAlreadyExists;
	}
	

	
}