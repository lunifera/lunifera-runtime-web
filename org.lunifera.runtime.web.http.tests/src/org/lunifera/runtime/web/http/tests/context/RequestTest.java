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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.http.tests.Activator;
import org.lunifera.runtime.web.jetty.JettyConstants;
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

public class RequestTest {

	private ConfigurationAdmin cm;
	@SuppressWarnings("unused")
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
		Configuration httpApp1Config = startHttpApp1("Server1");
		Configuration httpApp7Config = startHttpApp7("Server1");
		waitCM();

		// start a new jetty server
		Configuration jettyConfig = startJetty1();

		// register servlets
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerServlet("/value1", new Value1Servlet(), null, null);

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerServlet("/value77", new Value7Servlet(), null,
				null);

		// test servlet 1
		Assert.assertEquals("Servlet1 accessed!",
				httpGETFirstLine("http://localhost:8091/app1/test/value1"));

		// test servlet 7
		Assert.assertEquals("Servlet7 accessed!",
				httpGETFirstLine("http://localhost:8091/app7/test/value77"));

		// stop the services again
		jettyConfig.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
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
		Configuration httpApp1Config = startHttpApp1("Server1");
		Configuration httpApp7Config = startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		Configuration jetty1Config = startJetty1();
		Configuration jetty2Config = startJetty2();
		waitCM();
		waitCM();

		// register servlets
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerServlet("/value1", new Value1Servlet(), null, null);

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerServlet("/value77", new Value7Servlet(), null,
				null);

		// test servlet 1
		Assert.assertEquals("Servlet1 accessed!",
				httpGETFirstLine("http://localhost:8091/app1/test/value1"));

		// test servlet 7 - Server 2
		Assert.assertEquals(404,
				httpGET("http://localhost:8091/app7/test/value77")
						.getStatusLine().getStatusCode());
		Assert.assertEquals("Servlet7 accessed!",
				httpGETFirstLine("http://localhost:8099/app7/test/value77"));

		// stop the services again
		jetty1Config.delete();
		jetty2Config.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
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
		Configuration httpApp7Config = startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		Configuration jetty1Config = startJetty1();
		Configuration jetty2Config = startJetty2();
		waitCM();
		waitCM();

		// register servlets
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerServlet("/value1", new Value1Servlet(), null, null);

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerServlet("/value77", new Value7Servlet(), null,
				null);

		// test servlet 1
		Assert.assertEquals("Servlet1 accessed!",
				httpGETFirstLine("http://localhost:8091/app1/test/value1"));
		// test servlet 7 - Server 2
		Assert.assertEquals("Servlet7 accessed!",
				httpGETFirstLine("http://localhost:8099/app7/test/value77"));

		// update the httpApplication 1 to run on server 2
		//
		Dictionary<String, Object> props1 = httpApp1Config.getProperties();
		props1.put(HttpConstants.JETTY_SERVER_NAME, "Server2");
		httpApp1Config.update(props1);
		waitCM(); // server restart
		waitCM(); // server restart
		waitCM(); // server restart

		Assert.assertEquals(404,
				httpGET("http://localhost:8091/app1/test/value1")
						.getStatusLine().getStatusCode());
		Assert.assertEquals("Servlet1 accessed!",
				httpGETFirstLine("http://localhost:8099/app1/test/value1"));
		// test servlet 7 - Server 2
		Assert.assertEquals("Servlet7 accessed!",
				httpGETFirstLine("http://localhost:8099/app7/test/value77"));

		// stop the services again
		jetty1Config.delete();
		jetty2Config.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
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
	public void test_Resource_Request() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		Configuration httpApp1Config = startHttpApp1("Server1");
		Configuration httpApp7Config = startHttpApp7("Server1");
		waitCM();

		// start a new jetty server
		Configuration jettyConfig = startJetty1();
		waitCM();
		waitCM();

		// register resources
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerResources("/resource", null,
				new DefaultHttpContext(Activator.context.getBundle()));

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerResources("/resource", "/files2",
				new DefaultHttpContext(Activator.context.getBundle()));

		// test resource 1
		Assert.assertEquals(
				"Was files1/info.txt",
				httpGETFirstLine("http://localhost:8091/app1/test/resource/files1/info.txt"));

		// test resource 1
		Assert.assertEquals(
				"Was files2/info.txt",
				httpGETFirstLine("http://localhost:8091/app7/test/resource/files2/info.txt"));

