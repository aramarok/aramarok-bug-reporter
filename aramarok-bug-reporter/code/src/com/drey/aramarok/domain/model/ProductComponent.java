package com.drey.aramarok.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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
@Table(name = "PRODUCT_COMPONENT", 
		uniqueConstraints={@UniqueConstraint(columnNames="NAME")}
)
@NamedQueries( {
		@NamedQuery(name = "ProductComponent.findComponentByComponentName", query = "SELECT c from ProductComponent as c WHERE c.name = :componentName"),
		@NamedQuery(name = "ProductComponent.findComponentByComponentId", query = "SELECT c from ProductComponent as c WHERE c.id = :componentId"),
		@NamedQuery(name = "ProductComponent.allComponents", query = "SELECT c from ProductComponent as c ORDER BY c.id ASC")
		})
		
public class ProductComponent implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "COMPONENT_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME", nullable=false)
	private String name = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";
	
/*	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="PRODUCT_COMPONENT_ASSIGNED_TO",
				joinColumns = {@JoinColumn(name="comp_key", referencedColumnName="COMPONENT_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="user_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> usersAssigned;
*/	
	@ManyToOne
	@JoinColumn(name = "USER_ASSIGNED")
	private User userAssigned = null;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="COMPONENT_VERSIONS",
				joinColumns = {@JoinColumn(name="comp_key", referencedColumnName="COMPONENT_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="version_key", referencedColumnName="VERSION_ID", unique=false)}
				)
	private Set<ComponentVersion> versions;
	
	public ProductComponent(){		
	}
	
	public ProductComponent(String name, String description, User userAssigned){
		init(name, description, null, userAssigned);
	}
	
	public ProductComponent(String name, String description, List<ComponentVersion> versions, User userAssigned){
		init(name, description, versions, userAssigned);
	}
	
	private void init(String name, String description, List<ComponentVersion> versions, User userAssigned){
		this.name = name;
		this.description = description;
		this.userAssigned = userAssigned;
		Set<ComponentVersion> tmp = new HashSet<ComponentVersion>();
		if (versions!= null && !versions.isEmpty()){
			for (ComponentVersion cv: versions){
				tmp.add(cv);
			}
		}
		this.versions = tmp;
	}

	public void addVersion(ComponentVersion version){
		if (versions == null)
			versions = new HashSet<ComponentVersion>();
		versions.add(version);
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

	public Set<ComponentVersion> getVersions() {
		return versions;
	}

	public void setVersions(Set<ComponentVersion> versions) {
		this.versions = versions;
	}

	public User getUserAssigned() {
		return userAssigned;
	}

	public void setUserAssigned(User userAssigned) {
		this.userAssigned = userAssigned;
	}
}