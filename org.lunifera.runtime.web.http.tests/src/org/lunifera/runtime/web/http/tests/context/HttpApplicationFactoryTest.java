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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.http.tests.Activator;
import org.lunifera.runtime.web.jetty.IHandlerProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.http.HttpService;

public class HttpApplicationFactoryTest {

	private ConfigurationAdmin cm;
	private Activator activator;

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 */
	@Before
	public void setup() throws ConfigurationException {
		cm = Activator.getInstance().getConfigurationAdmin();
		activator = Activator.getInstance();

		Bundle bundle = Activator.context
				.getBundle("org.eclipse.equinox.http.jetty");
		if (bundle != null) {
			try {
				bundle.stop();
			} catch (BundleException e) {
			}
		}
	}

	/**
	 * Test that a http application starts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_startWebApplication() throws IOException {
		Assert.assertEquals(0, activator.getHttpApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());
		Assert.assertEquals(0, activator.getHandlerProvider().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getHttpApplications().size());
		Assert.assertEquals(1, activator.getHttpServices().size());
		Assert.assertEquals(1, activator.getHandlerProvider().size());

		// update instance
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getHttpApplications().size());
		Assert.assertEquals(1, activator.getHttpServices().size());
		Assert.assertEquals(1, activator.getHandlerProvider().size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		config2.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(2, activator.getHttpApplications().size());
		Assert.assertEquals(2, activator.getHttpServices().size());
		Assert.assertEquals(2, activator.getHandlerProvider().size());

		// remove instance 1
		config.delete();
		waitCM();
		Assert.assertEquals(1, activator.getHttpApplications().size());
		Assert.assertEquals(1, activator.getHttpServices().size());
		Assert.assertEquals(1, activator.getHandlerProvider().size());

		// remove instance 2
		config2.delete();
		waitCM();
		Assert.assertEquals(0, activator.getHttpApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());
		Assert.assertEquals(0, activator.getHandlerProvider().size());
	}

	/**
	 * Tests the use of properties in the services.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	@Test
	public void test_properties() throws IOException, InvalidSyntaxException {
		waitCM();

		Assert.assertEquals(0, activator.getHttpApplications().size());

		// HttpApplications
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server2)").size());

		// HttpService
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server2)").size());

		// HandlerProvider
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server2)").size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application1");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app1");
		props.put(HttpConstants.JETTY_SERVER_NAME, "Server1");
		config.update(props);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server1)").size());

		// HttpService
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server1)").size());

		// HandlerProvider
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server1)").size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props2 = new Hashtable<String, Object>();
		props2.put(HttpConstants.APPLICATION_NAME, "Application2");
		props2.put(HttpConstants.CONTEXT_PATH, "/test/app2");
		props2.put(HttpConstants.JETTY_SERVER_NAME, "Server2");
		config2.update(props2);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server2)").size());

		// HttpService
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server2)").size());

		// HandlerProvider
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server2)").size());

		config.delete();
		config2.delete();
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server2)").size());

		// HttpService
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server2)").size());

		// HandlerProvider
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server2)").size());
	}

	/**
	 * Tests the update of properties.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	@Test
	public void test_properties_update() throws IOException,
			InvalidSyntaxException {
		waitCM();

		Assert.assertEquals(0, activator.getHttpApplications().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application1");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app1");
		props.put(HttpConstants.JETTY_SERVER_NAME, "Server1");
		config.update(props);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server2)").size());
		// HttpServices
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server2)").size());

		// HandlerProvider
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server2)").size());

		props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application2");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app2");
		props.put(HttpConstants.JETTY_SERVER_NAME, "Server2");
		config.update(props);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHttpApplication.class,
						"(lunifera.jetty.name=Server2)").size());

		// HttpService
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(HttpService.class,
						"(lunifera.jetty.name=Server2)").size());

		// HandlerProvider
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.http.contextPath=/test/app2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IHandlerProvider.class,
						"(lunifera.jetty.name=Server2)").size());
		config.delete();

	}

	/**
	 * Tests that different web applications have different ids.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	@Test
	public void test_Id() throws IOException, InvalidSyntaxException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application2");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app2");
		config2.update(props);
		waitCM();

		IHttpApplication app1 = Activator.context.getService(Activator.context
				.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application1)").iterator().next());
		IHttpApplication app2 = Activator.context.getService(Activator.context
				.getServiceReferences(IHttpApplication.class,
						"(lunifera.http.name=Application2)").iterator().next());
		Assert.assertFalse(app1.getId().equals(app2.getId()));

		config.delete();
		config2.delete();
	}

	private void waitCM() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Prepares default properties that should be used.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "Application1");
		props.put(HttpConstants.CONTEXT_PATH, "/test/app1");
		return props;
	}

}
