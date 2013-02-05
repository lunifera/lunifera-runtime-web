package org.lunifera.runtime.web.http.tests.context;

import java.util.Dictionary;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.http.IHttpApplicationManager;
import org.lunifera.runtime.web.http.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;

public class HttpManagerTest {

	private IHttpApplicationManager manager;
	private BundleContext context;

	@Before
	public void setup() {
		manager = Activator.getInstance().getHttpManager();
		context = Activator.context;
	}

	@Test
	public void test_Exists() {
		Assert.assertNotNull(Activator.getInstance().getHttpManager());
	}

	@Test
	public void test_Add_Remove_HttpApplication() {
		InternalHttpApplication application = new InternalHttpApplication();
		application.id = "ID1";
		application.name = "name1";
		application.contextPath = "/test/1";

		Assert.assertEquals(0, application.count_start);
		Assert.assertEquals(0, application.count_stop);

		InternalHttpApplication application2 = new InternalHttpApplication();
		application2.id = "ID2";
		application2.name = "name2";
		application2.contextPath = "/test/2";

		Assert.assertEquals(0, application2.count_start);
		Assert.assertEquals(0, application2.count_stop);

		ServiceRegistration<IHttpApplication> reference = context
				.registerService(IHttpApplication.class, application, null);
		Assert.assertEquals(1, application.count_start);
		Assert.assertEquals(0, application.count_stop);
		Assert.assertEquals(0, application2.count_start);
		Assert.assertEquals(0, application2.count_stop);

		ServiceRegistration<IHttpApplication> reference2 = context
				.registerService(IHttpApplication.class, application2, null);
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
	public void test_getApplication() {
		InternalHttpApplication application = new InternalHttpApplication();
		application.id = "ID1";
		application.name = "name1";
		application.contextPath = "/test/1";

		InternalHttpApplication application2 = new InternalHttpApplication();
		application2.id = "ID2";
		application2.name = "name2";
		application2.contextPath = "/test/2";

		ServiceRegistration<IHttpApplication> reference = context
				.registerService(IHttpApplication.class, application, null);

		Assert.assertSame(application, manager.getApplication("ID1"));
		Assert.assertNull(manager.getApplication("ID2"));

		ServiceRegistration<IHttpApplication> reference2 = context
				.registerService(IHttpApplication.class, application2, null);

		Assert.assertSame(application, manager.getApplication("ID1"));
		Assert.assertSame(application2, manager.getApplication("ID2"));

		reference2.unregister();
		Assert.assertSame(application, manager.getApplication("ID1"));
		Assert.assertNull(manager.getApplication("ID2"));

		reference.unregister();
		Assert.assertNull(manager.getApplication("ID1"));
		Assert.assertNull(manager.getApplication("ID2"));
	}

	@Test
	public void test_update() throws ConfigurationException {
		manager.updated(null);
	}

	public static class InternalHttpApplication implements IHttpApplication {

		private int count_start;
		private int count_stop;

		private String id;
		private String name;
		private String contextPath;

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getContextPath() {
			return contextPath;
		}

		@Override
		public void start() {
			count_start++;
		}

		@Override
		public void updated(Dictionary<String, ?> properties)
				throws ConfigurationException {

		}

		@Override
		public void stop() {
			count_stop++;
		}

	}
}
