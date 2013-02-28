package org.lunifera.runtime.web.ecview.presentation.vaadin.tests;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.knowhowlab.osgi.testing.utils.ServiceUtils;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context.BundleHelper;
import org.lunifera.runtime.web.http.HttpConstants;
import org.lunifera.runtime.web.jetty.JettyConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinConstants;
import org.lunifera.runtime.web.vaadin.osgi.common.VaadinStatusCodes;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.http.NamespaceException;

public class VaadinStarter {

	private final String JETTY_SERVER_NAME = "Server1";
	private final String HTTP_PORT = "8091";
	private final String HTTP_APPLICATION_NAME = "HttpApp1";
	private final String HTTP_CONTEXT_PATH = "/app1/test";
	private final String VAADIN_APPLICATION_NAME = "Application1";
	private final String VAADIN_UI_ALIAS = "alias1";
	private final String VAADIN_WIDGETSET = "widgetset1";
	
	private ConfigurationAdmin cm;
	@SuppressWarnings("unused")
	private Activator activator;

	private Configuration httpApp1Config;
	private Configuration jettyConfig;
	private Configuration vaadinApp1Config;
	
	
	public Configuration getHttpApp1Config() {
		return httpApp1Config;
	}

	public Configuration getJettyConfig() {
		return jettyConfig;
	}

	public Configuration getVaadinApp1Config() {
		return vaadinApp1Config;
	}

	public void start(TestUIProvider uiprovider) throws IOException,
	InvalidSyntaxException, ServletException, NamespaceException, BundleException {

		BundleHelper.ensureSetup();
		
		cm = Activator.getInstance().getConfigurationAdmin();
		
		// start a new jetty server
		jettyConfig = startJetty1();
		
		// create new applications
		httpApp1Config = startHttpApp1(JETTY_SERVER_NAME);
		waitCM();
		
		// create vaadin applications
		vaadinApp1Config = startVaadin1(HTTP_APPLICATION_NAME);
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		waitCM(); // restarting servers
		
		IVaadinApplication application = ServiceUtils.getService(Activator.context, IVaadinApplication.class);
		application.addUIProvider(uiprovider);
	}

	public void stop() throws IOException{
		// stop the services again
		jettyConfig.delete();
		httpApp1Config.delete();
		vaadinApp1Config.delete();
	}
	
	/**
	 * Starts the http application 1.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startHttpApp1(String server) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(HttpConstants.APPLICATION_NAME, HTTP_APPLICATION_NAME);
		props.put(HttpConstants.CONTEXT_PATH, HTTP_CONTEXT_PATH);
		props.put(HttpConstants.JETTY_SERVER_NAME, server);
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				HttpConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);
		return httpAppConfig;
	}

	private void waitCM() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
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
		props.put(JettyConstants.JETTY_SERVER_NAME, JETTY_SERVER_NAME);
		props.put(JettyConstants.HTTP_PORT, HTTP_PORT);
		jettyConfig.update(props);
		return jettyConfig;
	}

	/**
	 * Starts the vaadin application 1.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration startVaadin1(String httpApp) throws IOException {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(VaadinConstants.APPLICATION_NAME, VAADIN_APPLICATION_NAME);
		props.put(VaadinConstants.UI_ALIAS, VAADIN_UI_ALIAS);
		props.put(VaadinConstants.HTTP_APPLICATION_NAME, httpApp);
		props.put(VaadinConstants.WIDGETSET, VAADIN_WIDGETSET);
		Configuration httpAppConfig = cm.createFactoryConfiguration(
				VaadinConstants.OSGI__FACTORY_PID, null);
		httpAppConfig.update(props);
		return httpAppConfig;
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

}
