package com.drey.aramarok.web.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.drey.aramarok.domain.service.DomainFacade;

public class AramarokHttpSessionListener implements HttpSessionListener {
	private static Logger log = Logger.getLogger(AramarokHttpSessionListener.class);

	public void sessionCreated(HttpSessionEvent evt) {
		log.info("Session created");
	}

	public void sessionDestroyed(HttpSessionEvent evt) {
		try {
			if(evt.getSession() != null) {
				DomainFacade facade = (DomainFacade)evt.getSession().getAttribute(WebUtilConstants.DOMAIN_FACADE);
				if(facade != null) {
					facade.stopSessionBean();
				}
				else {
					log.error("UNABLE TO INVALIDATE FACADE BECAUSE IS NULL");
				}
			} else {
				log.error("SESSION IS NULL");
			}
		} catch(Exception e) {
			log.error("AramarokHttpSessionListener.sessionDestroyed FAILED", e);
		}
	}
}