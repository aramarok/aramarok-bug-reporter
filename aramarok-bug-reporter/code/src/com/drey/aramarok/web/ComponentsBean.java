package com.drey.aramarok.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.exceptions.FatalDomainException;
import com.drey.aramarok.domain.exceptions.component.ComponentNameAlreadyExistsException;
import com.drey.aramarok.domain.exceptions.component.ComponentNotFoundException;
import com.drey.aramarok.domain.exceptions.component.NoComponentNameSpecifiedException;
import com.drey.aramarok.domain.exceptions.component.ProductComponentException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.ComponentVersion;
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

public class ComponentsBean {
	private Logger log = Logger.getLogger(ComponentsBean.class);

	private List<ProductComponent> productNameList_in = null;
	private LinkedList<SelectItem> productNameList_out = new LinkedList<SelectItem>();

	private List<User> userNameList_in = null;
	private LinkedList<SelectItem> userNameList_out = new LinkedList<SelectItem>();
	
	private List<ComponentVersion> userRoles = new ArrayList<ComponentVersion>();
	private LinkedList<SelectItem> userList = new LinkedList<SelectItem>();
	private LinkedList<SelectItem> systemList = new LinkedList<SelectItem>();
	private String userSelectedRole = "";
	private String systemSelectedRole = "";
	private List<ComponentVersion> roles = new ArrayList<ComponentVersion>();
	private List<ComponentVersion> allProductComponents = new ArrayList<ComponentVersion>();
	
	private String productNameSelected;
	private String name = "";
	private String description = "";
	private String userAsssignedSelected = "";
	private ProductComponent editedProductObject = null;
	
	private boolean editedProductNameAlreadyExists= false;
	private boolean editedProductNameInvalid = false;
	private boolean editedProductNameNotFound = false;
	
	
	private boolean newProductNameInvalid = false;
	
	private String newName = "";
	private String newDescription = "";
	private String newUserAsssignedSelected = "";
	
	private List<ComponentVersion> newUserRoles = new ArrayList<ComponentVersion>();
	private LinkedList<SelectItem> newUserList = new LinkedList<SelectItem>();
	private LinkedList<SelectItem> newSystemList = new LinkedList<SelectItem>();
	private String newUserSelectedRole = "";
	private String newSystemSelectedRole = "";
	
	private boolean editProduct = false;
	private boolean addAProduct = false;
	
	private boolean newProductNameIsInvalid = false;
	private boolean newProductNameAlreadyExists = false;
	
