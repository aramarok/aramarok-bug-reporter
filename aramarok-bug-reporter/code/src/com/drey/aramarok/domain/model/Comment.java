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
@Table(name = "COMMENT")
@NamedQueries( {
		@NamedQuery(name = "Comment.findCommentByPostedDate", query = "SELECT c from Comment as c WHERE c.datePosted = :datePosted"),
		@NamedQuery(name = "Comment.findCommentByUserId", query = "SELECT c from Comment as c WHERE c.user.id = :userId")
		} )
public class Comment implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "COMMENT_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "COMMENT_TEXT", nullable=false)
	private String commentText;
	
	@Column(name = "DATE_POSTED", nullable=false)
	private Date datePosted;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable=false)
	private User user;
	
	@Column(name = "VOTES")
	private int votes = 0;
	
	public Comment(){		
	}
	
	public Comment(String commnet, Date datePosted){
		this.commentText = commnet;
		this.datePosted = datePosted;
	}
	
	public Comment(User user, Date datePosted, String commnet){
		this.user = user;
		this.datePosted = datePosted;
		this.commentText = commnet;
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

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public Date getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(Date datePosted) {
		this.datePosted = datePosted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}