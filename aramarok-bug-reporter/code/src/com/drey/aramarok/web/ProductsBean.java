package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.drey.aramarok.domain.exceptions.product.NoProductNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.product.ProductException;
import com.drey.aramarok.domain.exceptions.product.ProductNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.product.ProductNotFoundException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Product;
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

public class ProductsBean {
	private Logger log = Logger.getLogger(ProductsBean.class);

	private List<Product> productNameList_in = null;
	private LinkedList<SelectItem> productNameList_out = new LinkedList<SelectItem>();

	private List<User> userNameList_in = null;
	private LinkedList<SelectItem> userNameList_out = new LinkedList<SelectItem>();
	
	private List<ProductComponent> userRoles = new ArrayList<ProductComponent>();
	private LinkedList<SelectItem> userList = new LinkedList<SelectItem>();
	private LinkedList<SelectItem> systemList = new LinkedList<SelectItem>();
	private String userSelectedRole = "";
	private String systemSelectedRole = "";
	private List<ProductComponent> roles = new ArrayList<ProductComponent>();
	private List<ProductComponent> allProductComponents = new ArrayList<ProductComponent>();
	
	private String productNameSelected;
	private String name = "";
	private String description = "";
	private String productURL = "";
	private String userAsssignedSelected = "";
	private boolean closeForBugEntry = false;
	private Product editedProductObject = null;
	
	private boolean editedProductNameAlreadyExists= false;
	private boolean editedProductNameInvalid = false;
	private boolean editedProductNameNotFound = false;
	
	
	private boolean newProductNameInvalid = false;
	
	private String newName = "";
	private String newDescription = "";
	private String newProductURL = "";
	private String newUserAsssignedSelected = "";
	private boolean newCloseForBugEntry = false;
	
	private List<ProductComponent> newUserRoles = new ArrayList<ProductComponent>();
	private LinkedList<SelectItem> newUserList = new LinkedList<SelectItem>();
	private LinkedList<SelectItem> newSystemList = new LinkedList<SelectItem>();
	private String newUserSelectedRole = "";
	private String newSystemSelectedRole = "";
	
	private boolean editProduct = false;
	private boolean addAProduct = false;
	
	private boolean newProductNameIsInvalid = false;
	private boolean newProductNameAlreadyExists = false;
	
	private boolean componentListWasModified(){
		HttpSession session = WebUtil.getHttpSession();
		Object o = session.getAttribute(WebUtil.COMPONENT_LIST_MODIFIED);
		
		if (o!=null && o instanceof Boolean){
			Boolean modified = (Boolean)o;
			return modified;
		} else {
			return false;
		}
	}
	
	public String getLoadData(){
		if (componentListWasModified()) {
			loadAllProductComponents();
			loadAvailableProductComponents();
			loadSelectedProductsNameData();
			
			HttpSession session = WebUtil.getHttpSession();
			session.setAttribute(WebUtil.COMPONENT_LIST_MODIFIED, new Boolean(false));
		}
		return null;
	}
	
