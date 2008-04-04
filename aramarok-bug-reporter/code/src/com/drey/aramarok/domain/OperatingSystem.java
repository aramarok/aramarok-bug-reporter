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
@Table(name = "OPERATING_SYSTEM")
@NamedQueries( {
		@NamedQuery(name = "OperatingSystem.findOperatingSystemByOperatingSystemName", query = "SELECT os from OperatingSystem as os WHERE os.name = :operatingSystemName"),
		@NamedQuery(name = "OperatingSystem.allOSs", query = "SELECT os from OperatingSystem as os ORDER BY os.id ASC")
		})
		
public class OperatingSystem implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "OS_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME")
	private String name = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";

	public OperatingSystem(){		
	}
	
	public OperatingSystem(String name, String description){
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