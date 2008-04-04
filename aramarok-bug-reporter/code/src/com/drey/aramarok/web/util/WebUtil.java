package com.drey.aramarok.web.util;

import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.DomainFacade;
import com.drey.aramarok.domain.User;
import com.drey.aramarok.web.SynchronizationObject;

public class WebUtil implements WebUtilConstants{

	private static Logger log = Logger.getLogger(WebUtil.class);
		
	/**
	 * Returns current web session. 
	 * If it does not exits creates a new one.
	 * @return
	 */
	static public HttpSession getHttpSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc != null) {
			ExternalContext ec = fc.getExternalContext();
			if(ec != null) {
				HttpSession session = (HttpSession)ec.getSession(true);
				return session;
			}
		}
		return null;
	}
	
	
	/**
	 * This method gets the DomainFacade from web session, if not found it will get it form JDNI.
	 * Returns DomainFacade if found.
	 * Returns null if DomainFacade is not found.
	 * @return 
	 */
	static public DomainFacade getDomainFacade(HttpSession session) {
		DomainFacade domainFacade = null;
		if (session != null) {
			domainFacade = (DomainFacade) session.getAttribute(WebUtilConstants.DOMAIN_FACADE); 
		}
		
		if (domainFacade==null) {
			Context ctx;
			try {
				ctx = new InitialContext();
				domainFacade = (DomainFacade) ctx.lookup(WebUtilConstants.DOMAIN_FACADE_LOOKUP_NAME);
			}
			catch(NamingException e) {
				log.error("Naming exception, when looking for DomainFacade in JDNI. Message: "+e.getMessage());
			}
			
			if(domainFacade != null) {
				session.setAttribute(WebUtilConstants.DOMAIN_FACADE, domainFacade);
			}
		}
		return domainFacade;
	}
	
	
	/**
	 * This method gets the DomainFacade from web session, if not found it will get it form JDNI.
	 * Returns DomainFacade if found.
	 * Returns null if DomainFacade is not found.
	 * @return 
	 */
	static public DomainFacade getDomainFacade() {
		return getDomainFacade(getHttpSession());
	}
	
	/**
	 * Use this method to save curent user's employee data to web session
	 * @param emplyee
	 */
	static public void setUser(User user, HttpSession session) {
		if (session!=null)
			session.setAttribute(WebUtilConstants.USER, user);
	}
		
	
	/**
	 * Use this method to save curent user's employee data to web session
	 * @param emplyee
	 */
	static public void setUser(User user) {
		setUser(user,getHttpSession());
	}
	
	/**
	 * Use this method to get curent user's employee data from web session
	 * @param emplyee
	 */
	static public User getUser(HttpSession session) {
		if(session != null) {
			return (User) session.getAttribute(WebUtilConstants.USER);	
		}
		return null;
	}	
	
	/**
	 * Use this method to get curent user's employee data from web session
	 * @param emplyee
	 */
	static public User getUser() {
		return getUser(getHttpSession());
	}
	
	
	/**
	 * @return current locale used in application if available or returns default empty locale if locale are not set
	 */
	public static Locale getLocale()
	{
		Locale locale = new Locale("ro");		
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc != null) {
			if(fc.getViewRoot() != null && fc.getViewRoot().getLocale() != null) {
				return fc.getViewRoot().getLocale();
			}
		}
		return locale;
	}
	
	
	/**
	 * Create synchronization object
	 * 
	 */	
	public static SynchronizationObject CreateSynchronizationObject(HttpSession session){
		SynchronizationObject syncObj = new SynchronizationObject();
		if (session!=null) {
			session.setAttribute(WebUtil.SYNCRONIZATION_OBJECT, syncObj);
			return syncObj;
		}
		else {
			log.error("CreateSynchronizationObject - session is null!");
		}
		return null;
	}
	
	
	/**
	 *  Get synchronization object
	 */	
	public static SynchronizationObject GetSynchronizationObject(HttpSession session){
		SynchronizationObject syncObj = null;
		
		if (session!=null) {
			Object osync = session.getAttribute(WebUtil.SYNCRONIZATION_OBJECT);
			if (osync!=null && osync instanceof SynchronizationObject){
				syncObj = (SynchronizationObject) osync;
				return syncObj;
			}
			else {
				return CreateSynchronizationObject(session);
			}
		}
		log.error("GetSynchronizationObject - session object is null.");
		return null;
	}
	
	/**
	 * Used to invalidate current session on logout action
	 *
	 */
	public static void invalidateSession() 
	{
		HttpSession session =  getHttpSession();
		if(session != null)
		{
			session.invalidate();
		}
	}
}
