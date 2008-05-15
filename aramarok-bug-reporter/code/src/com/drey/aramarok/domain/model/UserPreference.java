package com.drey.aramarok.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Version;

@Embeddable
public class UserPreference implements Serializable {
	
	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 012364;

	@Column(name = "HIDE_EMAIL")
	private boolean hideEmail = false;
	
	public UserPreference(){
		
	}

	
	
	
	// Getters and setters
	public boolean isHideEmail() {
		return hideEmail;
	}

	public void setHideEmail(boolean hideEmail) {
		this.hideEmail = hideEmail;
	}
	
}