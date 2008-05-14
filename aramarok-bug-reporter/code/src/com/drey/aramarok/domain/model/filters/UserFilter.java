package com.drey.aramarok.domain.model.filters;

import java.util.List;

import com.drey.aramarok.domain.model.Right;
import com.drey.aramarok.domain.model.UserStatus;

public class UserFilter {

	private static final long serialVersionUID = 0123;
	
	private List<Long> userIdList = null;
	private List<String> userNameList = null;
	private List<UserStatus> userStatusList = null;
	private List<Right> rightList = null;
	private UserSortingMode sortingMode = UserSortingMode.ID_ASC;
	
	
	public List<Long> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}
	public List<String> getUserNameList() {
		return userNameList;
	}
	public void setUserNameList(List<String> userNameList) {
		this.userNameList = userNameList;
	}
	public List<UserStatus> getUserStatusList() {
		return userStatusList;
	}
	public void setUserStatusList(List<UserStatus> userStatusList) {
		this.userStatusList = userStatusList;
	}
	public List<Right> getRightList() {
		return rightList;
	}
	public void setRightList(List<Right> rightList) {
		this.rightList = rightList;
	}
	public UserSortingMode getSortingMode() {
		return sortingMode;
	}
	public void setSortingMode(UserSortingMode sortingMode) {
		this.sortingMode = sortingMode;
	}	
}