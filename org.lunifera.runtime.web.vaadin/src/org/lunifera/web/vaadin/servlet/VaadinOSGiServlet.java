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
 * 				- org.vaadin.osgi.VaadinOSGiServlet from Chris Brind
 *				- com.c4biz.osgiutils.vaadin.equinox.shiro.VaadinOSGiServlet from Cristiano Gaviao
 *				- org.vaadin.artur.icepush.ICEPushServlet from Arthur Signell
 *
 * Contributors:
 *    Florian Pirchner - migrated to vaadin 7 and copied into org.lunifera namespace
 *    
 *******************************************************************************/
package org.lunifera.web.vaadin.servlet;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.icepush.servlet.MainServlet;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.artur.icepush.JavascriptProvider;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

/**
 * Used to create instances of applications that have been registered with the
 * container via a component factory.
 * 
 */
@SuppressWarnings("rawtypes")
public class VaadinOSGiServlet extends VaadinServlet implements
		OSGiServletService.IVaadinSessionManager {

	private static final long serialVersionUID = 1L;

	private final ComponentFactory factory;
	private Set<VaadinSessionInfo> sessions = new HashSet<VaadinSessionInfo>();
	private Dictionary properties;

	private MainServlet iCEPushServlet;
	private JavascriptProvider javascriptProvider;

	public VaadinOSGiServlet(ComponentFactory factory, Dictionary properties) {
		this.factory = factory;
		this.properties = properties;
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

		iCEPushServlet = new MainServlet(servletConfig.getServletContext());

		try {
			javascriptProvider = new JavascriptProvider(getServletContext()
					.getContextPath());

			ICEPush.setCodeJavascriptLocation(javascriptProvider
					.getCodeLocation());
		} catch (IOException e) {
			throw new ServletException("Error initializing JavascriptProvider",
					e);
		}
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null
				&& pathInfo.equals("/" + javascriptProvider.getCodeName())) {
			// Serve icepush.js
			serveIcePushCode(request, response);
			return;
		}

		if (request.getRequestURI().endsWith(".icepush")) {
			// Push request
			try {
				iCEPushServlet.service(request, response);
			} catch (ServletException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			// Vaadin request
			super.service(request, response);
		}
	}

	private void serveIcePushCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String icepushJavscript = javascriptProvider.getJavaScript();

		response.setHeader("Content-Type", "text/javascript");
		response.getOutputStream().write(icepushJavscript.getBytes());
	}

	@Override
	protected OSGiServletService createServletService(
			DeploymentConfiguration deploymentConfiguration) {
		return new OSGiServletService(this, deploymentConfiguration, this);
	}

	@Override
	public VaadinSession createVaadinSession(VaadinRequest request,
			HttpServletRequest httpServletRequest) {

		ComponentInstance instance = factory.newInstance(properties);
		VaadinSession session = (VaadinSession) instance
				.getInstance();
		final VaadinSessionInfo info = new VaadinSessionInfo(instance,
				httpServletRequest.getSession());

		info.session.setAttribute(VaadinOSGiServlet.class.getName(),
				new HttpSessionListener() {
					@Override
					public void sessionDestroyed(HttpSessionEvent arg0) {
						info.dispose();
					}

					@Override
					public void sessionCreated(HttpSessionEvent arg0) {

					}
				});
		System.out.println("Ready: " + info); //$NON-NLS-1$
		return (VaadinSession) info.instance.getInstance();
	}

	@Override
	public void destroy() {
		super.destroy();

		synchronized (this) {
			HashSet<VaadinSessionInfo> sessions = new HashSet<VaadinSessionInfo>();
			sessions.addAll(this.sessions);
			this.sessions.clear();
			for (VaadinSessionInfo info : sessions) {
				info.dispose();
			}
		}

		iCEPushServlet.shutdown();
	}

	/**
	 * Track the component instance and session. If this is disposed the entire
	 * associated http session is also disposed.
	 */
	class VaadinSessionInfo {

		final ComponentInstance instance;
		final HttpSession session;

		public VaadinSessionInfo(ComponentInstance instance, HttpSession session) {
			this.instance = instance;
			this.session = session;
			sessions.add(this);
		}

		public void dispose() {
			VaadinSessionInfo app = (VaadinSessionInfo) instance.getInstance();
			if (app != null) {
				app.dispose();
			}

			instance.dispose();

			session.removeAttribute(VaadinOSGiServlet.class.getName());
			sessions.remove(this);
		}
	}

}
