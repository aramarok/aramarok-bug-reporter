package com.drey.aramarok.web.util;

public interface WebUtilConstants {
	
	/**
	 * Constant for successful login action.
	 * Used in navigation
	 */
	public static final String SUCCESS_LOGIN_OUTCOME = "SUCCESS_LOGIN_OUTCOME";
	
	/**
	 * Constant for unsuccessful login action.
	 * Used in navigation
	 */
	public static final String FAIL_LOGIN_OUTCOME = "FAIL_LOGIN_OUTCOME";
	
	/**
	 * Constant for logout action
	 * Used in navigation
	 */
	public static final String LOGOUT_OUTCOME="LOGOUT_OUTCOME";
	
	/**
	 * Constant for registering new account action.
	 * Used in navigation
	 */
	public static final String REGISTER_NEW_ACCOUNT_OUTCOME = "REGISTER_NEW_ACCOUNT_OUTCOME";
	
	/**
	 * Constant for successful register action.
	 * Used in navigation
	 */
	public static final String SUCCESS_REGISTER_NEW_ACCOUNT_OUTCOME = "SUCCESS_REGISTER_NEW_ACCOUNT_OUTCOME";
	
	/**
	 * Constant for error page
	 * Used in navigation 
	 */
	public static final String ERROR_PAGE_OUTCOME="ERROR_PAGE_OUTCOME";
	
	/**
	 * Constant for home page.
	 * Used in navigation
	 */
	public static final String HOME_OUTCOME = "HOME_OUTCOME";
	
	/**
	 * Constant for reporting a new bug page.
	 * Used in navigation
	 */
	public static final String NEW_BUG_OUTCOME = "NEW_BUG_OUTCOME";

	/**
	 * Constant for entering a new bug.
	 * Used in navigation
	 */
	public static final String NEW_BUG_ENTER_OUTCOME = "NEW_BUG_ENTER_OUTCOME";
	
	/**
	 * Constant for successfully entering a new bug.
	 * Used in navigation
	 */
	public static final String NEW_BUG_SUCCESSFULLY_ENTER_OUTCOME = "NEW_BUG_SUCCESSFULLY_ENTER_OUTCOME";
	
	/**
	 * 
	 */
	public static final String VIEW_BUG_OUTCOME = "VIEW_BUG_OUTCOME";
	
	/**
	 * Constant for searching bugs page.
	 * Used in navigation
	 */
	public static final String SEARCH_BUGS_OUTCOME = "SEARCH_BUGS_OUTCOME";
	
	/**
	 * Constant for my bugs page.
	 * Used in navigation
	 */
	public static final String MY_BUGS_OUTCOME = "MY_BUGS_OUTCOME";
	
	/**
	 * Constant for preferences page.
	 * Used in navigation
	 */
	public static final String PREFERENCES_OUTCOME = "PREFERENCES_OUTCOME";
	
	/**
	 * Constant for definig products page.
	 * Used in navigation
	 */
	public static final String PRODUCTS_OUTCOME = "PRODUCTS_OUTCOME";
	
	/**
	 * Constant for definig components page.
	 * Used in navigation
	 */
	public static final String COMPONENTS_OUTCOME = "COMPONENTS_OUTCOME";
	
	/**
	 * Constant for definig versions page.
	 * Used in navigation
	 */
	public static final String VERSIONS_OUTCOME = "VERSIONS_OUTCOME";
	
	/**
	 * Constant for the system options page.
	 * Used in navigation
	 */
	public static final String SYS_OPTIONS_OUTCOME = "SYS_OPTIONS_OUTCOME";
	
	/**
	 * Constant for edit users page.
	 * Used in navigation
	 */
	public static final String USERS_OUTCOME = "USERS_OUTCOME";
	
	
	
	
	/**
	 * Constant used to save Syncronization Object
	 */	
	public static final String SYNCRONIZATION_OBJECT = "SYNCRONIZATION_OBJECT";
	
	/**
	 * Constatnt used to save current DomainFacade instance in session for future use.
	 */
	public static final String DOMAIN_FACADE = "DOMAIN_FACADE";
	
	/**
	 * Constatnt used to lookup DomainFacade class in JDNI.
	 */
	public static final String DOMAIN_FACADE_LOOKUP_NAME = "java:/DomainFacade";
	
	/**
	 * Constant used to save curent user instance in session for future use.
	 */
	public static final String USER = "USER";
	
	/**
	 * Constant used to identify locale object in session
	 */
	public static final String LOCALE = "LOCALE";
	
	
	public static final String ProductToEnterNewBug = "PRODUCT_TO_ENTER_BUG";
	public static final String ENTER_NEW_BUG_FOR_PRODUCT = "ENTER_NEW_BUG_FOR_PRODUCT";
	public static final String NEW_INSERED_BUG_ID = "NEW_INSERED_BUG_ID"; 

	public static final String BUG_ID_SELECTED_FOR_VIEWING = "BUG_ID_SELECTED_FOR_VIEWING";
}