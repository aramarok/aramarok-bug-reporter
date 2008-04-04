package com.drey.aramarok.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "SAVED_SEARCHES")
@NamedQueries( {
		@NamedQuery(name = "SavedSearch.findSavedSearchByDate", query = "SELECT s from SavedSearch as s WHERE s.savedDate = :savedDate")
		} )
		
public class SavedSearch implements Serializable {
	
	@Id
	@GeneratedValue
	@Column(name = "SAVED_SEARCH_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;
	
	@Column(name = "NAME")
	private String name = "";
	
	//@Embedded
	//private BugFilter bugFilter = null;
	
	@Column(name = "SAVED_DATE")
	private Date savedDate;

	public SavedSearch(){
		
	}
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(Date savedDate) {
		this.savedDate = savedDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}