package com.drey.aramarok.domain.service;

import java.util.List;

import com.drey.aramarok.domain.model.Bug;
import com.drey.aramarok.domain.model.Comment;
import com.drey.aramarok.domain.model.ComponentVersion;
import com.drey.aramarok.domain.model.OperatingSystem;
import com.drey.aramarok.domain.model.Platform;
import com.drey.aramarok.domain.model.Priority;
import com.drey.aramarok.domain.model.Product;
import com.drey.aramarok.domain.model.ProductComponent;
import com.drey.aramarok.domain.model.Severity;
import com.drey.aramarok.domain.model.User;

public interface DomainService {
	
	public User findUser(String userName);
	
	public Product findProduct(String productName);
	
	public ProductComponent findProductComponent(String componentName);
	
	public ComponentVersion findComponentVersion(String versionName);
	
	public String currentDateAndTime();
	
	public Bug getBug(Long bugId);
	
	public List<Comment> getComments(CommentFilter commentFilter);

	public List<Bug> getBugs(BugFilter bugFilter);
	
	public Priority getPriority(String priorityName);
	
	public Severity getSeverity(String severityName);
	
	public OperatingSystem getOperatingSystem(String operatingSystemName);
	
	public Platform getPlatform(String platformName);
}
