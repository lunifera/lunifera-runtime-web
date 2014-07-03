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

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.tests.Activator;
import org.lunifera.runtime.web.vaadin.osgi.tests.context.helper.UI_WithProviderFactory;
import org.lunifera.runtime.web.vaadin.osgi.webapp.OSGiUIProvider;
import org.lunifera.runtime.web.vaadin.osgi.webapp.VaadinApplication;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@SuppressWarnings("restriction")
public class VaadinApplicationCMTests {

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

		try {
			Collection<ServiceReference<IVaadinApplication>> appRefs = Activator.context
					.getServiceReferences(IVaadinApplication.class, null);
			for (ServiceReference<IVaadinApplication> ref : appRefs) {
				String pid = (String) ref.getProperty("service.pid");
				Configuration config = cm.getConfiguration(pid);
				if (config != null) {
					config.delete();
				}
			}

			Collection<ServiceReference<HttpService>> httpRefs = Activator.context
					.getServiceReferences(HttpService.class, null);
			for (ServiceReference<HttpService> ref : httpRefs) {
				String pid = (String) ref.getProperty("service.pid");
				if (pid != null) {
					Configuration config = cm.getConfiguration(pid);
					if (config != null) {
						config.delete();
					}
				}
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void test_Id_Equals() throws IOException, InvalidSyntaxException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = prepareDefaultProps();
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
		Assert.assertEquals(app1.getId(), app2.getId());

		config.delete();
		config2.delete();
	}

	@Test
	public void test_Id_NotEquals() throws IOException, InvalidSyntaxException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareDefaultProps());
		// create new instance
		Configuration config2 = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.EXTERNAL_PID, "app2");
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
		Assert.assertNotEquals(app1.getId(), app2.getId());
		Assert.assertEquals("app1", app1.getId());
		Assert.assertEquals("app2", app2.getId());

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
						"(lunifera.externalPid=" + id + ")");
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

	@Test
	public void test_UiProviderFactory() throws IOException {
		waitCM();
		waitCM();

		Assert.assertEquals(0, activator.getVaadinApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareUiProviderFactoryTestProps());

		waitCM();
		waitCM();
		waitCM();
		waitCM();

		VaadinApplication app = (VaadinApplication) activator
				.getVaadinApplications().get(0);
		List<OSGiUIProvider> providers = app.getUiProviders();
		Assert.assertEquals(1, providers.size());

		// Ensure that the customer UI provider was used
		Assert.assertTrue(providers.get(0) instanceof UI_WithProviderFactory.UiProvider);
	}

	@Test
	public void test_NoUiProviderFactory() throws IOException {
		waitCM();
		waitCM();

		Assert.assertEquals(0, activator.getVaadinApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareNoUiProviderFactoryTestProps());

		waitCM();
		waitCM();
		waitCM();
		waitCM();

		VaadinApplication app = (VaadinApplication) activator
				.getVaadinApplications().get(0);
		List<OSGiUIProvider> providers = app.getUiProviders();
		Assert.assertEquals(1, providers.size());

		// Ensure that the default UI provider was used
		Assert.assertEquals(providers.get(0).getClass().getName(),
				OSGiUIProvider.class.getName());
	}

	@Test
	public void test_DefaultUiProvider() throws IOException {
		waitCM();
		waitCM();

		Assert.assertEquals(0, activator.getVaadinApplications().size());
		Assert.assertEquals(0, activator.getHttpServices().size());

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		config.update(prepareUiProviderFactoryTestProps());

		waitCM();
		waitCM();
		waitCM();
		waitCM();

		VaadinApplication app = (VaadinApplication) activator
				.getVaadinApplications().get(0);
		List<OSGiUIProvider> providers = app.getUiProviders();
		Assert.assertEquals(1, providers.size());

		// Ensure that the customer UI provider was used
		Assert.assertTrue(providers.get(0) instanceof UI_WithProviderFactory.UiProvider);
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void test_start_stop_destroy() throws ConfigurationException,
			InvalidSyntaxException, ServletException, NamespaceException,
			IOException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);

		// create properties
		Dictionary<String, Object> props = prepareDefaultProps();
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		config.update(props);
		waitCM();

		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}
		VaadinApplication application = (VaadinApplication) Activator.context
				.getService(refs.iterator().next());
		Assert.assertFalse(application.isDeployed());
		application.stop();
		Assert.assertFalse(application.isDeployed());

		HttpServiceImpl service = new HttpServiceImpl();
		Hashtable<String, String> httpProps = new Hashtable<String, String>();
		httpProps.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		ServiceRegistration<ExtendedHttpService> reg = Activator.context
				.registerService(ExtendedHttpService.class, service, httpProps);
		try {
			assertNull(service.servletAlias);
			assertNull(service.filterAlias);
			assertNull(service.resourceAlias);
			assertNull(service.servletAlias);
			assertNull(service.resourceName);
			assertNull(service.filter);
			assertNull(service.servlet);

			application.start();
			Assert.assertTrue(application.isDeployed());

			assertNotNull(service.servlet);
			assertNotNull(service.filterAlias);
			assertNotNull(service.resourceAlias);
			assertNotNull(service.servletAlias);
			assertEquals("/alias1", service.servletAlias);
			assertNotNull(service.resourceName);
			assertNotNull(service.filter);
			assertNotNull(service.servlet);

			application.stop();
			Assert.assertFalse(application.isDeployed());

			assertNull(service.servlet);
			assertNull(service.filterAlias);
			assertNull(service.resourceAlias);
			assertNull(service.servletAlias);
			assertNull(service.resourceName);
			assertNull(service.filter);
			assertNull(service.servlet);

			application.start();
			Assert.assertTrue(application.isDeployed());

			assertNotNull(service.servlet);
			assertNotNull(service.filterAlias);
			assertNotNull(service.resourceAlias);
			assertNotNull(service.servletAlias);
			assertEquals("/alias1", service.servletAlias);
			assertNotNull(service.resourceName);
			assertNotNull(service.filter);
			assertNotNull(service.servlet);

			application.destroy();
			Assert.assertFalse(application.isDeployed());

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

		config.delete();
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 * @throws IOException
	 */
	@Test
	public void test_start_stop_HttpService() throws ConfigurationException,
			InvalidSyntaxException, ServletException, NamespaceException,
			IOException {

		// create new instance
		Configuration config = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);

		// create properties
		Dictionary<String, Object> props = prepareDefaultProps();
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		config.update(props);
		waitCM();

		Collection<ServiceReference<IVaadinApplication>> refs = Activator.context
				.getServiceReferences(IVaadinApplication.class,
						"(lunifera.web.vaadin.name=Application1)");
		if (refs.size() != 1) {
			Assert.fail("Instance not found!");
		}
		VaadinApplication application = (VaadinApplication) Activator.context
				.getService(refs.iterator().next());
		Assert.assertFalse(application.isDeployed());

		HttpServiceImpl service = new HttpServiceImpl();
		Hashtable<String, String> httpProps = new Hashtable<String, String>();
		httpProps.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		ServiceRegistration<ExtendedHttpService> reg = Activator.context
				.registerService(ExtendedHttpService.class, service, httpProps);
		try {

			Assert.assertTrue(application.isDeployed());

			reg.unregister();

			Assert.assertFalse(application.isDeployed());

		} finally {
			application.destroy();
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
		props.put(VaadinConstants.EXTERNAL_PID, "app1");
		props.put(VaadinConstants.APPLICATION_NAME, "Application1");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.WIDGETSET, "widgetset1");
		props.put(VaadinConstants.UI_ALIAS, "alias1");
		return props;
	}

	/**
	 * Prepares properties for providerFactoryTest.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareUiProviderFactoryTestProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "providerFactoryTest");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.UI_ALIAS, "alias");
		return props;
	}

	/**
	 * Prepares properties for the noProviderFactoryTest.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareNoUiProviderFactoryTestProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "noProviderFactoryTest");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, "http1");
		props.put(VaadinConstants.UI_ALIAS, "alias");
		return props;
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
