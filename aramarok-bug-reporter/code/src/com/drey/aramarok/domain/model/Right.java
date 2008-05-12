package com.drey.aramarok.domain.model;

public enum Right {
	NONE,
	
	ENTER_NEW_BUG, //allow user to commit a new bug
	SEARCH_BUGS, //allow user to search bugs 
	EDIT_PREFERENCES, //allow user to edit it's own profile preferences
	CHANGE_BUG_STATUS, //allow user to change the status of the bug if it is the reporter or the resolver of the bug
	ADD_BUG_COMMENT, //allow user to add a bug comment
	EDIT_BUG, //allow user to edit bug information if it is the reporter or the resolver of the bug
	EDIT_BUGS, //allow user to edit all other bugs and change their statuses
	DEFINE_PRODUCTS, //allow user to define the products in the system
	DEFINE_COMPONENTS, //allow user to define the components in the system
	DEFINE_VERSIONS, //allow user to define the versions in the system
	DEFINE_ARAMAROK_OPTIONS, //allow user to edit aramarok's options
	EDIT_OTHER_USERS; //allow user to edit other user's profiles information
	
	public static Right getRightByName(String s){
		if (s != null && !s.isEmpty()){
			Right[] rights = Right.values();
			for(int i=0;i<rights.length;i++){
				if (rights[i].name().compareTo(s)==0){
					return rights[i];
				}
			}
		}
		return null;
	}
}