	public String removeNewRole() {
		if (newUserSelectedRole != null) {
			ComponentVersion role = getRoleByName(newUserSelectedRole);
			if (role != null){
				ComponentVersion roleToRemove = null;
				for (ComponentVersion ur : newUserRoles) {
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
			ComponentVersion role = getRoleByName(newSystemSelectedRole);
			if (role != null){
				for (ComponentVersion ur : newUserRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						return null;
					}
				}
				newUserRoles.add(role);
				newUserList.add(new SelectItem(role.getName(), role.getName()));
				
				ComponentVersion rrr = null;
				for (ComponentVersion rr: roles){
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
			ComponentVersion role = getRoleByName(userSelectedRole);
			if (role != null){
				ComponentVersion roleToRemove = null;
				for (ComponentVersion ur : userRoles) {
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
			ComponentVersion role = getRoleByName(systemSelectedRole);
			if (role != null){
				for (ComponentVersion ur : userRoles) {
					if (ur.getId().compareTo(role.getId())==0){
						return null;
					}
				}
				userRoles.add(role);
				userList.add(new SelectItem(role.getName(), role.getName()));
				
				ComponentVersion rrr = null;
				for (ComponentVersion rr: roles){
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
	private ComponentVersion getRoleByName(String roleName) {
		List<ComponentVersion> roleList = allProductComponents;
		for (ComponentVersion r : roleList) {
			//System.out.println(r.getName());
			if (r.getName().compareTo(roleName) == 0) {
				return r;
			}
		}
		return null;
	}
	private List<ComponentVersion> returnDifference(List<ComponentVersion> full_list, List<ComponentVersion> second_list){
		if (full_list!=null){
			List<ComponentVersion> tmp_role_list = new ArrayList<ComponentVersion>();
			for (Iterator<ComponentVersion> i = full_list.iterator(); i.hasNext();){
				ComponentVersion d1 = i.next();
				int sw = 0;
				for (Iterator<ComponentVersion> j = second_list.iterator(); j.hasNext();){
					ComponentVersion d2 = j.next();
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
	private LinkedList<SelectItem> returnSelectItemList(List<ComponentVersion> list){
		LinkedList<SelectItem> list2 = new LinkedList<SelectItem>();
		for (ComponentVersion r : list) {
			list2.add(new SelectItem(r.getName(), r.getName()));
		}
		return list2;
	}
	
	private void loadAvailableProductComponents() {
		DomainFacade dom = WebUtil.getDomainFacade();
		List<ComponentVersion> roleList = null;
		if (dom != null) {
			try {
				roleList = dom.getUnusedComponentVersions();
			} catch (ExternalSystemException ex) {
				log.error("ExternalSystemException.", ex);
			}
		}
		roles = roleList;
	}
	
	private void loadAllProductComponents() {
		DomainFacade dom = WebUtil.getDomainFacade();
		List<ComponentVersion> roleList = null;
		if (dom != null) {
			try {
				roleList = dom.getAllComponentVersions();
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
						newUserAssignedSelected = facade.getUser(newUserAsssignedSelected);
					}
					
					facade.addNewProductComponent(newName, newDescription, newUserAssignedSelected, newUserRoles);
					addAProduct = false;
					productNameSelected = newName;
					loadSelectedProductsNameData();
					resetNewProductFields();
					initializeProductList();
				} catch (ComponentNameAlreadyExistsException e){
					log.error("ComponentNameAlreadyExistsException");
					newProductNameAlreadyExists = true;
				} catch (NoComponentNameSpecifiedException e){
					log.error("NoComponentNameSpecifiedException");
					newProductNameIsInvalid = true;
				} catch (ProductComponentException e) {
					log.error("ProductComponentException");
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
						userSelected = facade.getUser(userAsssignedSelected);
					}
					editedProductObject.setUserAssigned(userSelected);
					editedProductObject.setVersions(new HashSet<ComponentVersion>(userRoles));
					
					try {
						facade.modifyProductComponent(editedProductObject.getId(), editedProductObject);
						productNameSelected = editedProductObject.getName();
						reInitializeProductList();
						editProduct = false;
						loadAvailableProductComponents();
					} catch (ComponentNotFoundException e) {
						log.error("ComponentNotFoundException!");
					} catch (NoComponentNameSpecifiedException e) {
						log.error("NoComponentNameSpecifiedException!");
					} catch (ComponentNameAlreadyExistsException e){
						editedProductNameAlreadyExists = true;
						log.error("ComponentNameAlreadyExistsException!");
					} catch (ProductComponentException e) {
						log.error("ProductComponentException!");
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
		loadSelectedProductsNameData();
	}
	
	
	@SuppressWarnings("unchecked")
	private void loadSelectedProductsNameData(){
		if (productNameSelected != null){
			DomainFacade facade = WebUtil.getDomainFacade();
			try {
				editedProductObject = facade.getProductComponent(productNameSelected);
				if (editedProductObject != null){
					name = editedProductObject.getName();
					description = editedProductObject.getDescription();
					if (editedProductObject.getUserAssigned()!=null){
						userAsssignedSelected = editedProductObject.getUserAssigned().getUserName();
					} else {
						userAsssignedSelected = "";
					}
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
		newUserAsssignedSelected = "";
		loadAvailableProductComponents();
		resetNewProductComponentList();
	}
	
	private void resetProductFields(){
		name = "";
		description = "";
		userAsssignedSelected = "";
		userList = new LinkedList<SelectItem>();
		systemList = new LinkedList<SelectItem>();
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
	

	@SuppressWarnings("unchecked")
	private void initializeProductComponentList() {
		userRoles = new LinkedList<ComponentVersion>();
		if (editedProductObject.getVersions()!=null){
			userRoles.addAll(editedProductObject.getVersions());
		}
		userList = returnSelectItemList(userRoles);
		systemList = returnSelectItemList(returnDifference(roles, userRoles));
	}
	
	private void resetNewProductComponentList() {
		newUserRoles = new LinkedList<ComponentVersion>();
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
	
	public ComponentsBean() {
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

	public String getParentProductOfComponent(){
		if (editedProductObject!=null ){
			try {
				Product p = WebUtil.getDomainFacade().getProductForProductComponent(editedProductObject.getId());
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

	public void setProductNameList_out(
			LinkedList<SelectItem> productNameList_out) {
		this.productNameList_out = productNameList_out;
	}

	public LinkedList<SelectItem> getUserNameList_out() {
		return userNameList_out;
	}

	public void setUserNameList_out(LinkedList<SelectItem> userNameList_out) {
		this.userNameList_out = userNameList_out;
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

	public void setNewProductNameAlreadyExists(
			boolean newProductNameAlreadyExists) {
		this.newProductNameAlreadyExists = newProductNameAlreadyExists;
	}
	
}