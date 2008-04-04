package com.drey.aramarok.web;

import com.drey.aramarok.domain.Right;
import com.drey.aramarok.web.util.WebUtil;

public class RightsBean {
	
	public boolean isEnterNewBugRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.ENTER_NEW_BUG);
		}
		return false;
	}
	
	public boolean isSearchBugsRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.SEARCH_BUGS);
		}
		return false;
	}
	
	public boolean isEditPreferencesRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.EDIT_PREFERENCES);
		}
		return false;
	}
	
	public boolean isDefineProductsRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.DEFINE_PRODUCTS);
		}
		return false;
	}
	
	public boolean isDefineComponentsRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.DEFINE_COMPONENTS);
		}
		return false;
	}
	
	public boolean isDefineVersionsRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.DEFINE_VERSIONS);
		}
		return false;
	}
	
	public boolean isDefineAramarokOptionsRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.DEFINE_ARAMAROK_OPTIONS);
		}
		return false;
	}
	
	public boolean isEditOtherUsersRight() {
		if(WebUtil.getUser() != null)
		{
			return WebUtil.getUser().hasRight(Right.EDIT_OTHER_USERS);
		}
		return false;
	}
}
