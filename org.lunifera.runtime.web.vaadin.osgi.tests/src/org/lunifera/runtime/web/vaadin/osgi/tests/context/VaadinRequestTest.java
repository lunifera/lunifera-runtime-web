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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.jetty.JettyConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinStatusCodes;
import org.lunifera.runtime.web.vaadin.osgi.tests.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class VaadinRequestTest {

	private ConfigurationAdmin cm;
	private Activator activator;
	private List<Configuration> toDelete = new ArrayList<Configuration>();

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

	@After
	public void tearDown() throws IOException {
		for (Configuration config : toDelete) {
			config.delete();
		}
	}

	/**
	 * Tests that requests are processed properly by the servlets.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Servlet_Request() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		startHttpApp1("Server1");
		startHttpApp7("Server1");
		waitCM();

		// start a new jetty server
		startJetty1();

		// create vaadin applications
		startVaadin1("HttpApp1");
		startVaadin2("HttpApp7");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");
		assertStatusOK("Application2");

		// test vaadin application 1
		HttpResponse response = httpGET("http://localhost:8091/app1/test/alias1/");
		Assert.assertEquals(500, response.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Server Error",
				response.getStatusLine().getReasonPhrase());

		// alias not registered
		HttpResponse response2 = httpGET("http://localhost:8091/app1/test/aliasXY/");
		Assert.assertEquals(404, response2.getStatusLine().getStatusCode());

		// test vaadin application 2
		HttpResponse response3 = httpGET("http://localhost:8091/app7/test/alias2");
		Assert.assertEquals(500, response3.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Server Error",
				response3.getStatusLine().getReasonPhrase());

		assertStatusOK("Application1");
		assertStatusOK("Application2");

	}

	/**
	 * Tests that requests are processed properly by the servlets.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Servlet_Request_on_different_server() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		startHttpApp1("Server1");
		startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		startJetty1();
		startJetty2();

		// create vaadin applications
		startVaadin1("HttpApp1");
		startVaadin2("HttpApp7");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");
		assertStatusOK("Application2");

		// test vaadin application 1
		HttpResponse response = httpGET("http://localhost:8091/app1/test/alias1/");
		Assert.assertEquals(500, response.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Server Error",
				response.getStatusLine().getReasonPhrase());
		Assert.assertEquals(404,
				httpGET("http://localhost:8091/app7/test/alias2")
						.getStatusLine().getStatusCode());

		// test vaadin application 2
		HttpResponse response3 = httpGET("http://localhost:8099/app7/test/alias2");
		Assert.assertEquals(500, response3.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Server Error",
				response3.getStatusLine().getReasonPhrase());
		Assert.assertEquals(404,
				httpGET("http://localhost:8099/app1/test/alias1/")
						.getStatusLine().getStatusCode());

		assertStatusOK("Application1");
		assertStatusOK("Application2");

	}

	/**
	 * Tests that requests are processed properly by the servlets.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Servlet_Request_switch_server() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		Configuration httpApp1Config = startHttpApp1("Server1");
		startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		startJetty1();
		startJetty2();

		// create vaadin applications
		startVaadin1("HttpApp1");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");

		// test vaadin application 1
		Assert.assertEquals(500,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		Assert.assertEquals(404,
				httpGET("http://localhost:8099/app1/test/alias1/")
						.getStatusLine().getStatusCode());

		// update the httpApplication 1 to run on server 2
		//
		Dictionary<String, Object> props1 = httpApp1Config.getProperties();
		props1.put(HttpConstants.JETTY_SERVER_NAME, "Server2");
		httpApp1Config.update(props1);
		waitCM(); // server restart
		waitCM(); // server restart
		waitCM(); // server restart

		assertStatusOK("Application1");

		// test vaadin application 1
		Assert.assertEquals(404,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		// --> Switched to new server
		Assert.assertEquals(500,
				httpGET("http://localhost:8099/app1/test/alias1/")
						.getStatusLine().getStatusCode());
	}

	/**
	 * Tests that requests are processed properly by the servlets.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Servlet_Request_switch_webApp() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		startHttpApp1("Server1");
		startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		startJetty1();
		startJetty2();

		// create vaadin applications
		Configuration vaadinApp1Config = startVaadin1("HttpApp1");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");

		// test vaadin application 1
		Assert.assertEquals(500,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		// test vaadin application 2
		Assert.assertEquals(404,
				httpGET("http://localhost:8099/app7/test/alias1")
						.getStatusLine().getStatusCode());

		// update the httpApplication 1 to run on server 2
		//
		Dictionary<String, Object> props1 = vaadinApp1Config.getProperties();
		props1.put(VaadinConstants.HTTP_APPLICATION_NAME, "HttpApp7");
		vaadinApp1Config.update(props1);
		waitCM(); // remounting servlets

		assertStatusOK("Application1");

		// test vaadin application 1
		Assert.assertEquals(404,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		// --> Switched to new server
		Assert.assertEquals(500,
				httpGET("http://localhost:8099/app7/test/alias1")
						.getStatusLine().getStatusCode());

	}

	/**
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Servlet_Request_switch_usedWebApp() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		startHttpApp1("Server1");
		startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		startJetty1();
		startJetty2();

		// create vaadin applications
		Configuration vaadinApp1Config = startVaadin1("HttpApp1");
		startVaadin2("HttpApp7");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");
		assertStatusOK("Application2");

		// test vaadin application 1
		Assert.assertEquals(500,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		// test vaadin application 2
		Assert.assertEquals(500,
				httpGET("http://localhost:8099/app7/test/alias2")
						.getStatusLine().getStatusCode());

		// update the httpApplication 1 to run on already used webApp7
		//
		Dictionary<String, Object> props1 = vaadinApp1Config.getProperties();
		props1.put(VaadinConstants.HTTP_APPLICATION_NAME, "HttpApp7");
		vaadinApp1Config.update(props1);
		waitCM(); // remounting servlets

		IVaadinApplication app = getVaadinApplication("Application1");
		IStatus status = app.getStatus();
		Assert.assertEquals(VaadinStatusCodes.SETTING_HTTP_SERVICE,
				status.getCode());
		Assert.assertEquals(IStatus.ERROR, status.getSeverity());
		assertStatusOK("Application2");

	}

	/**
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_StoppedOnError() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		startHttpApp1("Server1");
		startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		startJetty1();
		startJetty2();

		// create vaadin applications
		Configuration vaadinApp1Config = startVaadin1("HttpApp1");
		startVaadin2("HttpApp7");
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers

		assertStatusOK("Application1");
		assertStatusOK("Application2");

		// test vaadin application 1
		Assert.assertEquals(500,
				httpGET("http://localhost:8091/app1/test/alias1/")
						.getStatusLine().getStatusCode());
		// test vaadin application 2
		Assert.assertEquals(500,
				httpGET("http://localhost:8099/app7/test/alias2")
						.getStatusLine().getStatusCode());

		// update the httpApplication 1 to run on already used webApp7
		//
		Dictionary<String, Object> props1 = vaadinApp1Config.getProperties();
		props1.put(VaadinConstants.HTTP_APPLICATION_NAME, "HttpApp7");
		vaadinApp1Config.update(props1);
		waitCM(); // remounting servlets
		waitCM(); // remounting servlets
		waitCM(); // remounting servlets

		// test that the application is stopped!
		IVaadinApplication app1 = getVaadinApplication("Application1");
		Assert.assertFalse(app1.isStarted());
		IVaadinApplication app2 = getVaadinApplication("Application2");
		Assert.assertTrue(app2.isStarted());

	}

	/**
	 * Asserts that the status of the application is OK.
	 * 
	 * @param vaadinApp
	 */
	protected void assertStatusOK(String vaadinApp) {
		Assert.assertSame(VaadinStatusCodes.OK_STATUS,
				getVaadinApplication(vaadinApp).getStatus());
	}

	private IVaadinApplication getVaadinApplication(String name) {
		for (IVaadinApplication app : activator.getVaadinApplications()) {
			if (app.getName().equals(name)) {
				return app;
			}
		}
		return null;
	}

	/**
	 * Returns the first line of the http GET content.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("unused")
	private String httpGETFirstLine(String url) throws IOException,
			ClientProtocolException {
		HttpResponse resp = httpGET(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		String content = reader.readLine();
		reader.close();
		return content;
	}

	/**
	 * Calls get at http client.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private HttpResponse httpGET(String url) throws IOException,
			ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse resp = client.execute(get);
		return resp;
	}

	/**
	 * Starts the jetty server 1.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startJetty1() throws IOException {
		Configuration jettyConfig = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		props.put(JettyConstants.HTTP_PORT, "8091");
		jettyConfig.update(props);

		toDelete.add(jettyConfig);

		return jettyConfig;
	}

	/**
	 * Starts the jetty server 2.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startJetty2() throws IOException {
		Configuration jettyConfig = cm.createFactoryConfiguration(
				JettyConstants.OSGI__FACTORY_PID, null);
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		props.put(JettyConstants.HTTP_PORT, "8099");
		jettyConfig.update(props);

		toDelete.add(jettyConfig);

		return jettyConfig;
	}

	/**
	 * Starts the http application 1.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startHttpApp1(String server) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "HttpApp1");
		props.put(HttpConstants.CONTEXT_PATH, "/app1/test");
		props.put(HttpConstants.JETTY_SERVER_NAME, server);
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);

		toDelete.add(httpAppConfig);

		return httpAppConfig;
	}

	/**
	 * Starts the http application 7.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startHttpApp7(String server) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, "HttpApp7");
		props.put(HttpConstants.CONTEXT_PATH, "/app7/test");
		props.put(HttpConstants.JETTY_SERVER_NAME, server);
		Configuration httpApp7Config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		httpApp7Config.update(props);

		toDelete.add(httpApp7Config);

		return httpApp7Config;
	}

	/**
	 * Starts the vaadin application 1.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startVaadin1(String httpApp) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application1");
		props.put(VaadinConstants.UI_ALIAS, "alias1");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, httpApp);
		props.put(VaadinConstants.WIDGETSET, "widgetset1");
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);

		toDelete.add(httpAppConfig);

		return httpAppConfig;
	}

	/**
	 * Starts the vaadin application 2.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startVaadin2(String httpApp) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, "Application2");
		props.put(VaadinConstants.UI_ALIAS, "alias2");
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, httpApp);
		props.put(VaadinConstants.WIDGETSET, "widgetset2");
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);

		toDelete.add(httpAppConfig);

		return httpAppConfig;
	}

	/**
	 * Returns the HttpService by context path.
	 * 
	 * @param contextPath
	 * @return
	 * @throws InvalidSyntaxException
	 */
	public HttpService getHttpServiceByContextpath(String contextPath)
			throws InvalidSyntaxException {
		ServiceReference<HttpService> reference = Activator.context
				.getServiceReferences(
						HttpService.class,
						String.format("(lunifera.http.contextPath=%s)",
								contextPath)).iterator().next();
		HttpService httpService = Activator.context.getService(reference);
		return httpService;
	}

	private void waitCM() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}

	public static class DefaultHttpContext implements HttpContext {

		private Bundle bundle;

		public DefaultHttpContext(Bundle bundle) {
			this.bundle = bundle;
		}

		public boolean handleSecurity(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			return true;
		}

		public URL getResource(String name) {
			return bundle.getResource(name);
		}

		public String getMimeType(String name) {
			return null;
		}
	}
}
