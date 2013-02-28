package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context;

import java.io.IOException;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.TestUIProvider;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.VaadinStarter;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.http.NamespaceException;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class VaadinStarterTest {

	private VaadinStarter vaadinStarter = new VaadinStarter();
	
	@Before
	public void setup(){
		vaadinStarter = new VaadinStarter();
	}
	
	@Test
	public void test_VaadinStarter() throws IOException, InvalidSyntaxException, ServletException, NamespaceException, BundleException{
		MyUI myui = new MyUI();
		vaadinStarter.start(new TestUIProvider(myui));
		Assert.assertEquals(false, myui.called);
		waitCM();
		// test vaadin application 1
		HttpResponse response = httpGET("http://localhost:8091/app1/test/alias1/");
		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
		waitCM();
		Assert.assertEquals(true, myui.called);
		vaadinStarter.stop();
	}
	
	private static final class MyUI extends UI{
		boolean called = false;
		
		@Override
		protected void init(VaadinRequest request) {
			called = true;
		}
		
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
	
	private void waitCM() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}


}
