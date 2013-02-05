package org.lunifera.runtime.web.http.tests.context;

import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpApplication;
import org.lunifera.runtime.web.http.IHttpApplication;
import org.lunifera.runtime.web.http.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.http.HttpService;

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

	}
}
