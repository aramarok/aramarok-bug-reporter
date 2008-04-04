package com.drey.aramarok.web;

import com.drey.aramarok.web.util.WebUtil;
import com.drey.aramarok.web.util.WebUtilConstants;

/**
 * Backing bean for logout action
 * 
 * @author Tolnai Andrei
 *
 */

public class LogoutBean {
	
	/**
	 * Method used in logout action.
	 */
	public String logout() {
		WebUtil.invalidateSession();
		return WebUtilConstants.LOGOUT_OUTCOME;
	}
}