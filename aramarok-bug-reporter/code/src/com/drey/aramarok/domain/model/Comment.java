package com.drey.aramarok.domain.model;

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
@Table(name = "COMMENT")
@NamedQueries( {
		@NamedQuery(name = "Comment.findCommentByPostedDate", query = "SELECT c from Comment as c WHERE c.datePosted = :datePosted")
		} )
public class Comment implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "COMMENT_ID")
	private Long id = null;

	@Version
	@Column(name = "OBJ_VERSION")
	private static final long serialVersionUID = 0;

	@Column(name = "COMMENT_TEXT")
	private String commentText = "";
	
	@Column(name = "DATE_POSTED")
	private Date datePosted = null;
	
	public Comment(){		
	}
	
	public Comment(String commnet, Date datePosted){
		this.commentText = commnet;
		this.datePosted = datePosted;
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
}
