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

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.jetty.IJettyService;
import org.lunifera.runtime.web.jetty.JettyService;
import org.lunifera.runtime.web.jetty.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.NamespaceException;

public class JettyServiceTest {

	private Activator activator;
	private BundleContext context;
	private InternalJettyService jetty;

	@Before
	public void setup() throws ConfigurationException {
		activator = Activator.getInstance();
		context = Activator.context;
		jetty = new InternalJettyService("App1");
		jetty.initialize(prepareDefaultProps());
	}

	@Test
	public void test_initialize() throws ConfigurationException {
		jetty = new InternalJettyService();
		Assert.assertNull(jetty.getId());
		Assert.assertNull(jetty.getName());
		Assert.assertEquals(8080, jetty.getPort());

		Dictionary<String, Object> props = prepareDefaultProps();
		jetty.initialize(props);

		Assert.assertEquals("App1", jetty.getId());
		Assert.assertEquals("Application1", jetty.getName());
		Assert.assertEquals(8081, jetty.getPort());
	}

	@Test
	public void test_IdNull() throws ConfigurationException, ServletException,
			NamespaceException, InvalidSyntaxException {
		jetty = new InternalJettyService();
		Assert.assertNull(jetty.getId());
		try {
			jetty.updated(null);
			Assert.fail();
		} catch (Exception e) {
			// expected
		}
	}

	@Test
	public void test_IdNotNull_start() throws ConfigurationException,
			ServletException, NamespaceException, InvalidSyntaxException {
		jetty = new InternalJettyService();
		Assert.assertNull(jetty.getId());
		try {
			jetty.updated(null);
			Assert.fail();
		} catch (Exception e) {
			// expected
		} finally {
			jetty.stop();
		}
	}

	@Test
	public void test_update() throws ConfigurationException {
		jetty = new InternalJettyService();
		Assert.assertNull(jetty.getId());
		Assert.assertNull(jetty.getName());
		Assert.assertEquals(8080, jetty.getPort());

		Dictionary<String, Object> props = prepareDefaultProps();
		jetty.initialize(props);

		Dictionary<String, Object> props2 = new Hashtable<String, Object>();
		props2.put(IJettyService.OSGI__ID, "App2");
		props2.put(IJettyService.OSGI__NAME, "Application2");
		props2.put(IJettyService.OSGI__PORT, String.valueOf(8088));
		jetty.updated(props2);

		Assert.assertEquals("App2", jetty.getId());
		Assert.assertEquals("Application2", jetty.getName());
		Assert.assertEquals(8088, jetty.getPort());
	}

	@Test
	public void test_start_stop() throws ConfigurationException {
		Assert.assertEquals(0, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

		// do no registration on service registry!
		//
		jetty.start();
		// jetty service not available
		Assert.assertEquals(0, activator.getJettyServices().size());
		// managed service is available
		Assert.assertEquals(1, activator.getManagedServices().size());

		jetty.stop();
		Assert.assertEquals(0, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

	}

	@Test
	public void test_start_stop__with_registry() throws ConfigurationException {
		Assert.assertEquals(0, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

		// was internal started by the manager
		//
		ServiceRegistration<IJettyService> registration = context
				.registerService(IJettyService.class, jetty,
						prepareDefaultProps());
		Assert.assertEquals(1, activator.getJettyServices().size());
		Assert.assertEquals(1, activator.getManagedServices().size());

		jetty.stop();
		Assert.assertEquals(1, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

		jetty.start();
		Assert.assertEquals(1, activator.getJettyServices().size());
		Assert.assertEquals(1, activator.getManagedServices().size());

		jetty.stop();
		Assert.assertEquals(1, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());

		registration.unregister();
		Assert.assertEquals(0, activator.getJettyServices().size());
		Assert.assertEquals(0, activator.getManagedServices().size());
	}

	@Test
	public void test_FilterById() throws ConfigurationException,
			InvalidSyntaxException {
		// was internal started by the manager
		//
		ServiceRegistration<IJettyService> registration = context
				.registerService(IJettyService.class, jetty,
						prepareDefaultProps());

		Collection<ServiceReference<IJettyService>> refs = context
				.getServiceReferences(IJettyService.class,
						"(lunifera.jetty.id=App1)");
		try {
			if (refs.size() != 1) {
				Assert.fail("Instance not found!");
			}
		} finally {
			registration.unregister();
		}
	}

	@Test
	public void test_FilterByName() throws ConfigurationException,
			InvalidSyntaxException {
		// was internal started by the manager
		//
		ServiceRegistration<IJettyService> registration = context
				.registerService(IJettyService.class, jetty,
						prepareDefaultProps());

		Collection<ServiceReference<IJettyService>> refs = context
				.getServiceReferences(IJettyService.class,
						"(lunifera.jetty.name=Application1)");
		try {
			if (refs.size() != 1) {
				Assert.fail("Instance not found!");
			}
		} finally {
			registration.unregister();
		}
	}

	@Test
	public void test_FilterByPort() throws ConfigurationException,
			InvalidSyntaxException {
		// was internal started by the manager
		//
		ServiceRegistration<IJettyService> registration = context
				.registerService(IJettyService.class, jetty,
						prepareDefaultProps());

		Collection<ServiceReference<IJettyService>> refs = context
				.getServiceReferences(IJettyService.class,
						"(lunifera.jetty.port=8081)");
		try {
			if (refs.size() != 1) {
				Assert.fail("Instance not found!");
			}
		} finally {
			registration.unregister();
		}
	}

	@Test
	public void test_Properties() throws ConfigurationException {
		ServiceRegistration<IJettyService> registration = context
				.registerService(IJettyService.class, jetty, null);

		try {
			ServiceReference<ManagedService> ref = context
					.getServiceReferences(ManagedService.class,
							"(service.pid=org.lunifera.runtime.web.jetty.service)")
					.iterator().next();
			String id = (String) ref.getProperty(IJettyService.OSGI__ID);
			String name = (String) ref.getProperty(IJettyService.OSGI__NAME);
			String port = (String) ref.getProperty(IJettyService.OSGI__PORT);
			Assert.assertEquals("App1", id);
			Assert.assertEquals("Application1", name);
			Assert.assertEquals("8081", port);
		} catch (InvalidSyntaxException e) {
			Assert.fail(e.toString());
		}

		ServiceReference<IJettyService> ref = context
				.getServiceReference(IJettyService.class);
		IJettyService jetty = context.getService(ref);
		Assert.assertEquals("App1", jetty.getId());
		Assert.assertEquals("Application1", jetty.getName());
		Assert.assertEquals(8081, jetty.getPort());

		registration.unregister();
	}

	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(IJettyService.OSGI__ID, "App1");
		props.put(IJettyService.OSGI__NAME, "Application1");
		props.put(IJettyService.OSGI__PORT, String.valueOf(8081));
		return props;
	}

	public static class InternalJettyService extends JettyService {

		private String id;

		public InternalJettyService() {
			setBundleContext(Activator.context);
		}

		public InternalJettyService(String id) {
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

	}
}
