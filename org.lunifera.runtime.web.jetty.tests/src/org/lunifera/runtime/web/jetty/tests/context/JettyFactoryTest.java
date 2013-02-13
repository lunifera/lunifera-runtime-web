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
package org.lunifera.runtime.web.jetty.tests.context;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.jetty.JettyConstants;
import org.lunifera.runtime.web.jetty.IJetty;
import org.lunifera.runtime.web.jetty.tests.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;

public class JettyFactoryTest {

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
		Assert.assertEquals(0, activator.getJettys().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getJettys().size());

		// update instance
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getJettys().size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		config2.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(2, activator.getJettys().size());

		// remove instance 1
		config.delete();
		waitCM();
		Assert.assertEquals(1, activator.getJettys().size());

		// remove instance 2
		config2.delete();
		waitCM();
		Assert.assertEquals(0, activator.getJettys().size());
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

		Assert.assertEquals(0, activator.getJettys().size());

		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8081)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8082)").size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		props.put(JettyConstants.HTTP_PORT, "8081");
		config.update(props);
		waitCM();

		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8081)").size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props2 = new Hashtable<String, Object>();
		props2.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		props2.put(JettyConstants.HTTP_PORT, "8082");
		config2.update(props2);
		waitCM();

		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8082)").size());

		config.delete();
		config2.delete();
		waitCM();

		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8081)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8082)").size());
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

		Assert.assertEquals(0, activator.getJettys().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		props.put(JettyConstants.HTTP_PORT, "8081");
		config.update(props);
		waitCM();

		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8081)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8082)").size());

		props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		props.put(JettyConstants.HTTP_PORT, "8082");
		config.update(props);
		waitCM();

		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8081)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(IJetty.class,
						"(lunifera.jetty.http.port=8082)").size());

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
				JettyConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		props.put(JettyConstants.HTTP_PORT, "8082");
		config2.update(props);
		waitCM();

		IJetty app1 = Activator.context.getService(Activator.context
				.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server1)").iterator().next());
		IJetty app2 = Activator.context.getService(Activator.context
				.getServiceReferences(IJetty.class,
						"(lunifera.jetty.name=Server2)").iterator().next());
		Assert.assertFalse(app1.getId().equals(app2.getId()));

		config.delete();
		config2.delete();
	}

	private void waitCM() {
		try {
			Thread.sleep(200);
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
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		props.put(JettyConstants.HTTP_PORT, "8081");
		return props;
	}

}
