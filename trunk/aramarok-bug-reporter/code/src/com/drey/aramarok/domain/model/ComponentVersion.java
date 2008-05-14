package com.drey.aramarok.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name = "VERSION")
@NamedQueries( {
		@NamedQuery(name = "ComponentVersion.findVersionsByVersionName", query = "SELECT v from ComponentVersion as v WHERE v.name = :versionName"),
		@NamedQuery(name = "ComponentVersion.findVersionsByVersionId", query = "SELECT v from ComponentVersion as v WHERE v.id = :versionId"),
		@NamedQuery(name = "ComponentVersion.allVersions", query = "SELECT v from ComponentVersion as v ORDER BY v.id ASC")
		})
public class ComponentVersion implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "VERSION_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME", nullable=false)
	private String name = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";
	
/*	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="COMPONENT_VERSION_ASSIGNED_TO",
				joinColumns = {@JoinColumn(name="version_key", referencedColumnName="VERSION_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="user_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> usersAssigned;
	*/
	@ManyToOne
	@JoinColumn(name = "USER_ASSIGNED")
	private User userAssigned = null;

	public ComponentVersion(){		
	}
	
	public ComponentVersion(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public ComponentVersion(String name, String description, User userAssigned){
		this.name = name;
		this.description = description;
		this.userAssigned = userAssigned;
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

	public User getUserAssigned() {
		return userAssigned;
	}

	public void setUserAssigned(User userAssigned) {
		this.userAssigned = userAssigned;
	}
}