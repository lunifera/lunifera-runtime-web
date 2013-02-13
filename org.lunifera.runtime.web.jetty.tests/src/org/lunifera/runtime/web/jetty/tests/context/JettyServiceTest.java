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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.jetty.JettyConstants;
import org.lunifera.runtime.web.jetty.IHandlerProvider;
import org.lunifera.runtime.web.jetty.internal.JettyService;
import org.lunifera.runtime.web.jetty.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;

@SuppressWarnings("restriction")
public class JettyServiceTest {

	private Activator activator;
	@SuppressWarnings("unused")
	private BundleContext context;
	private InternalJettyService server;

	/**
	 * Setup of the test.
	 * 
	 * @throws ConfigurationException
	 */
	@Before
	public void setup() throws ConfigurationException {
		activator = Activator.getInstance();
		context = Activator.context;
		server = new InternalJettyService("S1");
		server.setName("Server1");
		server.setHttpPort(8099);
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Test
	public void test_start_stop() throws ConfigurationException,
			ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://localhost:8099");
		HttpResponse resp;
		try {
			resp = client.execute(get);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		server.start();
		client = new DefaultHttpClient();
		resp = client.execute(get);
		Assert.assertEquals(404, resp.getStatusLine().getStatusCode());

		server.stop();
		try {
			client = new DefaultHttpClient();
			resp = client.execute(get);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}
	}

	/**
	 * Tests start and stop of application.
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Test
	public void test_update() throws ConfigurationException,
			ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get8099 = new HttpGet("http://localhost:8099");
		HttpGet get8091 = new HttpGet("http://localhost:8091");
		HttpResponse resp;
		try {
			resp = client.execute(get8099);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8091);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		// start for port 8099
		//
		server.start();
		client = new DefaultHttpClient();
		resp = client.execute(get8099);
		Assert.assertEquals(404, resp.getStatusLine().getStatusCode());
		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8091);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		// stop for port 8099
		//
		server.stop();
		try {
			resp = client.execute(get8099);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8091);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		// change port to 8091
		//
		server.setHttpPort(8091);
		try {
			resp = client.execute(get8099);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8091);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		// start for port 8091
		//
		server.start();
		client = new DefaultHttpClient();
		resp = client.execute(get8091);
		Assert.assertEquals(404, resp.getStatusLine().getStatusCode());
		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8099);
			Assert.fail();
		} catch (HttpHostConnectException e) {
		}

		// start port 8091
		//
		server.stop();
		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8099);
		} catch (HttpHostConnectException e) {
		}
		try {
			client = new DefaultHttpClient();
			resp = client.execute(get8091);
		} catch (HttpHostConnectException e) {
		}
	}

	// /**
	// * Tests start and stop of application.
	// *
	// * @throws ConfigurationException
	// * @throws IOException
	// * @throws ClientProtocolException
	// */
	// @Test
	// public void test_https() throws ConfigurationException,
	// ClientProtocolException, IOException {
	//
	// application.setUseHttp(false);
	// application.setUseHttps(true);
	// application.setHttpsPort(8099);
	// application.setSslNeedsClientAuth(false);
	// application.setSslWantsClientAuth(false);
	//
	// HttpClient client = new DefaultHttpClient();
	// HttpGet getHttps8099 = new HttpGet("https://localhost:8099");
	// HttpGet getHttp8099 = new HttpGet("http://localhost:8099");
	// HttpResponse resp;
	// try {
	// resp = client.execute(getHttps8099);
	// Assert.fail();
	// } catch (HttpHostConnectException e) {
	// }
	//
	// try {
	// client = new DefaultHttpClient();
	// resp = client.execute(getHttp8099);
	// Assert.fail();
	// } catch (HttpHostConnectException e) {
	// }
	//
	// // start for https port 8099
	// //
	// application.start();
	// client = new DefaultHttpClient();
	// resp = client.execute(getHttps8099);
	// Assert.assertEquals(404, resp.getStatusLine().getStatusCode());
	// try {
	// client = new DefaultHttpClient();
	// resp = client.execute(getHttp8099);
	// Assert.fail();
	// } catch (HttpHostConnectException e) {
	// }
	//
	// // start port 8091
	// //
	// application.stop();
	// try {
	// client = new DefaultHttpClient();
	// resp = client.execute(getHttps8099);
	// } catch (HttpHostConnectException e) {
	// }
	// try {
	// client = new DefaultHttpClient();
	// resp = client.execute(getHttp8099);
	// } catch (HttpHostConnectException e) {
	// }
	// }

	/**
	 * Tests add / remove handler.
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Test
	public void test_add_remove_handler() throws ConfigurationException,
			ClientProtocolException, IOException {
		server.setName("Server1");
		server.setHttpPort(8099);

		// reg0 - Server1
		//
		Hashtable<String, String> dict0 = new Hashtable<String, String>();
		dict0.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg0 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict0);

		server.start();

		Assert.assertEquals(1,
				server.getHandlerCollection().getHandlers().length);

		// reg1 - Server1
		//
		Hashtable<String, String> dict1 = new Hashtable<String, String>();
		dict1.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg1 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict1);
		Assert.assertEquals(2,
				server.getHandlerCollection().getHandlers().length);

		// reg2 - Server2
		//
		Hashtable<String, String> dict2 = new Hashtable<String, String>();
		dict2.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		ServiceRegistration<IHandlerProvider> reg2 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict2);
		Assert.assertEquals(2,
				server.getHandlerCollection().getHandlers().length);

		// reg3 - Server1
		//
		Hashtable<String, String> dict3 = new Hashtable<String, String>();
		dict3.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg3 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict3);
		Assert.assertEquals(3,
				server.getHandlerCollection().getHandlers().length);

		reg1.unregister();
		Assert.assertEquals(2,
				server.getHandlerCollection().getHandlers().length);
		reg2.unregister();
		Assert.assertEquals(2,
				server.getHandlerCollection().getHandlers().length);
		reg3.unregister();
		Assert.assertEquals(1,
				server.getHandlerCollection().getHandlers().length);
		reg0.unregister();
		Assert.assertEquals(0,
				server.getHandlerCollection().getHandlers().length);
		server.stop();
	}

	/**
	 * Tests add / remove handler.
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Test
	public void test_add_remove_handler__start_with_handlers()
			throws ConfigurationException, ClientProtocolException, IOException {
		server.setName("Server1");
		server.setHttpPort(8099);

		// reg1 - Server1
		//
		Hashtable<String, String> dict1 = new Hashtable<String, String>();
		dict1.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg1 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict1);

		// reg2 - Server2
		//
		Hashtable<String, String> dict2 = new Hashtable<String, String>();
		dict2.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		ServiceRegistration<IHandlerProvider> reg2 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict2);

		// reg3 - Server1
		//
		Hashtable<String, String> dict3 = new Hashtable<String, String>();
		dict3.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg3 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict3);

		Assert.assertNull(server.getHandlerCollection());
		server.start();
		Assert.assertEquals(2,
				server.getHandlerCollection().getHandlers().length);

		reg1.unregister();
		Assert.assertEquals(1,
				server.getHandlerCollection().getHandlers().length);
		reg2.unregister();
		Assert.assertEquals(1,
				server.getHandlerCollection().getHandlers().length);
		reg3.unregister();
		Assert.assertEquals(0,
				server.getHandlerCollection().getHandlers().length);

		server.stop();
	}

	/**
	 * Tests add / remove handler.
	 * 
	 * @throws ConfigurationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Test
	public void test_move_handler_between_jetties()
			throws ConfigurationException, ClientProtocolException, IOException {
		InternalJettyService server1 = new InternalJettyService("S2");
		server1.setName("Server1");
		server1.setHttpPort(8091);
		server1.start();

		InternalJettyService server2 = new InternalJettyService("S2");
		server2.setName("Server2");
		server2.setHttpPort(8092);
		server2.start();

		// reg1 - Server1
		//
		Hashtable<String, String> dict1 = new Hashtable<String, String>();
		dict1.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		ServiceRegistration<IHandlerProvider> reg1 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict1);

		// reg2 - Server2
		//
		Hashtable<String, String> dict2 = new Hashtable<String, String>();
		dict2.put(JettyConstants.JETTY_SERVER_NAME, "Server2");
		ServiceRegistration<IHandlerProvider> reg2 = context.registerService(
				IHandlerProvider.class, new HandlerProvider(
						new DefaultHandler()), dict2);

		Assert.assertEquals(1,
				server1.getHandlerCollection().getHandlers().length);
		Assert.assertEquals(1,
				server2.getHandlerCollection().getHandlers().length);

		// update the properties of handler1 to "Server2"
		//
		reg1.setProperties(dict2);
		Assert.assertEquals(0,
				server1.getHandlerCollection().getHandlers().length);
		Assert.assertEquals(2,
				server2.getHandlerCollection().getHandlers().length);

		// unregister
		reg1.unregister();
		reg2.unregister();

		server1.stop();
		server2.stop();
	}

	/**
	 * Creates default properties for the tests.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(JettyConstants.JETTY_SERVER_NAME, "Server1");
		props.put(JettyConstants.HTTP_PORT, "8081");
		return props;
	}

	/**
	 * An internal helper server.
	 */
	public static class InternalJettyService extends JettyService {

		public InternalJettyService() {
			this("S1");
		}

		public InternalJettyService(String id) {
			super(id, Activator.context);
		}

		@Override
		public HandlerCollection getHandlerCollection() {
			return super.getHandlerCollection();
		}
	}

	private static class HandlerProvider implements IHandlerProvider {

		private final Handler handler;

		public HandlerProvider(Handler handler) {
			super();
			this.handler = handler;
		}

		@Override
		public Handler getHandler() {
			return handler;
		}
	}
}
