package com.drey.aramarok.domain;

import java.io.Serializable;
import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name = "ROLE")
@NamedQueries( 
		{@NamedQuery(name = "Role.allRoles", query = "SELECT r from Role as r")}
)

public class Role implements Serializable {	
	@Id
	@GeneratedValue
	@Column(name = "ROLE_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME")
	private String name = "";
	
	@Column(name = "RIGHTS", length = 1255)
	private EnumSet<Right> rights;

	public Role(){
		
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

	public EnumSet<Right> getRights() {
		return rights;
	}

	public void setRights(EnumSet<Right> rights) {
		this.rights = rights;
	}
}