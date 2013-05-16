/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Information:
 * 		Based on original sources of 
 *				- org.vaadin.artur.icepush.ICEPushServlet from Arthur Signell
 *
 * Contributors:
 *    Florian Pirchner - implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.osgi.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

/**
 * Used to create instances of applications that have been registered with the
 * container via a component factory.
 * 
 */
public class VaadinOSGiServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	private IVaadinApplication webApplication;

	/**
	 * Default constructor.
	 * 
	 * @param webApplication
	 *            The vaadin web application.
	 */
	public VaadinOSGiServlet(IVaadinApplication webApplication) {
		this.webApplication = webApplication;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try {
			super.init(servletConfig);
		} catch (ServletException e) {
			if (e.getMessage().equals(
					"Application not specified in servlet parameters")) {
				// Ignore if application is not specified to allow the same
				// servlet to be used for only push in portals
			} else {
				throw e;
			}
		}

	}

	@Override
	protected OSGiServletService createServletService(
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {

		// create the servlet service initialized with the ui provider
		OSGiServletService service = new OSGiServletService(this,
				deploymentConfiguration,
				new OSGiServletService.IVaadinSessionFactory() {
					@Override
					public VaadinSession createSession(VaadinRequest request,
							HttpServletRequest httpServletRequest) {
						VaadinSession session = new VaadinSession(request
								.getService());
						for (UIProvider provider : webApplication
								.getUiProviders()) {
							session.addUIProvider(provider);
						}
						return session;
					}
				});
		service.init();
		return service;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
