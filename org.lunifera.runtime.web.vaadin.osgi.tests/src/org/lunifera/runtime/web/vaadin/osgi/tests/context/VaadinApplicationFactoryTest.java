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

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.tests.Activator;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;

public class VaadinApplicationFactoryTest {

	private ConfigurationAdmin cm;
	private Activator activator;

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 * @throws BundleException
	 */
	@Before
	public void setup() throws ConfigurationException, BundleException {
		BundleHelper.ensureSetup();
		cm = Activator.getInstance().getConfigurationAdmin();
		activator = Activator.getInstance();
	}

	/**
	 * Test that a http application starts.
	 * 
	 * @throws IOException
	 */
	@Test
	public void test_startVaadinApplication() throws IOException {
		waitCM();
		waitCM();

		Assert.assertEquals(0, activator.getVaadinApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getVaadinApplications().size());

		// update instance
		config.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(1, activator.getVaadinApplications().size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config2.update(prepareDefaultProps());
		waitCM();
		Assert.assertEquals(2, activator.getVaadinApplications().size());

		// remove instance 1
		config.delete();
		waitCM();
		Assert.assertEquals(1, activator.getVaadinApplications().size());

		// remove instance 2
		config2.delete();
		waitCM();
		Assert.assertEquals(0, activator.getVaadinApplications().size());
	}

	/**
	 * Tests the use of properties in the services.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	@Test
	public void test_properties() throws IOException, InvalidSyntaxException {

		// wait 600ms for CM
		waitCM();
		waitCM();
		waitCM();

		Assert.assertEquals(0, activator.getVaadinApplications().size());

		// HttpApplications
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http1)")
						.size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http2)")
						.size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application1");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.WIDGETSET, "widgetset1");
		props.put(VaadinConstants.UI_ALIAS, "alias1");
		config.update(props);
		waitCM();
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http1)")
						.size());

		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props2 = new Hashtable<String, Object>();
		props2.put(VaadinConstants.APPLICATION_NAME, "Application2");
		props2.put(VaadinConstants.HTTP_APPLICATION_NAME, "http2");
		props2.put(VaadinConstants.WIDGETSET, "widgetset2");
		props2.put(VaadinConstants.UI_ALIAS, "alias2");
		config2.update(props2);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http2)")
						.size());

		config.delete();
		config2.delete();
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http1)")
						.size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http2)")
						.size());
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

		Assert.assertEquals(0, activator.getVaadinApplications().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application1");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.WIDGETSET, "widgetset1");
		props.put(VaadinConstants.UI_ALIAS, "alias1");
		config.update(props);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset1)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http1)")
						.size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset2)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http2)")
						.size());

		props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application2");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http2");
		props.put(VaadinConstants.WIDGETSET, "widgetset2");
		props.put(VaadinConstants.UI_ALIAS, "alias2");
		config.update(props);
		waitCM();

		// HttpApplication
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset1)").size());
		Assert.assertEquals(
				0,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http1)")
						.size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class,
						"(lunifera.web.vaadin.widgetset=widgetset2)").size());
		Assert.assertEquals(
				1,
				Activator.context.getServiceReferences(
						IVaadinApplication.class, "(lunifera.http.name=http2)")
						.size());

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
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application2");
		config2.update(props);
		waitCM();
		waitCM();

		IVaadinApplication app1 = Activator.context
				.getService(Activator.context
						.getServiceReferences(IVaadinApplication.class,
								"(lunifera.web.vaadin.name=Application1)")
						.iterator().next());
		IVaadinApplication app2 = Activator.context
				.getService(Activator.context
						.getServiceReferences(IVaadinApplication.class,
								"(lunifera.web.vaadin.name=Application2)")
						.iterator().next());
		Assert.assertFalse(app1.getId().equals(app2.getId()));

		config.delete();
		config2.delete();
	}

	/**
	 * Filters the vaadin application by its id.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 */
	@Test
	public void test_FilterVaadinApplicationById()
			throws ConfigurationException, InvalidSyntaxException, IOException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		
		String id = Activator.getInstance().getVaadinApplications().get(0)
				.getId();
		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.web.vaadin.id=" + id + ")");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}

		config.delete();
	}

	/**
	 * Filters the vaadin application by its name.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 */
	@Test
	public void test_FilterVaadinApplicationByName()
			throws ConfigurationException, InvalidSyntaxException, IOException {
		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		
		
		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}

		config.delete();
	}

	/**
	 * Filters the vaadin application service by its uialias.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 */
	@Test
	public void test_FilterVaadinApplicationByUiAlias()
			throws ConfigurationException, InvalidSyntaxException, IOException {
		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		
		
		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.web.vaadin.uialias=alias1)");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}

		config.delete();
	}

	/**
	 * Filters the vaadin application service by its uialias.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws IOException
	 */
	@Test
	public void test_FilterVaadinApplicationByHttpApplication()
			throws ConfigurationException, InvalidSyntaxException, IOException {
		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		waitCM();
		
		
		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.http.name=http1)");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}

		config.delete();
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
		props.put(VaadinConstants.APPLICATION_NAME, "Application1");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.WIDGETSET, "widgetset1");
		props.put(VaadinConstants.UI_ALIAS, "alias1");
		return props;
	}

}
