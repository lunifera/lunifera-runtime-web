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
package org.lunifera.runtime.web.gyrex.vaadin.internal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lunifera.web.vaadin.common.Constants;
import org.lunifera.web.vaadin.common.OSGiUI;
import org.lunifera.web.vaadin.common.OSGiUIProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class VaadinOSGiServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	// private MainServlet iCEPushServlet;
	// private JavascriptProvider javascriptProvider;

	private UiProviderTracker tracker;

	public VaadinOSGiServlet() {
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

		try {
			tracker = new UiProviderTracker(VaadinActivator.getInstance()
					.getBundle().getBundleContext());
			tracker.open();
		} catch (InvalidSyntaxException e) {
			throw new ServletException(e);
		}

		// iCEPushServlet = new MainServlet(servletConfig.getServletContext());
		//
		// try {
		// javascriptProvider = new JavascriptProvider(getServletContext()
		// .getContextPath());
		//
		// ICEPush.setCodeJavascriptLocation(javascriptProvider
		// .getCodeLocation());
		// } catch (IOException e) {
		// throw new ServletException("Error initializing JavascriptProvider",
		// e);
		// }
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// String pathInfo = request.getPathInfo();
		// if (pathInfo != null
		// && pathInfo.equals("/" + javascriptProvider.getCodeName())) {
		// // Serve icepush.js
		// serveIcePushCode(request, response);
		// return;
		// }
		//
		// if (request.getRequestURI().endsWith(".icepush")) {
		// // Push request
		// try {
		// iCEPushServlet.service(request, response);
		// } catch (ServletException e) {
		// throw e;
		// } catch (IOException e) {
		// throw e;
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
		// } else {
		// Vaadin request
		super.service(request, response);
		// }
	}

	// private void serveIcePushCode(HttpServletRequest request,
	// HttpServletResponse response) throws IOException {
	//
	// String icepushJavscript = javascriptProvider.getJavaScript();
	//
	// response.setHeader("Content-Type", "text/javascript");
	// response.getOutputStream().write(icepushJavscript.getBytes());
	// }

	@Override
	protected OSGiServletService createServletService(
			DeploymentConfiguration deploymentConfiguration) {
		OSGiServletService service = new OSGiServletService(this,
				deploymentConfiguration,
				new OSGiServletService.IVaadinSessionFactory() {
					@Override
					public VaadinSession createSession(VaadinRequest request,
							HttpServletRequest httpServletRequest) {
						VaadinSession session = new VaadinSession(
								request.getService());
						for (UiProviderInfo info : tracker.getInfos()) {
							session.addUIProvider(new OSGiUIProvider(info
									.getFactory(), info.getProviderClass()));
						}
						return session;
					}
				});
		return service;
	}

	@Override
	public void destroy() {
		super.destroy();

		if (tracker != null) {
			tracker.close();
			tracker = null;
		}

		// iCEPushServlet.shutdown();
	}

	@SuppressWarnings("unchecked")
	public class UiProviderTracker extends ServiceTracker {

		private final Logger logger = LoggerFactory
				.getLogger(UiProviderTracker.class);
		private final Set<UiProviderInfo> infos = new HashSet<UiProviderInfo>();

		public UiProviderTracker(BundleContext ctx)
				throws InvalidSyntaxException {
			super(ctx, ctx.createFilter("(component.factory="
					+ Constants.OSGI_COMP_FACTORY__VAADIN_UI + "/*)"), null);
		}

		public Set<UiProviderInfo> getInfos() {
			return infos;
		}

		@Override
		public Object addingService(ServiceReference reference) {
			Object o = super.addingService(reference);

			if (o instanceof ComponentFactory) {
				ComponentFactory factory = (ComponentFactory) o;
				String name = (String) reference
						.getProperty("component.factory");
				String className = name.substring(Constants.PREFIX__UI_CLASS
						.length());
				try {
					Class<? extends OSGiUI> clazz = (Class<? extends OSGiUI>) reference
							.getBundle().loadClass(className);

					infos.add(new UiProviderInfo(factory, clazz));
					logger.info(String.format("Added %s as UiProvider",
							clazz.getName()));
				} catch (ClassNotFoundException e) {
					logger.error("{}", e);
					throw new RuntimeException(e);
				}
			}

			return o;
		}

		@Override
		public void removedService(ServiceReference reference, Object service) {
			if (service instanceof ComponentFactory) {
				ComponentFactory factory = (ComponentFactory) service;
				for (Iterator<UiProviderInfo> iterator = infos.iterator(); iterator
						.hasNext();) {
					UiProviderInfo info = iterator.next();
					if (info.getFactory() == factory) {
						iterator.remove();
						break;
					}
				}
			}
		}
	}

	public static class UiProviderInfo {
		private final ComponentFactory factory;
		private final Class<? extends OSGiUI> clazz;

		public UiProviderInfo(ComponentFactory factory,
				Class<? extends OSGiUI> clazz) {
			super();
			this.factory = factory;
			this.clazz = clazz;
		}

		public ComponentFactory getFactory() {
			return factory;
		}

		public Class<? extends OSGiUI> getProviderClass() {
			return clazz;
		}

	}

}
