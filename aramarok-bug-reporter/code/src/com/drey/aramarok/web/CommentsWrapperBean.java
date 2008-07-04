package com.drey.aramarok.web;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.exceptions.ExternalSystemException;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.service.DomainFacade;
import com.drey.aramarok.web.util.WebUtil;

public class CommentsWrapperBean {

	private Logger log = Logger.getLogger(CommentsWrapperBean.class);
	
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
	
	public String getCommentOwner(){
		return comment.getUser().getUserName();
	}
	
	public String getVotes(){
		return String.valueOf(comment.getVotes());
	}
	
	public boolean isCanUserVote(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			User user = facade.getUser(WebUtil.getUser().getUserName(), true); //WebUtil.getUser();
			for (Comment c: user.getVotedComments()){
				if (c.getId().compareTo(comment.getId())==0){
					return false;
				}
			}
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException!! ");
		}
		
		return true;
	}
	
	public void vote(){
		DomainFacade facade = WebUtil.getDomainFacade();
		try {
			facade.voteComment(comment.getId());
			comment = facade.getComment(comment.getId());
		} catch (ExternalSystemException e) {
			log.error("ExternalSystemException!! ");
		}
	}
	
}
