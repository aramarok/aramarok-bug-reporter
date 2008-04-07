package com.drey.aramarok.domain.model;

/**
 * @author Tolnai.Andrei
 */

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "BUG")
@NamedQueries( {
		@NamedQuery(name = "Bug.allBugs", query = "SELECT b from Bug as b ORDER BY b.id ASC")
		})
public class Bug implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "BUG_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID", nullable=false)
	private User owner;
	
	@Column(name = "OPEN_DATE", nullable=false)
	private Date openDate;
	
	@Column(name = "OBSERVED_DATE")
	private Date observedDate;
	/*
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="BUG_ASSIGNED_TO",
				joinColumns = {@JoinColumn(name="bug_key", referencedColumnName="BUG_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="user_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> usersAssignedTo;*/
	
	@ManyToOne
	@JoinColumn(name = "USER_ASSIGNED")
	private User userAssignedTo;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="BUG_CC",
				joinColumns = {@JoinColumn(name="bug_key", referencedColumnName="BUG_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="cc_user_key", referencedColumnName="USER_ID", unique=false)}
				)
	private Set<User> ccUsers;
	
	@Column(name = "SUMMARY")
	private String summary = "";
	
	@Column(name = "DESCRIPTION")
	private String description = "";
	
	//@ManyToOne
	//@JoinColumn(name = "BUG_STATUS_ID")
	@Column(name = "BUG_STATUS")
	private BugGeneralStatus status;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "COMPONENT_ID")
	private ProductComponent productComponent;
	
	@ManyToOne
	@JoinColumn(name = "VERSION_ID")
	private ComponentVersion componentVersion;
	
	@ManyToOne
	@JoinColumn(name = "PLATFORM_ID")
	private Platform platform;
	
	@ManyToOne
	@JoinColumn(name = "OS_ID")
	private OperatingSystem operatingSystem;
	
	@ManyToOne
	@JoinColumn(name = "PRIORITY_ID")
	private Priority priority;
	
	@ManyToOne
	@JoinColumn(name = "SEVERITY_ID")
	private Severity severity;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="BUG_COMMENTS",
				joinColumns = {@JoinColumn(name="bug_key", referencedColumnName="BUG_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="comm_key", referencedColumnName="COMMENT_ID", unique=true)}
				)
	private Set<Comment> comments;

	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="BUG_HISTORIES",
				joinColumns = {@JoinColumn(name="bug_key", referencedColumnName="BUG_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="bug_hist_key", referencedColumnName="BUG_HISTORY_ID", unique=true)}
				)
	private Set<BugHistory> bugHistory;
	
	@Column(name = "VOTES")
	private int votes = 0;
	
	public Bug(){
	}
	
	public Bug(	User owner,
				Date observedDate,
				User userAssignedTo,
				String summary,
				String description, 
				BugGeneralStatus status, 
				Product product, 
				ProductComponent productComponent, 
				ComponentVersion componentVersion,
				Platform platform,
				OperatingSystem operatingSystem,
				Priority priority,
				Severity severity,
				Set<Comment> comments){
		init(owner, observedDate, userAssignedTo, summary, description, status, product, productComponent, componentVersion, platform, operatingSystem, priority, severity, comments);
	}
	
	private void init(	User owner,
						Date observedDate,
						User userAssignedTo,
						String summary,
						String description, 
						BugGeneralStatus status, 
						Product product, 
						ProductComponent productComponent, 
						ComponentVersion componentVersion,
						Platform platform,
						OperatingSystem operatingSystem,
						Priority priority,
						Severity severity,
						Set<Comment> comments){
		this.owner = owner;
		this.observedDate = observedDate;
		this.userAssignedTo = userAssignedTo;
		this.summary = summary;
		this.description = description; 
		this.status = status; 
		this.product = product;
		this.productComponent = productComponent; 
		this.componentVersion = componentVersion;
		this.platform = platform;
		this.operatingSystem = operatingSystem;
		this.priority = priority;
		this.severity = severity;
		this.comments = comments;
	}
	
	public void addComment(Comment comment){
		if (comments == null)
			comments = new HashSet<Comment>();
		comments.add(comment);
	}
	
	public void addBugHistory(BugHistory newBugHistory){
		if (this.bugHistory == null)
			this.bugHistory = new HashSet<BugHistory>();
		this.bugHistory.add(newBugHistory);
	}
	
	public void addUserToCC(User ccUser){
		if (this.ccUsers == null)
			this.ccUsers = new HashSet<User>();
		this.ccUsers.add(ccUser);
	}
	
	public void removeUserFromCC(User ccUser){
		if (this.ccUsers != null && !this.ccUsers.isEmpty() && ccUser!= null && ccUser.getId()!=null){
			for (User u: this.ccUsers){
				if (ccUser.getId().compareTo(u.getId()) == 0){
					this.ccUsers.remove(u);
				}
			}
		}	
	}
	
	public void addVote(){
		this.votes ++;
	}
	
	/* Getters/setter */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BugGeneralStatus getStatus() {
		return status;
	}

	public void setStatus(BugGeneralStatus status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductComponent getProductComponent() {
		return productComponent;
	}

	public void setProductComponent(ProductComponent productComponent) {
		this.productComponent = productComponent;
	}

	public ComponentVersion getComponentVersion() {
		return componentVersion;
	}

	public void setComponentVersion(ComponentVersion componentVersion) {
		this.componentVersion = componentVersion;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Date getObservedDate() {
		return observedDate;
	}

	public void setObservedDate(Date observedDate) {
		this.observedDate = observedDate;
	}

	public Set<User> getCcUsers() {
		return ccUsers;
	}

	public void setCcUsers(Set<User> ccUsers) {
		this.ccUsers = ccUsers;
	}

	public Set<BugHistory> getBugHistory() {
		return bugHistory;
	}

	public void setBugHistory(Set<BugHistory> bugHistory) {
		this.bugHistory = bugHistory;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public User getUserAssignedTo() {
		return userAssignedTo;
	}

	public void setUserAssignedTo(User userAssignedTo) {
		this.userAssignedTo = userAssignedTo;
	}
}