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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "USER", 
		uniqueConstraints={@UniqueConstraint(columnNames="USER_NAME")}
)
@NamedQueries( {
		@NamedQuery(name = "User.findAll", query = "SELECT usr from User as usr ORDER BY usr.userName ASC"),
		@NamedQuery(name = "User.finaAllByStatus", query = "SELECT usr from User as usr WHERE usr.status = :status  OR usr.status=null ORDER BY usr.userName ASC"),
		@NamedQuery(name = "User.findUserByUserName", query = "SELECT usr from User as usr WHERE usr.userName = :userName")
		} )
		
public class User implements Serializable {	
	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;
	
	@Column(name = "USER_NAME", unique=true, nullable=false)
	private String userName;
	
	@Column(name = "PASSWORD", nullable=false)
	private String password = "";
	
	@Column(name = "STATUS", nullable=false)
	private UserStatus status = null;
		
	@Column(name = "FIRST_NAME")
	private String firstName = "";
	
	@Column(name = "LAST_NAME")
	private String lastName = "";
	
	@Column(name = "MIDDLE_NAME")
	private String middleName = "";

	@Column(name = "EMAIL_ADDRESS", nullable=false)
	private String emailAddress = "";
	
	@Column(name = "HIDE_EMAIL")
	private boolean hideEmail = false;
	
	@Column(name = "HOME_PAGE")
	private String homePage= "";
	
	
	@Column(name = "REGISTER_DATE", nullable=false)
	private Date registerDate;
	/*
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(name = "user_role",	joinColumns = @JoinColumn(name = "USER_ID"),
									inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<Role> roles;
	*/
	
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	
	@ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(	name ="USER_SAVED_SEARCHES",
				joinColumns = {@JoinColumn(name="user_key", referencedColumnName="USER_ID", unique=false)},
				inverseJoinColumns={@JoinColumn(name="search_key", referencedColumnName="SAVED_SEARCH_ID", unique=false)}
				)
	private Set<SavedSearch> savedSearches = null;

	public User(){		
	}
	
	public User(String userName, UserStatus status, String firstName, String lastName, String middleName, String emailAddress, Date registerDate){
		init(userName, status, firstName, lastName, middleName, emailAddress, "", registerDate);
	}
	
	public User(String userName, UserStatus status, String firstName, String lastName, String middleName, String emailAddress, String homePage, Date registerDate){
		init(userName, status, firstName, lastName, middleName, emailAddress, homePage, registerDate);
	}
	
	private void init(String userName, UserStatus status, String firstName, String lastName, String middleName, String emailAddress, String homePage, Date registerDate){
		this.userName = userName;
		this.status = status;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.emailAddress = emailAddress;
		this.homePage = homePage;
		this.registerDate = registerDate;
	}
	
	public boolean hasRight(Right right) {
		if (right == null)
			return false;
	/*
		for(Role role : this.getRoles()){
			Set<Right> rights = role.getRights();
			if (rights != null){
				if (rights.contains(right)){
					return true;
				}
			}
		}
	*/
		Set<Right> rights = this.getRole().getRights();
		if (rights != null){
			if (rights.contains(right)){
				return true;
			}
		}
		return false;
	}
	
	
	public void addASearch(SavedSearch ss){
		if (savedSearches == null)
			savedSearches = new HashSet<SavedSearch>();
		savedSearches.add(ss);
	}
	
	/* Getters/setter */
	public String getName(){
		return lastName + " " + firstName + " " + middleName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
/*
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
*/
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<SavedSearch> getSavedSearches() {
		return savedSearches;
	}

	public void setSavedSearches(Set<SavedSearch> savedSearches) {
		this.savedSearches = savedSearches;
	}

	public boolean isHideEmail() {
		return hideEmail;
	}

	public void setHideEmail(boolean hideEmail) {
		this.hideEmail = hideEmail;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
}