		// stop the services again
		jettyConfig.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
	}

	/**
	 * Tests that requests are processed properly for the resources.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Resource_Request_on_different_server() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		Configuration httpApp1Config = startHttpApp1("Server1");
		Configuration httpApp7Config = startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		Configuration jetty1Config = startJetty1();
		Configuration jetty2Config = startJetty2();
		waitCM();
		waitCM();

		// register resources
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerResources("/resource", null,
				new DefaultHttpContext(Activator.context.getBundle()));

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerResources("/resource", "/files2",
				new DefaultHttpContext(Activator.context.getBundle()));

		// test servlet 1
		Assert.assertEquals(
				"Was files1/info.txt",
				httpGETFirstLine("http://localhost:8091/app1/test/resource/files1/info.txt"));

		// test servlet 7 - Server 2
		Assert.assertEquals(
				404,
				httpGET(
						"http://localhost:8091/app7/test/resource/files1/info.txt")
						.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Was files2/info.txt",
				httpGETFirstLine("http://localhost:8099/app7/test/resource/files2/info.txt"));

		// stop the services again
		jetty1Config.delete();
		jetty2Config.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
	}

	/**
	 * Tests that requests are processed properly for the resources.
	 * 
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 * @throws NamespaceException
	 * @throws ServletException
	 */
	@Test
	public void test_Resource_Request_switch_server() throws IOException,
			InvalidSyntaxException, ServletException, NamespaceException {

		// create new applications
		Configuration httpApp1Config = startHttpApp1("Server1");
		Configuration httpApp7Config = startHttpApp7("Server2");
		waitCM();

		// start a new jetty server
		Configuration jetty1Config = startJetty1();
		Configuration jetty2Config = startJetty2();
		waitCM();
		waitCM();

		// register servlets
		HttpService httpService = getHttpServiceByContextpath("/app1/test");
		httpService.registerResources("/resource", null,
				new DefaultHttpContext(Activator.context.getBundle()));

		HttpService httpService7 = getHttpServiceByContextpath("/app7/test");
		httpService7.registerResources("/resource", "/files2",
				new DefaultHttpContext(Activator.context.getBundle()));

		// test servlet 1
		Assert.assertEquals(
				"Was files1/info.txt",
				httpGETFirstLine("http://localhost:8091/app1/test/resource/files1/info.txt"));
		// test servlet 7 - Server 2
		Assert.assertEquals(
				"Was files2/info.txt",
				httpGETFirstLine("http://localhost:8099/app7/test/resource/files2/info.txt"));

		// update the httpApplication 1 to run on server 2
		//
		Dictionary<String, Object> props1 = httpApp1Config.getProperties();
		props1.put(HttpConstants.JETTY_SERVER_NAME, "Server2");
		httpApp1Config.update(props1);
		waitCM(); // server restart
		waitCM(); // server restart
		waitCM(); // server restart

		Assert.assertEquals(
				404,
				httpGET(
						"http://localhost:8091/app1/test/resource/files1/info.txt")
						.getStatusLine().getStatusCode());
		Assert.assertEquals(
				"Was files1/info.txt",
				httpGETFirstLine("http://localhost:8099/app1/test/resource/files1/info.txt"));
		// test servlet 7 - Server 2
		Assert.assertEquals(
				"Was files2/info.txt",
				httpGETFirstLine("http://localhost:8099/app7/test/resource/files2/info.txt"));

		// stop the services again
		jetty1Config.delete();
		jetty2Config.delete();
		httpApp1Config.delete();
		httpApp7Config.delete();
	}

	/**
	 * Returns the first line of the http GET content.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
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
		props.put(HttpConstants.APPLICATION_NAME, "Application1");
		props.put(HttpConstants.CONTEXT_PATH, "/app1/test");
		props.put(HttpConstants.JETTY_SERVER_NAME, server);
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);
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
		props.put(HttpConstants.APPLICATION_NAME, "Application7");
		props.put(HttpConstants.CONTEXT_PATH, "/app7/test");
		props.put(HttpConstants.JETTY_SERVER_NAME, server);
		Configuration httpApp7Config = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		httpApp7Config.update(props);
		return httpApp7Config;
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

	@SuppressWarnings("serial")
	private static class Value1Servlet extends HttpServlet {
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.getOutputStream().print("Servlet1 accessed!");
		}
	}

	@SuppressWarnings("serial")
	private static class Value7Servlet extends HttpServlet {
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.getOutputStream().print("Servlet7 accessed!");
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
