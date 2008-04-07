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

	@Column(name = "NAME")
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
	
	public Product(){
	}
	
	public Product(String name, String description, Set<ProductComponent> components){
		init(name, description, components);
	}
	
	public Product(String name, String description){
		init(name, description, null);
	}
	
	private void init(String name, String description, Set<ProductComponent> components){
		this.name = name;
		this.description = description;
		this.productComponents = components;
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
}