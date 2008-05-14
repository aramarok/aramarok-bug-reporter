package com.drey.aramarok.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "PRODUCT", 
		uniqueConstraints={@UniqueConstraint(columnNames="NAME")}
)
@NamedQueries( {
		@NamedQuery(name = "Product.findProductByProductName", query = "SELECT prod from Product as prod WHERE prod.name = :productName"),
		@NamedQuery(name = "Product.findProductByProductId", query = "SELECT prod from Product as prod WHERE prod.id = :productId"),
		@NamedQuery(name = "Product.allProducts", query = "SELECT prod from Product as prod ORDER BY prod.id ASC")
		})
public class Product implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "PRODUCT_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME", nullable=false)
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "URL")
	private String productURL;
	
	@Column(name = "CLOSE_FOR_BUG_ENTRY")
	private boolean closeForBugEntry;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="PRODUCT_COMPONENTS",
				joinColumns = {@JoinColumn(name="prod_key", referencedColumnName="PRODUCT_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="comp_key", referencedColumnName="COMPONENT_ID", unique=false)}
				)
	private Set<ProductComponent> productComponents;
	
	@ManyToOne
	@JoinColumn(name = "USER_ASSIGNED")
	private User userAssigned = null;
	
	public Product(){
	}
	
	public Product(String name, String description, String productURL, boolean closeForBugEntry){
		init(name, description, productURL, null, null, closeForBugEntry);
	}
	
	public Product(String name, String description, String productURL, Set<ProductComponent> components, boolean closeForBugEntry){
		init(name, description, productURL, components, null, closeForBugEntry);
	}
	
	public Product(String name, String description, String productURL, Set<ProductComponent> components, User userAssigned, boolean closeForBugEntry){
		init(name, description, productURL, components, userAssigned, closeForBugEntry);
	}
	
	private void init(String name, String description, String productURL, Set<ProductComponent> components, User userAssigned, boolean closeForBugEntry){
		this.name = name;
		this.description = description;
		this.productURL = productURL;
		this.productComponents = components;
		this.userAssigned = userAssigned;
		this.closeForBugEntry = closeForBugEntry;
	}
	
	public void addComponent(ProductComponent component){
		if (productComponents == null)
			productComponents = new HashSet<ProductComponent>();
		productComponents.add(component);
	}
	
	
	/* Getters/setter */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<ProductComponent> getProductComponents() {
		return productComponents;
	}

	public void setProductComponents(Set<ProductComponent> productComponents) {
		this.productComponents = productComponents;
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

	public User getUserAssigned() {
		return userAssigned;
	}

	public void setUserAssigned(User userAssigned) {
		this.userAssigned = userAssigned;
	}
}