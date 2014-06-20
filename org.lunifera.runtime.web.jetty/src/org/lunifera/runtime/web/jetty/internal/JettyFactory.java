/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.jetty.internal.app.jettyContext (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.jetty.internal;

import java.io.File;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.lunifera.runtime.web.jetty.IJetty;
import org.lunifera.runtime.web.jetty.JettyConstants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link ManagedServiceFactory} to start and stop jetty server.
 * <p>
 * <h3>properties</h3>
 * <ul>
 * <li>{@link JettyConstants#JETTY_SERVER_NAME} - Name of the jetty server.</li>
 * <li>{@link JettyConstants#HTTP_ENABLED}</li>
 * <li>{@link JettyConstants#HTTP_PORT}</li>
 * <li>{@link JettyConstants#HTTP_HOST}</li>
 * <li>{@link JettyConstants#HTTP_NIO}</li>
 * <li>{@link JettyConstants#HTTPS_ENABLED}</li>
 * <li>{@link JettyConstants#HTTPS_HOST}</li>
 * <li>{@link JettyConstants#HTTPS_PORT}</li>
 * <li>{@link JettyConstants#SSL_KEYSTORE}</li>
 * <li>{@link JettyConstants#SSL_PASSWORD}</li>
 * <li>{@link JettyConstants#SSL_KEYPASSWORD}</li>
 * <li>{@link JettyConstants#SSL_NEEDCLIENTAUTH}</li>
 * <li>{@link JettyConstants#SSL_WANTCLIENTAUTH}</li>
 * <li>{@link JettyConstants#SSL_PROTOCOL}</li>
 * <li>{@link JettyConstants#SSL_KEYSTORETYPE}</li>
 * </ul>
 */
public class JettyFactory implements ManagedServiceFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(JettyFactory.class);

	private static final String JETTY_WORK_DIR = "jettywork"; //$NON-NLS-1$
	private static final String DIR_PREFIX = "pid_"; //$NON-NLS-1$

	private Map<String, IJetty> jetties = Collections
			.synchronizedMap(new HashMap<String, IJetty>());
	private Map<String, ServiceRegistration<IJetty>> registrations = Collections
			.synchronizedMap(new HashMap<String, ServiceRegistration<IJetty>>());

	private int lastServiceId = 0;
	private ComponentContext context;

	private File jettyWorkDir;

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void activate(ComponentContext context,
			Map<String, Object> properties) {
		logger.debug("{} started", getName());
		this.context = context;

		jettyWorkDir = new File(
				context.getBundleContext().getDataFile(""), JETTY_WORK_DIR); //$NON-NLS-1$ 
		jettyWorkDir.mkdir();
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param context
	 * @param properties
	 */
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {

		this.context = null;

		for (String pid : jetties.keySet().toArray(new String[jetties.size()])) {
			deleted(pid);
		}

		logger.debug("{} stopped", getName());
	}

	@Override
	public String getName() {
		return "IJetty Factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties)
			throws ConfigurationException {

		JettyService jetty = (JettyService) jetties.get(pid);
		if (jetty == null) {
			jetty = new JettyService(Integer.toString(++lastServiceId),
					context.getBundleContext());

			File contextWorkDir = new File(jettyWorkDir, DIR_PREFIX
					+ pid.hashCode());
			jetty.setWorkDir(contextWorkDir);
		} else {
			jetty.stop();
		}

		String name = JettyConstants.DEFAULT_SERVER_NAME;
		if (properties.get(JettyConstants.JETTY_SERVER_NAME) != null) {
			name = (String) properties.get(JettyConstants.JETTY_SERVER_NAME);
		}
		jetty.setName(name);

		// handle http properties
		handleHttp(jetty, properties);

		// handle https properties
		handleHttps(jetty, properties);

		// update the service registration
		updateServiceRegistration(pid, jetty, properties);

		// start the jetty again
		//
		jetty.start();
		logger.debug("New IJetty {} started on port {}", jetty.getName(),
				jetty.getHttpPort());
	}

	/**
	 * Updates the service registration.
	 * 
	 * @param pid
	 * @param jetty
	 * @param properties
	 */
	private void updateServiceRegistration(String pid, JettyService jetty,
			Dictionary<String, ?> properties) {
		// Register jetty as service and add to cache
		//
		Dictionary<String, Object> copyProps = copy(properties);
		copyProps.put(JettyConstants.SERVER_ID, jetty.getId());
		if (!jetties.containsKey(pid)) {
			jetties.put(pid, jetty);

			// register http jetty as a service
			ServiceRegistration<IJetty> registration = context
					.getBundleContext().registerService(IJetty.class, jetty,
							copyProps);
			registrations.put(pid, registration);
		} else {
			ServiceRegistration<IJetty> registration = registrations.get(pid);
			registration.setProperties(copyProps);
		}
	}

	/**
	 * Maps the params to a map.
	 * 
	 * @param input
	 * @return
	 */
	private Dictionary<String, Object> copy(final Dictionary<?, ?> input) {
		if (input == null) {
			return null;
		}

		final Hashtable<String, Object> result = new Hashtable<String, Object>(
				input.size());
		final Enumeration<?> keys = input.keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			try {
				result.put((String) key, (String) input.get(key));
			} catch (final ClassCastException e) {
				throw new IllegalArgumentException("Only strings are allowed",
						e);
			}
		}
		return result;
	}

	@Override
	public void deleted(String pid) {
		String jettyName = "";
		IJetty jetty = jetties.remove(pid);
		if (jetty != null) {
			jettyName = jetty.getName();
			jetty.stop();
			logger.debug("IJetty {} deleted from configuration {}",
					jetty.getName(), jetty.getHttpPort());

			// deletes the working directory of the jetty server with the given
			// pid
			deleteDirectory(new File(jettyWorkDir, DIR_PREFIX + pid.hashCode()));
		}

		// unregister http jetty as a service
		ServiceRegistration<IJetty> registration = registrations.remove(pid);
		if (registration != null) {
			registration.unregister();
			logger.debug("IJetty {} removed as a service!", jettyName);
		}

	}

	/**
	 * Deletes the given directory.
	 * 
	 * @param directory
	 * @return
	 */
	private static boolean deleteDirectory(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return directory.delete();
	}

	/**
	 * Handles the http properties.
	 * 
	 * @param jetty
	 * @param dictionary
	 */
	private void handleHttp(JettyService jetty,
			@SuppressWarnings("rawtypes") Dictionary dictionary) {
		Boolean httpEnabled = null;
		Object httpEnabledObj = dictionary.get(JettyConstants.HTTP_ENABLED);
		if (httpEnabledObj instanceof Boolean) {
			httpEnabled = (Boolean) httpEnabledObj;
		} else if (httpEnabledObj instanceof String) {
			httpEnabled = Boolean.parseBoolean(httpEnabledObj.toString());
		}
		// default is true
		if (httpEnabled != null && !httpEnabled.booleanValue()) {
			jetty.setUseHttp(false);
		}

		Integer httpPort = null;
		Object httpPortObj = dictionary.get(JettyConstants.HTTP_PORT);
		if (httpPortObj instanceof Integer) {
			httpPort = (Integer) httpPortObj;
		} else if (httpPortObj instanceof String) {
			httpPort = Integer.valueOf(httpPortObj.toString());
		}
		if (httpPort != null) {
			jetty.setHttpPort(httpPort);
		}

		Boolean nioEnabled = null;
		Object nioEnabledObj = dictionary.get(JettyConstants.HTTP_NIO);
		if (nioEnabledObj instanceof Boolean) {
			nioEnabled = (Boolean) nioEnabledObj;
		} else if (nioEnabledObj instanceof String) {
			nioEnabled = Boolean.parseBoolean(nioEnabledObj.toString());
		}
		if (nioEnabled == null) {
			nioEnabled = getDefaultNIOEnablement();
		}

		jetty.setUseNio(nioEnabled);

		jetty.setHost((String) dictionary.get(JettyConstants.HTTP_HOST));

	}

	private Boolean getDefaultNIOEnablement() {
		Properties systemProperties = System.getProperties();
		String javaVendor = systemProperties.getProperty("java.vendor", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (javaVendor.equals("IBM Corporation")) { //$NON-NLS-1$
			String javaVersion = systemProperties.getProperty(
					"java.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if (javaVersion.startsWith("1.4")) { //$NON-NLS-1$
				return Boolean.FALSE;
			}
			// Note: no problems currently logged with 1.5
			if (javaVersion.equals("1.6.0")) { //$NON-NLS-1$
				String jclVersion = systemProperties.getProperty(
						"java.jcl.version", ""); //$NON-NLS-1$ //$NON-NLS-2$
				if (jclVersion.startsWith("2007")) { //$NON-NLS-1$
					return Boolean.FALSE;
				}
				if (jclVersion.startsWith("2008") && !jclVersion.startsWith("200811") && !jclVersion.startsWith("200812")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * Handles the http properties.
	 * 
	 * @param jetty
	 * @param dictionary
	 */
	private void handleHttps(JettyService jetty,
			@SuppressWarnings("rawtypes") Dictionary dictionary) {
		Boolean httpsEnabled = null;
		Object httpsEnabledObj = dictionary.get(JettyConstants.HTTPS_ENABLED);
		if (httpsEnabledObj instanceof Boolean) {
			httpsEnabled = (Boolean) httpsEnabledObj;
		} else if (httpsEnabledObj instanceof String) {
			httpsEnabled = Boolean.parseBoolean(httpsEnabledObj.toString());
		}
		// default false
		if (httpsEnabled == null || !httpsEnabled.booleanValue()) {
			jetty.setUseHttps(false);
			return;
		}

		Integer httpsPort = null;
		Object httpsPortObj = dictionary.get(JettyConstants.HTTPS_PORT);
		if (httpsPortObj instanceof Integer) {
			httpsPort = (Integer) httpsPortObj;
		} else if (httpsPortObj instanceof String) {
			httpsPort = Integer.valueOf(httpsPortObj.toString());
		}
		if (httpsPort != null) {
			jetty.setHttpsPort(httpsPort);
		}

		jetty.setHttpsHost((String) dictionary.get(JettyConstants.HTTPS_HOST));
		jetty.setSslKeystore((String) dictionary
				.get(JettyConstants.SSL_KEYSTORE));
		jetty.setSslKeystore((String) dictionary
				.get(JettyConstants.SSL_PASSWORD));
		jetty.setSslKeystore((String) dictionary
				.get(JettyConstants.SSL_KEYPASSWORD));

		jetty.setSslNeedsClientAuth(false);
		Object needClientAuth = dictionary
				.get(JettyConstants.SSL_NEEDCLIENTAUTH);
		if (needClientAuth != null) {
			if (needClientAuth instanceof String) {
				needClientAuth = Boolean.valueOf((String) needClientAuth);
			}
			jetty.setSslNeedsClientAuth((Boolean) needClientAuth);
		}

		jetty.setSslWantsClientAuth(false);
		Object wantClientAuth = dictionary
				.get(JettyConstants.SSL_WANTCLIENTAUTH);
		if (wantClientAuth != null) {
			if (wantClientAuth instanceof String) {
				wantClientAuth = Boolean.valueOf((String) wantClientAuth);
			}
			jetty.setSslWantsClientAuth((Boolean) wantClientAuth);
		}

		jetty.setSslKeystore((String) dictionary
				.get(JettyConstants.SSL_PROTOCOL));

		jetty.setSslKeystore((String) dictionary
				.get(JettyConstants.SSL_KEYSTORETYPE));

	}
}
