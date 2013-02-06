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

import java.util.Dictionary;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.jetty.IJettyService;
import org.lunifera.runtime.web.jetty.IJettyServiceManager;
import org.lunifera.runtime.web.jetty.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;

public class JettyManagerTest {

	private IJettyServiceManager manager;
	private BundleContext context;

	@Before
	public void setup() {
		manager = Activator.getInstance().getJettyManager();
		context = Activator.context;
	}

	@Test
	public void test_Exists() {
		Assert.assertNotNull(Activator.getInstance().getJettyManager());
	}

	@Test
	public void test_Add_Remove_Jetty() {
		InternalJetty application = new InternalJetty();
		application.id = "ID1";
		application.name = "name1";
		application.port = 8081;

		Assert.assertEquals(0, application.count_start);
		Assert.assertEquals(0, application.count_stop);

		InternalJetty application2 = new InternalJetty();
		application2.id = "ID2";
		application2.name = "name2";
		application.port = 8082;

		Assert.assertEquals(0, application2.count_start);
		Assert.assertEquals(0, application2.count_stop);

		ServiceRegistration<IJettyService> reference = context.registerService(
				IJettyService.class, application, null);
		Assert.assertEquals(1, application.count_start);
		Assert.assertEquals(0, application.count_stop);
		Assert.assertEquals(0, application2.count_start);
		Assert.assertEquals(0, application2.count_stop);

		ServiceRegistration<IJettyService> reference2 = context
				.registerService(IJettyService.class, application2, null);
		Assert.assertEquals(1, application.count_start);
		Assert.assertEquals(0, application.count_stop);
		Assert.assertEquals(1, application2.count_start);
		Assert.assertEquals(0, application2.count_stop);

		reference2.unregister();
		Assert.assertEquals(1, application.count_start);
		Assert.assertEquals(0, application.count_stop);
		Assert.assertEquals(1, application2.count_start);
		Assert.assertEquals(1, application2.count_stop);

		reference.unregister();
		Assert.assertEquals(1, application.count_start);
		Assert.assertEquals(1, application.count_stop);
		Assert.assertEquals(1, application2.count_start);
		Assert.assertEquals(1, application2.count_stop);
	}

	@Test
	public void test_getJetty() {
		InternalJetty application = new InternalJetty();
		application.id = "ID1";
		application.name = "name1";
		application.port = 8081;

		InternalJetty application2 = new InternalJetty();
		application2.id = "ID2";
		application2.name = "name2";
		application.port = 8082;

		ServiceRegistration<IJettyService> reference = context.registerService(
				IJettyService.class, application, null);

		Assert.assertSame(application, manager.getJetty("ID1"));
		Assert.assertNull(manager.getJetty("ID2"));

		ServiceRegistration<IJettyService> reference2 = context
				.registerService(IJettyService.class, application2, null);

		Assert.assertSame(application, manager.getJetty("ID1"));
		Assert.assertSame(application2, manager.getJetty("ID2"));

		reference2.unregister();
		Assert.assertSame(application, manager.getJetty("ID1"));
		Assert.assertNull(manager.getJetty("ID2"));

		reference.unregister();
		Assert.assertNull(manager.getJetty("ID1"));
		Assert.assertNull(manager.getJetty("ID2"));
	}

	@Test
	public void test_update() throws ConfigurationException {
		manager.updated(null);
	}

	public static class InternalJetty implements IJettyService {

		private int count_start;
		private int count_stop;

		private String id;
		private String name;
		private int port;
		private boolean started;

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int getPort() {
			return port;
		}

		@Override
		public boolean isStarted() {
			return started;
		}

		@Override
		public void start() {
			started = true;
			count_start++;
		}

		@Override
		public void updated(Dictionary<String, ?> properties)
				throws ConfigurationException {

		}

		@Override
		public void stop() {
			started = false;
			count_stop++;
		}

	}
}
