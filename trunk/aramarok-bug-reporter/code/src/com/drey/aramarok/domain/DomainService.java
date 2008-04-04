package com.drey.aramarok.domain;

import java.util.List;

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
