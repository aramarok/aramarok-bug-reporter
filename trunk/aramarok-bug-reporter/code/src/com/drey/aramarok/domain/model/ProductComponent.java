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

	@Column(name = "NAME")
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
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="COMPONENT_VERSIONS",
				joinColumns = {@JoinColumn(name="comp_key", referencedColumnName="COMPONENT_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="version_key", referencedColumnName="VERSION_ID", unique=false)}
				)
	private Set<ComponentVersion> versions;
	
	public ProductComponent(){		
	}
	
	public ProductComponent(String name, String description, List<ComponentVersion> versions){
		init(name, description, versions);
	}
	
	public ProductComponent(String name, String description){
		init(name, description, null);
	}
	
	private void init(String name, String description, List<ComponentVersion> versions){
		this.name = name;
		this.description = description;
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
}