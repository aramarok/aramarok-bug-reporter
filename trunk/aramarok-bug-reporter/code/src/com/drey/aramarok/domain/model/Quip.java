package com.drey.aramarok.domain.model;

/**
 * @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "QUIPS")
@NamedQueries( {
		@NamedQuery(name = "Quip.allQuipsByAddedDate", query = "SELECT q from Quip as q ORDER BY q.addedDate ASC"),
		@NamedQuery(name = "Quip.allQuipsByQuipText", query = "SELECT q from Quip as q ORDER BY q.quipText ASC"),
		@NamedQuery(name = "Quip.byUserId", query = "SELECT q from Quip as q WHERE q.owner.id = :userId ORDER BY q.id ASC")
		})
public class Quip implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "QUIP_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;
	
	@Column(name = "ADDED_DATE", nullable=false)
	private Date addedDate;
	
	@ManyToOne
	@JoinColumn(name = "OWNER_ID", nullable=true)
	private User owner;
	
	@Column(name = "QUIP_TEXT", nullable=false)
	private String quipText;
	
	@Column(name = "VISIBLE", nullable=false)
	private boolean visible = true;
	
	@Column(name = "APPROVED", nullable=false)
	private boolean approved = false;
	
	public Quip(){
		
	}
	
	public Quip(User owner, String quipText){
		this.owner = owner;
		this.quipText = quipText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getQuipText() {
		return quipText;
	}

	public void setQuipText(String quipText) {
		this.quipText = quipText;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}