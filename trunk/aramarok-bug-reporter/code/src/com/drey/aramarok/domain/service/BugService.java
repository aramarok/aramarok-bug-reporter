package com.drey.aramarok.domain.service;

import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import com.drey.aramarok.domain.exceptions.bug.BugException;
import com.drey.aramarok.domain.exceptions.user.UserException;
import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.User;
import com.drey.aramarok.domain.model.filters.BugFilter;

public interface BugService {
	
	public Bug getBug(Long bugId) throws PersistenceException ;
	
	public List<Bug> getBugs(BugFilter bugFilter) throws PersistenceException ;
	
	public Long commitNewBug(Bug bug, User owner, Set<User> ccUsers) throws PersistenceException, BugException;
	
	public Long commitBug(Bug bug, User logedInUser) throws PersistenceException, BugException, UserException;
}