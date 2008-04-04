package com.drey.aramarok.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "PLATFORM")
@NamedQueries( {
		@NamedQuery(name = "Platform.findPlatformByPlatformName", query = "SELECT p from Platform as p WHERE p.name = :platformName"),
		@NamedQuery(name = "Platform.allPlatforms", query = "SELECT p from Platform as p ORDER BY p.id ASC")
		})
		
public class Platform  implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "PLATFORM_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME")
	private String name = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";

	public Platform(){		
	}
	
	public Platform(String name, String description){
		this.name = name;
		this.description = description;
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
}