	public String removeNewRole() {
		if (newUserSelectedRole != null) {
			ProductComponent role = getRoleByName(newUserSelectedRole);
			if (role != null){
				ProductComponent roleToRemove = null;
				for (ProductComponent ur : newUserRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						roleToRemove = ur;
					}
				}
				if (roleToRemove != null){
					newUserRoles.remove(roleToRemove);
					roles.add(roleToRemove);
					newUserList = returnSelectItemList(newUserRoles);
					newSystemList = returnSelectItemList(returnDifference(roles, newUserRoles));
				}
			}
		}
		return null;
	}
	public String addNewRole() {
		if (newSystemSelectedRole != null) {
			ProductComponent role = getRoleByName(newSystemSelectedRole);
			if (role != null){
				for (ProductComponent ur : newUserRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						return null;
					}
				}
				newUserRoles.add(role);
				newUserList.add(new SelectItem(role.getName(), role.getName()));
				
				ProductComponent rrr = null;
				for (ProductComponent rr: roles){
					if (role.getId().compareTo(rr.getId())==0){
						rrr = rr;
					}
				}
				if (rrr!=null){
					roles.remove(rrr);
				}
				
				newSystemList = returnSelectItemList(returnDifference(roles, newUserRoles));
			}
		}
		return null;
	}
	public String removeRole() {
		if (userSelectedRole != null) {
			ProductComponent role = getRoleByName(userSelectedRole);
			if (role != null){
				ProductComponent roleToRemove = null;
				for (ProductComponent ur : userRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						roleToRemove = ur;
					}
				}
				if (roleToRemove != null){
					userRoles.remove(roleToRemove);
					roles.add(roleToRemove);
					userList = returnSelectItemList(userRoles);
					systemList = returnSelectItemList(returnDifference(roles, userRoles));
				}
			}
		}
		return null;
	}
	public String addRole() {
		if (systemSelectedRole != null) {
			ProductComponent role = getRoleByName(systemSelectedRole);
			if (role != null){
				for (ProductComponent ur : userRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						return null;
					}
				}
				userRoles.add(role);
				userList.add(new SelectItem(role.getName(), role.getName()));
				
				ProductComponent rrr = null;
				for (ProductComponent rr: roles){
					if (role.getId().compareTo(rr.getId())==0){
						rrr = rr;
					}
				}
				if (rrr!=null){
					roles.remove(rrr);
				}
				
				systemList = returnSelectItemList(returnDifference(roles, userRoles));
			}
		}
		return null;
	}
	private ProductComponent getRoleByName(String roleName) {
		List<ProductComponent> roleList = allProductComponents;
		for (ProductComponent r : roleList) {
			//System.out.println(r.getName());
			if (r.getName().compareTo(roleName) == 0) {
				return r;
			}
		}
		return null;
	}
	private List<ProductComponent> returnDifference(List<ProductComponent> full_list, List<ProductComponent> second_list){
		if (full_list!=null){
			List<ProductComponent> tmp_role_list = new ArrayList<ProductComponent>();
			for (Iterator<ProductComponent> i = full_list.iterator(); i.hasNext();){
				ProductComponent d1 = i.next();
				int sw = 0;
				for (Iterator<ProductComponent> j = second_list.iterator(); j.hasNext();){
					ProductComponent d2 = j.next();
					if (d1.getId() == d2.getId()){
						sw = 1;
					}
				}
				if (sw == 0){
					tmp_role_list.add(d1);
				}
			}
			return tmp_role_list;
		}else
			return null;	
	}
	private LinkedList<SelectItem> returnSelectItemList(List<ProductComponent> list){
		LinkedList<SelectItem> list2 = new LinkedList<SelectItem>();
		for (ProductComponent r : list) {
			list2.add(new SelectItem(r.getName(), r.getName()));
		}
		return list2;
	}
	
	private void loadAvailableProductComponents() {
		DomainFacade dom = WebUtil.getDomainFacade();
		List<ProductComponent> roleList = null;
		if (dom != null) {
			try {
				roleList = dom.getUnusedProductComponents();
			} catch (ExternalSystemException ex) {
				log.error("ExternalSystemException.", ex);
			}
		}
		roles = roleList;
	}
	
	private void loadAllProductComponents() {
		DomainFacade dom = WebUtil.getDomainFacade();
		List<ProductComponent> roleList = null;
		if (dom != null) {
			try {
				roleList = dom.getAllProductComponents();
			} catch (ExternalSystemException ex) {
				log.error("ExternalSystemException.", ex);
			}
		}
		allProductComponents = roleList;
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
					
					facade.addNewProduct(newName, newDescription, newProductURL, newCloseForBugEntry, newUserAssignedSelected, newUserRoles);
					addAProduct = false;
					productNameSelected = newName;
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
					editedProductObject.setProductURL(productURL);
					editedProductObject.setCloseForBugEntry(closeForBugEntry);
					User userSelected = null;
					if (userAsssignedSelected!=null && userAsssignedSelected.trim().compareTo("")!=0){
						userSelected = facade.getUser(userAsssignedSelected, false);
					}
					editedProductObject.setUserAssigned(userSelected);
					editedProductObject.setProductComponents(new HashSet<ProductComponent>(userRoles));
					
					try {
						facade.modifyProduct(editedProductObject.getId(), editedProductObject);
						productNameSelected = editedProductObject.getName();
						reInitializeProductList();
						editProduct = false;
						loadAvailableProductComponents();
						loadAllProductComponents();
					} catch (ProductNotFoundException e) {
						log.error("ProductNotFoundException!");
					} catch (NoProductNameSpecifiedException e) {
						log.error("NoProductNameSpecifiedException!");
					} catch (ProductNameAlreadyExistsException e){
						editedProductNameAlreadyExists = true;
						log.error("ProductNameAlreadyExistsException!");
					} catch (ProductException e) {
						log.error("ProductException!");
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
		loadAvailableProductComponents();
		loadAllProductComponents();
		loadSelectedProductsNameData();
	}
	
	
	@SuppressWarnings("unchecked")
	private void loadSelectedProductsNameData(){
		if (productNameSelected != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			try {
				editedProductObject = facade.getProduct(productNameSelected);
				if (editedProductObject != null){
					name = editedProductObject.getName();
					description = editedProductObject.getDescription();
					productURL = editedProductObject.getProductURL();
					if (editedProductObject.getUserAssigned()!=null){
						userAsssignedSelected = editedProductObject.getUserAssigned().getUserName();
					} else {
						userAsssignedSelected = "";
					}
					closeForBugEntry = editedProductObject.isCloseForBugEntry();
					initializeProductComponentList();
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
		newProductURL = "";
		newCloseForBugEntry = false;
		newUserAsssignedSelected = "";
		loadAvailableProductComponents();
		loadAllProductComponents();
		resetNewProductComponentList();
	}
	
	private void resetProductFields(){
		name = "";
		description = "";
		productURL = "";
		closeForBugEntry = false;
		userAsssignedSelected = "";
		userList = new LinkedList<SelectItem>();
		systemList = new LinkedList<SelectItem>();
	}
	
	private void initializeProductList(){
		DomainFacade dom = WebUtil.getDomainFacade();
		if (dom != null) {
			try {
				productNameList_in = dom.getAllProducts();
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
	
	private LinkedList<SelectItem> returnSelectItemLinkedListFromAProductList(List<Product> list, boolean addAElement){
		LinkedList<SelectItem> itemList = new LinkedList<SelectItem>();
		if (addAElement) {
			SelectItem item = new SelectItem("" , "");
			itemList.add(item);
		}
		if (list != null) {
			for(Iterator<Product> i=list.iterator(); i.hasNext(); ){
				Product p = i.next();
				SelectItem item = new SelectItem(p.getName(), p.getName());
				itemList.add(item);
			}
		}
		return itemList;
	}
	

	@SuppressWarnings("unchecked")
	private void initializeProductComponentList() {
		userRoles = new LinkedList<ProductComponent>();
		if (editedProductObject.getProductComponents()!=null){
			userRoles.addAll(editedProductObject.getProductComponents());
		}
		userList = returnSelectItemList(userRoles);
		systemList = returnSelectItemList(returnDifference(roles, userRoles));
	}
	
	private void resetNewProductComponentList() {
		newUserRoles = new LinkedList<ProductComponent>();
		newUserList = returnSelectItemList(newUserRoles);
		newSystemList = returnSelectItemList(returnDifference(roles, newUserRoles));
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
	
	public ProductsBean() {
		resetNewProductFields();
		resetProductFields();
		
		initializeProductList();
		selectFirstProductName();
		
		loadSelectedProductsNameData();
		
		initializeProductComponentList();
		initializeUserList();
		
		loadAllProductComponents();
		loadAvailableProductComponents();
	}
	
	public boolean isEditOrAddProduct(){
		return isEditProduct() || isAddAProduct();
	}
	
	// Getters and setters

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

	public Product getEditedProductObject() {
		return editedProductObject;
	}

	public void setEditedProductObject(Product editedProductObject) {
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

	public boolean isNewProductNameAlreadyExists() {
		return newProductNameAlreadyExists;
	}

	public void setNewProductNameAlreadyExists(boolean newProductNameAlreadyExists) {
		this.newProductNameAlreadyExists = newProductNameAlreadyExists;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public boolean isCloseForBugEntry() {
		return closeForBugEntry;
	}

	public void setCloseForBugEntry(boolean closeForBugEntry) {
		this.closeForBugEntry = closeForBugEntry;
	}

	public LinkedList<SelectItem> getUserNameList_out() {
		return userNameList_out;
	}

	public void setUserNameList_out(LinkedList<SelectItem> userNameList_out) {
		this.userNameList_out = userNameList_out;
	}

	public String getUserAsssignedSelected() {
		return userAsssignedSelected;
	}

	public void setUserAsssignedSelected(String userAsssignedSelected) {
		this.userAsssignedSelected = userAsssignedSelected;
	}
	public String getNewProductURL() {
		return newProductURL;
	}
	public void setNewProductURL(String newProductURL) {
		this.newProductURL = newProductURL;
	}
	public String getNewUserAsssignedSelected() {
		return newUserAsssignedSelected;
	}
	public void setNewUserAsssignedSelected(String newUserAsssignedSelected) {
		this.newUserAsssignedSelected = newUserAsssignedSelected;
	}
	public boolean isNewCloseForBugEntry() {
		return newCloseForBugEntry;
	}
	public void setNewCloseForBugEntry(boolean newCloseForBugEntry) {
		this.newCloseForBugEntry = newCloseForBugEntry;
	}
	public LinkedList<SelectItem> getUserList() {
		return userList;
	}
	public void setUserList(LinkedList<SelectItem> userList) {
		this.userList = userList;
	}
	public LinkedList<SelectItem> getSystemList() {
		return systemList;
	}
	public void setSystemList(LinkedList<SelectItem> systemList) {
		this.systemList = systemList;
	}
	public String getUserSelectedRole() {
		return userSelectedRole;
	}
	public void setUserSelectedRole(String userSelectedRole) {
		this.userSelectedRole = userSelectedRole;
	}
	public String getSystemSelectedRole() {
		return systemSelectedRole;
	}
	public void setSystemSelectedRole(String systemSelectedRole) {
		this.systemSelectedRole = systemSelectedRole;
	}
	public LinkedList<SelectItem> getNewUserList() {
		return newUserList;
	}
	public void setNewUserList(LinkedList<SelectItem> newUserList) {
		this.newUserList = newUserList;
	}
	public LinkedList<SelectItem> getNewSystemList() {
		return newSystemList;
	}
	public void setNewSystemList(LinkedList<SelectItem> newSystemList) {
		this.newSystemList = newSystemList;
	}
	public String getNewUserSelectedRole() {
		return newUserSelectedRole;
	}
	public void setNewUserSelectedRole(String newUserSelectedRole) {
		this.newUserSelectedRole = newUserSelectedRole;
	}
	public String getNewSystemSelectedRole() {
		return newSystemSelectedRole;
	}
	public void setNewSystemSelectedRole(String newSystemSelectedRole) {
		this.newSystemSelectedRole = newSystemSelectedRole;
	}
}