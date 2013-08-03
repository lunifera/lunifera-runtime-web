/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.vaadin.osgi.tests.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.tests.Activator;
import org.lunifera.runtime.web.vaadin.osgi.webapp.VaadinApplication;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.NamespaceException;

@SuppressWarnings("restriction")
public class VaadinApplicationTest {

	private Activator activator;
	private InternalVaadinApplication application;

	/**
	 * Setup of the test.
	 * 
	 * @throws ConfigurationException
	 */
	@Before
	public void setup() throws ConfigurationException {
		activator = Activator.getInstance();
		application = new InternalVaadinApplication("App1");
		application.setName("Application1");
		application.setUIAlias("alias1");
		application.setWidgetSetName("widgetset1");
		application.setHttpApplication("http1");
	}

	@After
	public void tearDown() {
		if (application.isStarted()) {
			application.destroy();
		}
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 */
	@Test
	public void test_start_stop() throws ConfigurationException {
		waitCM();
		waitCM();
		waitCM();

		assertEquals(0, activator.getVaadinApplications().size());
		application.start();
		assertEquals(0, activator.getVaadinApplications().size());
		application.stop();
		assertEquals(0, activator.getVaadinApplications().size());
		application.destroy();
		assertEquals(0, activator.getVaadinApplications().size());
	}

	private void waitCM() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_start_stop_destroy() throws ConfigurationException,
			InvalidSyntaxException, ServletException, NamespaceException {

		HttpServiceImpl service = new HttpServiceImpl();
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		ServiceRegistration<ExtendedHttpService> reg = Activator.context
				.registerService(ExtendedHttpService.class, service, props);
		try {
			assertNull(service.servletAlias);
			assertNull(service.filterAlias);
			assertNull(service.resourceAlias);
			assertNull(service.servletAlias);
			assertNull(service.resourceName);
			assertNull(service.filter);
			assertNull(service.servlet);

			application.start();

			assertNotNull(service.servlet);
			assertNotNull(service.filterAlias);
			assertNotNull(service.resourceAlias);
			assertNotNull(service.servletAlias);
			assertEquals("/alias1", service.servletAlias);
			assertNotNull(service.resourceName);
			assertNotNull(service.filter);
			assertNotNull(service.servlet);

			application.stop();

			assertNull(service.servlet);
			assertNull(service.filterAlias);
			assertNull(service.resourceAlias);
			assertNull(service.servletAlias);
			assertNull(service.resourceName);
			assertNull(service.filter);
			assertNull(service.servlet);
		} finally {
			reg.unregister();
			application.destroy();
		}
	}

	/**
	 * Creates default properties for the tests.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application1");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app1");
		return props;
	}

	/**
	 * An internal helper application.
	 */
	public static class InternalVaadinApplication extends VaadinApplication {

		public InternalVaadinApplication() {
			this("App1");
		}

		public InternalVaadinApplication(String id) {
			super(id, Activator.context);
		}
	}

	@SuppressWarnings("rawtypes")
	private static class HttpServiceImpl implements ExtendedHttpService {

		private String servletAlias;
		private Servlet servlet;
		private String resourceAlias;
		private String resourceName;
		private Filter filter;
		private String filterAlias;

		@Override
		public void registerServlet(String alias, Servlet servlet,
				Dictionary initparams, HttpContext context)
				throws ServletException, NamespaceException {
			this.servletAlias = alias;
			this.servlet = servlet;
		}

		@Override
		public void registerResources(String alias, String name,
				HttpContext context) throws NamespaceException {
			this.resourceAlias = alias;
			this.resourceName = name;
		}

		@Override
		public void unregister(String alias) {
			if (alias.equals(servletAlias)) {
				servletAlias = null;
				servlet = null;
			} else if (alias.equals(resourceAlias)) {
				resourceAlias = null;
				resourceName = null;
			}
		}

		@Override
		public HttpContext createDefaultHttpContext() {
			return null;
		}

		@Override
		public void registerFilter(String alias, Filter filter,
				Dictionary initparams, HttpContext context)
				throws ServletException, NamespaceException {
			this.filterAlias = alias;
			this.filter = filter;
		}

		@Override
		public void unregisterFilter(Filter filter) {
			if (this.filter == filter) {
				this.filter = null;
				this.filterAlias = null;
			}
		}

	}

}
