package com.drey.aramarok.web.authorization;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.drey.aramarok.domain.Right;
import com.drey.aramarok.domain.Role;
import com.drey.aramarok.domain.User;
import com.drey.aramarok.web.SynchronizationObject;
import com.drey.aramarok.web.util.WebUtil;
import com.drey.aramarok.web.util.WebUtilConstants;

/**
 *	This class is used to filter unauthorized and unauthenticated access to AramaroK bug reporter
 */
public class AuthenticationAndAuthorization implements Filter 
{

	/**
	 * Pages with theirs rights.
	 * Are set in web.xml as init params for this filter.
	 */
	private Hashtable<String, Vector<Right>> pageRights = new Hashtable<String, Vector<Right>>(0);

	/**
	 * Flag to bypas filter when developing
	 * For Release make shure this is set to false
	 */
	private boolean development = false;
	
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession();
		if(development) {
			SynchronizationObject syncObj = WebUtil.GetSynchronizationObject(session);
			if (syncObj != null) {
				synchronized(syncObj){
					filterChain.doFilter(request, response);
				}
			}
			else{
				((HttpServletResponse)response).sendRedirect("error.jsf");
			}
		} else {
			String page = getPageForRequest((HttpServletRequest)request);
			if (page != null) {
				if (isRestrictedPage(page)) {
					User user = getUser(request);
					if (user != null) {
						
						if (userHasRightsOnPage(page, user)) {
							if (session==null) {
								filterChain.doFilter(request, response);
							} else {
								SynchronizationObject syncObj = WebUtil.GetSynchronizationObject(session);
								if (syncObj!=null) {
									synchronized(syncObj) {
										filterChain.doFilter(request, response);
									}
								} else {
									((HttpServletResponse)response).sendRedirect("error.jsf");
								}
							}
						} else {
							((HttpServletResponse)response).sendRedirect("unauthorized.jsf");
						}
					} else {
						((HttpServletResponse)response).sendRedirect("login.jsf");
					}
				} else {
					if (session == null) {
						filterChain.doFilter(request, response);
					} else {
						SynchronizationObject syncObj = WebUtil.GetSynchronizationObject(session);
						if (syncObj!=null) {
							synchronized(syncObj){
								filterChain.doFilter(request, response);
							}
						} else {
							((HttpServletResponse)response).sendRedirect("error.jsf");
						}
					}
				}
			} else {
				if ( session ==null ) {
					filterChain.doFilter(request, response);
				} else {
					SynchronizationObject syncObj = WebUtil.GetSynchronizationObject(session);
					if (syncObj!=null) {
						synchronized(syncObj){
							filterChain.doFilter(request, response);
						}
					} else {
						((HttpServletResponse)response).sendRedirect("error.jsf");
					}
				}
			}
		}
	}

	/**
	 * parse filter configuration and get pages with theirs access rights
	 */
	@SuppressWarnings("unchecked")
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		Enumeration paramNames = filterConfig.getInitParameterNames();
		
		while (paramNames != null && paramNames.hasMoreElements()) {
			String parameterName = (String)paramNames.nextElement();
			if (parameterName.contains(".jsf")) {
				String page = parameterName;
				Vector<Right> rights = new Vector<Right>(0);
				
				String rightsAsString = filterConfig.getInitParameter(page);
				if(rightsAsString != null && !rightsAsString.trim().equals("")) {
					String rgs[] = rightsAsString.split(",");
					for(String r : rgs) {
						rights.add(Right.valueOf(r));
					}
					this.pageRights.put(page, rights);
				}
			}
			if (parameterName.compareTo("Development")==0) {
				String value = filterConfig.getInitParameter(parameterName);
				if (value.compareTo("false")==0){
					this.development = false;
				} else {
					this.development = true;
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param request
	 * @return The page that was requested by the user.
	 */
	private String getPageForRequest(HttpServletRequest request) 
	{
		return request.getServletPath();
	}
	
	/**
	 * Check if the page has restricted access
	 * @param page
	 * @return true if it is a restricted access page else false
	 */
	@SuppressWarnings("unchecked")
	private boolean isRestrictedPage(String page) 
	{
		Enumeration keys = pageRights.keys();
		boolean isRestricted = false;
		while(keys.hasMoreElements())
		{
			String key = (String)keys.nextElement(); 
			if(key.equals(page))
			{
				isRestricted = true;
				break;
			}
		}
		return isRestricted;
	}
	
	/**
	 * Get user from current request
	 * This method is used because at this point FacesContext is not available
	 * @param request
	 * @return null if a user cannot be retrieved or the current logged in user
	 */
	private User getUser(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession(false);
		if(session != null) {
			User user = (User)session.getAttribute(WebUtilConstants.USER);
			return user;
		}
		return null;
	}
	
	private boolean userHasRightsOnPage(String page, User user) {
		Vector<Right> rights = (Vector<Right>)pageRights.get(page);
		if(rights != null) {
			
			if (rights.contains(Right.NONE)) 
				return true;
			
			//Set<Role> roles = user.getRoles();
			
			//for(Iterator<Role> i=roles.iterator();i.hasNext();){
				Role role = user.getRole(); //i.next();
				Set<Right> emp_rights=role.getRights();
				for(Iterator<Right> j=emp_rights.iterator();j.hasNext();){
					Right right = j.next();
					if (rights.contains(right)){
						return true;
					}
				}
			//}
		}
		
		return false;
	}
}