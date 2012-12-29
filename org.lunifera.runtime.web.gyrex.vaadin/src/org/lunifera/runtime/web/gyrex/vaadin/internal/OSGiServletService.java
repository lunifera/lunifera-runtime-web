/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.gyrex.vaadin.internal;

import java.util.Dictionary;

import javax.servlet.http.HttpServletRequest;

import org.lunifera.runtime.web.gyrex.vaadin.IVaadinGyrexConstants;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class OSGiServletService extends VaadinServletService {

	private final IVaadinSessionFactory factory;
	@SuppressWarnings("rawtypes")
	private Dictionary webAppProperties;

	public OSGiServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration,
			@SuppressWarnings("rawtypes") Dictionary webAppProperties,
			IVaadinSessionFactory factory) {
		super(servlet, deploymentConfiguration);

		this.webAppProperties = webAppProperties;
		this.factory = factory;
	}

	@Override
	public String getConfiguredWidgetset(VaadinRequest request) {
		String result = (String) webAppProperties
				.get(IVaadinGyrexConstants.PROP_WIDGETSET);
		if (result == null || result.equals("")) {
			return super.getConfiguredWidgetset(request);
		}

		return result;
	}

	@Override
	protected VaadinSession createVaadinSession(VaadinRequest request)
			throws ServiceException {
		return factory.createSession(request, getCurrentServletRequest());
	}

	/**
	 * Creates new instances of vaadin sessions.
	 */
	public interface IVaadinSessionFactory {
		/**
		 * Returns a new instance of a vaadin session.
		 * 
		 * @param request
		 * @param httpServletRequest
		 * 
		 * @return
		 */
		VaadinSession createSession(VaadinRequest request,
				HttpServletRequest httpServletRequest);
	}
}
