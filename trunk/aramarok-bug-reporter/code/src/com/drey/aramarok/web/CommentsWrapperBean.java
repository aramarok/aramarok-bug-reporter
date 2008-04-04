package com.drey.aramarok.web;

import java.text.SimpleDateFormat;

import com.drey.aramarok.domain.model.Comment;

public class CommentsWrapperBean {

	private Comment comment = null;
	
	public CommentsWrapperBean(Comment comment){
		this.comment = comment;
	}
	
	public String getCommentText(){
		return comment.getCommentText();
	}
	
	public String getCommentDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss ");
		return formatter.format(comment.getDatePosted());
	}
	
	public Long getCommentId(){
		return comment.getId();
	}
	
}
