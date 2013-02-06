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
package org.lunifera.runtime.web.http.tests.context;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import junit.framework.Assert;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletMapping;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpApplication;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.http.internal.ServletContextHandler;
import org.lunifera.runtime.web.http.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@SuppressWarnings("restriction")
public class HttpApplicationTest {

	private Activator activator;
	private BundleContext context;
	private InternalHttpApplication application;

	@Before
	public void setup() throws ConfigurationException {
		activator = Activator.getInstance();
		context = Activator.context;
		application = new InternalHttpApplication("App1");
		application.updated(prepareDefaultProps());
	}

	@Test
	public void test_update() throws ConfigurationException {
		application = new InternalHttpApplication();
		Assert.assertNull(application.getId());
		Assert.assertNull(application.getName());
		Assert.assertNull(application.getContextPath());

		Dictionary<String, Object> props = prepareDefaultProps();
		application.updated(props);

		Assert.assertNull(application.getId());
		Assert.assertEquals("Application1", application.getName());
		Assert.assertEquals("/test/app1", application.getContextPath());
	}

	@Test
	public void test_start_stop() throws ConfigurationException {
		Assert.assertEquals(0, activator.getHttpServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

		application.start();
		Assert.assertEquals(1, activator.getHttpServices().size());
		Assert.assertEquals(1, activator.getManagedServices().size());

		application.stop();
		Assert.assertEquals(0, activator.getHttpServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

	}

	@Test
	public void test_Properties() throws ConfigurationException {
		application.start();

		try {
			ServiceReference<ManagedService> ref = context
					.getServiceReferences(ManagedService.class,
							"(service.pid=org.lunifera.runtime.web.http.application)")
					.iterator().next();
			String id = (String) ref.getProperty(IHttpApplication.OSGI__ID);
			String name = (String) ref.getProperty(IHttpApplication.OSGI__NAME);
			String path = (String) ref
					.getProperty(IHttpApplication.OSGI__CONTEXT_PATH);
			Assert.assertEquals("App1", id);
			Assert.assertEquals("Application1", name);
			Assert.assertEquals("/test/app1", path);
		} catch (InvalidSyntaxException e) {
			Assert.fail(e.toString());
		}

		ServiceReference<HttpService> ref = context
				.getServiceReference(HttpService.class);
		String id = (String) ref.getProperty(IHttpApplication.OSGI__ID);
		String name = (String) ref.getProperty(IHttpApplication.OSGI__NAME);
		String path = (String) ref
				.getProperty(IHttpApplication.OSGI__CONTEXT_PATH);
		Assert.assertEquals("App1", id);
		Assert.assertEquals("Application1", name);
		Assert.assertEquals("/test/app1", path);

		application.stop();
	}

	@Test
	public void test_registerServlet() throws ConfigurationException,
			ServletException, NamespaceException {
		application.start();

		ServletContextHandler contexthandler = application.getServletContext();
		ServletMapping[] mappings = contexthandler.getServletHandler()
				.getServletMappings();
		Assert.assertNull(mappings);

		HttpService service = activator.getHttpServices().get(0);

		InternalServlet servlet = new InternalServlet();
		service.registerServlet("/test", servlet, null, null);

		mappings = contexthandler.getServletHandler().getServletMappings();
		Assert.assertEquals(1, mappings.length);
		Assert.assertEquals("/test", mappings[0].getPathSpecs()[0]);

		service.unregister("/test");
		mappings = contexthandler.getServletHandler().getServletMappings();
		Assert.assertEquals(0, mappings.length);

		application.stop();
	}

	@Test
	public void test_registerServlet_twice() throws ConfigurationException,
			ServletException, NamespaceException {
		application.start();

		HttpService service = activator.getHttpServices().get(0);
		InternalServlet servlet = new InternalServlet();
		service.registerServlet("/test", servlet, null, null);
		try {
			service.registerServlet("/test", servlet, null, null);
			Assert.fail();
		} catch (NamespaceException e) {
			// expected
		}

		application.stop();
	}

	@Test
	public void test_unregisterServlet_twice() throws ConfigurationException,
			ServletException, NamespaceException {
		application.start();

		HttpService service = activator.getHttpServices().get(0);
		InternalServlet servlet = new InternalServlet();
		service.registerServlet("/test", servlet, null, null);
		try {
			service.unregister("/test");
			service.unregister("/test");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Alias /test was not registered",
					e.getMessage());
		}

		application.stop();
	}

	@Test
	public void test_registerFilter() throws ConfigurationException,
			ServletException, NamespaceException {
		application.start();
		try {
			ServletContextHandler contexthandler = application
					.getServletContext();
			FilterMapping[] mappings = contexthandler.getServletHandler()
					.getFilterMappings();
			Assert.assertNull(mappings);

			ExtendedHttpService service = activator.getHttpServices().get(0);

			InternalFilter filter = new InternalFilter();
			service.registerFilter("/test", filter, null, null);

			mappings = contexthandler.getServletHandler().getFilterMappings();
			Assert.assertEquals(1, mappings.length);
			Assert.assertEquals("/test", mappings[0].getPathSpecs()[0]);

			service.unregisterFilter(filter);
			mappings = contexthandler.getServletHandler().getFilterMappings();
			Assert.assertEquals(0, mappings.length);
		} finally {
			application.stop();
		}
	}

	@Test
	public void test_registerFilter_twice() throws ConfigurationException,
			ServletException, NamespaceException {

		try {
			application.stop();
			application.start();

			ExtendedHttpService service = activator.getHttpServices().get(0);
			InternalFilter filter = new InternalFilter();
			service.registerFilter("/test", filter, null, null);
			service.registerFilter("/test", filter, null, null);

		} finally {
			application.stop();
		}
	}

	@Test
	public void test_unregisterFilter_twice() throws ConfigurationException,
			ServletException, NamespaceException {
		application.start();

		ExtendedHttpService service = activator.getHttpServices().get(0);
		InternalFilter filter = new InternalFilter();
		InternalServlet servlet = new InternalServlet();
		service.registerServlet("/test", servlet, null, null);
		service.registerFilter("/test", filter, null, null);
		try {
			service.unregisterFilter(filter);
			service.unregisterFilter(filter);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Alias /test was not registered",
					e.getMessage());
		}

		application.stop();
	}

	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(IHttpApplication.OSGI__ID, "App1");
		props.put(IHttpApplication.OSGI__NAME, "Application1");
		props.put(IHttpApplication.OSGI__CONTEXT_PATH, "/test/app1");
		return props;
	}

	public static class InternalHttpApplication extends HttpApplication {

		private String id;

		public InternalHttpApplication() {
			setBundleContext(Activator.context);
		}

		public InternalHttpApplication(String id) {
			setBundleContext(Activator.context);
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id != null ? id : super.getId();
		}

		@Override
		public void setBundleContext(BundleContext context) {
			super.setBundleContext(context);
		}

		@Override
		public ServletContextHandler getServletContext() {
			return super.getServletContext();
		}

	}

	private static class InternalServlet extends HttpServlet {

		private boolean initCalled;

		@Override
		public void init() throws ServletException {
			initCalled = true;
			super.init();
		}
	}

	private static class InternalFilter implements Filter {

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {

		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {

		}

		@Override
		public void destroy() {

		}

	}
}