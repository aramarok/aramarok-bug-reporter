package com.drey.aramarok.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;


@Entity
@Table(name = "BUG_STATUS")
public class BugStatus implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "BUG_STATUS_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "NAME")
	private String name = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";
	
	@Column(name = "BUG_GENERAL_STATUS")
	private BugGeneralStatus bugGeneralStatus = null;
	
	public BugStatus(){
	}
	
	public BugStatus(String name, String description, BugGeneralStatus bugGeneralStatus){
		this.name = name;
		this.description = description;
		this.bugGeneralStatus = bugGeneralStatus;
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

	public BugGeneralStatus getBugGeneralStatus() {
		return bugGeneralStatus;
	}

	public void setBugGeneralStatus(BugGeneralStatus bugGeneralStatus) {
		this.bugGeneralStatus = bugGeneralStatus;
	}
}