package com.drey.aramarok.domain.model.filters;

import java.util.List;

public class CommentFilter {
	
	private static final long serialVersionUID = 0;
	
	private List<Long> bugIdList = null;
	
	private CommentSortingMode sortingMode = CommentSortingMode.ID_ASC;

	
	public List<Long> getBugIdList() {
		return bugIdList;
	}

	public void setBugIdList(List<Long> bugIdList) {
		this.bugIdList = bugIdList;
	}

	public CommentSortingMode getSortingMode() {
		return sortingMode;
	}

	public void setSortingMode(CommentSortingMode sortingMode) {
		this.sortingMode = sortingMode;
	}
}
