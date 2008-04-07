package com.drey.aramarok.domain.model;

/**
 * @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

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
@Table(name = "BUG_HISTORY")
@NamedQueries( {
		@NamedQuery(name = "BugHistory.byUserId", query = "SELECT b from BugHistory as b WHERE b.user.id = :userId ORDER BY b.id ASC")
		})
public class BugHistory implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "BUG_HISTORY_ID")
	private Long id = null;
	
	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = -1544532884553859741L;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable=false)
	private User user;
	
	@Column(name = "ACTION_DATE", nullable=false)
	private Date actionDate;
	
	@Column(name ="BUG_ACTIONS" )
	private EnumSet<BugAction> bugActions;
	
	@Column(name = "BUG_STATUS")
	private BugGeneralStatus bugStatus;
	
	public BugHistory(){
		
	}
	
	public BugHistory(User user, Date actionDate, EnumSet<BugAction> bugActions, BugGeneralStatus bugStatus){
		init(user, actionDate, bugActions, bugStatus);
	}
	
	private void init(User user, Date actionDate, EnumSet<BugAction> bugActions, BugGeneralStatus bugStatus){
		this.user = user;
		this.actionDate = actionDate;
		this.bugActions = bugActions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public EnumSet<BugAction> getBugActions() {
		return bugActions;
	}

	public void setBugActions(EnumSet<BugAction> bugActions) {
		this.bugActions = bugActions;
	}

	public BugGeneralStatus getBugStatus() {
		return bugStatus;
	}

	public void setBugStatus(BugGeneralStatus bugStatus) {
		this.bugStatus = bugStatus;
	}
}