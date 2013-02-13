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
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.jetty.Constants;
import org.lunifera.runtime.web.jetty.internal.JettyService;
import org.lunifera.runtime.web.jetty.tests.Activator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

@SuppressWarnings("restriction")
public class JettyServiceTest {

	private Activator activator;
	@SuppressWarnings("unused")
	private BundleContext context;
	private InternalJettyService application;

	/**
	 * Setup of the test.
	 * 
	 * @throws ConfigurationException
	 */
	@Before
	public void setup() throws ConfigurationException {
		activator = Activator.getInstance();
		context = Activator.context;
		application = new InternalJettyService("S1");
		application.setName("Server1");
		application.setHttpPort(8099);
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

		application.start();
		client = new DefaultHttpClient();
		resp = client.execute(get);
		Assert.assertEquals(404, resp.getStatusLine().getStatusCode());

		application.stop();
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
		application.start();
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
		application.stop();
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
		application.setHttpPort(8091);
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
		application.start();
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
		application.stop();
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
	 * Creates default properties for the tests.
	 * 
	 * @return
	 */
	public Dictionary<String, Object> prepareDefaultProps() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.OSGI__SERVER_NAME, "Server1");
		props.put(Constants.HTTP_PORT, "8081");
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

	}
}
