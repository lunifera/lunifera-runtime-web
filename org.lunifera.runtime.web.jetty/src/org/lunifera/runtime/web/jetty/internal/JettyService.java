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
package org.lunifera.runtime.web.jetty.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.lunifera.runtime.web.jetty.IJetty;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyService implements IJetty {

	private static final Logger logger = LoggerFactory
			.getLogger(JettyService.class);

	private boolean started;
	private final String id;
	private String name;

	private boolean useNio;
	private boolean useHttp = true;
	private String host;
	private int httpPort;

	private File workDir;

	private boolean useHttps;
	private String httpsHost;
	private int httpsPort;
	private String sslProtocol;
	private String sslPassword;
	private boolean sslNeedsClientAuth;
	private boolean sslWantsClientAuth;
	private String sslKeystore;
	private String sslKeystoreType;
	private String sslKeyPassword;

	private Server server;

	public JettyService(String id, BundleContext context) {
		if (id == null) {
			throw new NullPointerException("Id must not be null!");
		}
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of that server.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * Sets the http port of that server.
	 * 
	 * @param httpPort
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	@Override
	public boolean isUseHttp() {
		return useHttp;
	}

	/**
	 * @param useHttp
	 *            the useHttp to set
	 */
	public void setUseHttp(boolean useHttp) {
		this.useHttp = useHttp;
	}

	public boolean isUseNio() {
		return useNio;
	}

	/**
	 * @param useNio
	 *            the useNio to set
	 */
	public void setUseNio(boolean useNio) {
		this.useNio = useNio;
	}

	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getHttpsHost() {
		return httpsHost;
	}

	/**
	 * @param httpsHost
	 *            the httpsHost to set
	 */
	public void setHttpsHost(String httpsHost) {
		this.httpsHost = httpsHost;
	}

	@Override
	public boolean isUseHttps() {
		return useHttps;
	}

	/**
	 * @param useHttps
	 *            the useHttps to set
	 */
	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
	}

	@Override
	public String getSSLKeystore() {
		return sslKeystore;
	}

	/**
	 * @param sslKeystore
	 *            the sslKeystore to set
	 */
	public void setSslKeystore(String sslKeystore) {
		this.sslKeystore = sslKeystore;
	}

	@Override
	public String getSSLKeystoreType() {
		return sslKeystoreType;
	}

	/**
	 * @param sslKeystoreType
	 *            the sslKeystoreType to set
	 */
	public void setSslKeystoreType(String sslKeystoreType) {
		this.sslKeystoreType = sslKeystoreType;
	}

	@Override
	public String getSSLPassword() {
		return sslPassword;
	}

	/**
	 * @param sslPassword
	 *            the sslPassword to set
	 */
	public void setSslPassword(String sslPassword) {
		this.sslPassword = sslPassword;
	}

	@Override
	public String getSSLKeyPassword() {
		return sslKeyPassword;
	}

	/**
	 * @param sslKeyPassword
	 *            the sslKeyPassword to set
	 */
	public void setSslKeyPassword(String sslKeyPassword) {
		this.sslKeyPassword = sslKeyPassword;
	}

	@Override
	public boolean isSSLNeedClientAuthentication() {
		return sslNeedsClientAuth;
	}

	/**
	 * @param sslNeedsClientAuth
	 *            the sslNeedsClientAuth to set
	 */
	public void setSslNeedsClientAuth(boolean sslNeedsClientAuth) {
		this.sslNeedsClientAuth = sslNeedsClientAuth;
	}

	@Override
	public boolean isSSLWantsClientAuthentication() {
		return sslWantsClientAuth;
	}

	/**
	 * @param sslWantsClientAuth
	 *            the sslWantsClientAuth to set
	 */
	public void setSslWantsClientAuth(boolean sslWantsClientAuth) {
		this.sslWantsClientAuth = sslWantsClientAuth;
	}

	@Override
	public String getSSLProtocol() {
		return sslProtocol;
	}

	/**
	 * @param sslProtocol
	 *            the sslProtocol to set
	 */
	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	public File getWorkDir() {
		return workDir;
	}

	/**
	 * @param workDir
	 *            the workDir to set
	 */
	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	@Override
	public int getHttpsPort() {
		return httpsPort;
	}

	/**
	 * Sets the httpsPort of that server.
	 * 
	 * @param httpsPort
	 */
	public void setHttpsPort(int httpsPort) {
		this.httpsPort = httpsPort;
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 * Is called to start the server. All resources should be registered and the
	 * service is registered.
	 */
	public void start() {
		if (started) {
			logger.debug("Httpserver {} is already started", getName());
			return;
		}
		if (server != null) {
			logger.error("Server already exists!");
			throw new IllegalStateException("Server already exists!");
		}

		try {
			server = new Server();

			// use http?
			if (isUseHttp()) {
				Connector httpConnector = createHttpConnector();
				if (httpConnector != null) {
					server.addConnector(httpConnector);
				}
			}

			// use https?
			if (isUseHttps()) {
				Connector httpsConnector = createHttpsConnector();
				if (httpsConnector != null) {
					server.addConnector(httpsConnector);
				}
			}

			try {
				server.start();
			} catch (Exception e) {
				logger.error("{}", e);
			}

		} finally {
			started = true;
		}
	}

	/**
	 * Is called to stop the server. All resources should be unregistered and
	 * the service will become disposed.
	 */
	public void stop() {
		if (!started) {
			logger.debug("Httpserver {} not started", getName());
			return;
		}

		try {
			if (server != null) {
				try {
					server.stop();
					server.destroy();
					server = null;
				} catch (Exception e) {
					logger.debug("{}", e);
				}
			}

		} finally {
			started = false;
		}
	}

	/**
	 * Creates the http connector.
	 * 
	 * @return
	 */
	private Connector createHttpConnector() {
		Connector connector;
		if (isUseNio()) {
			connector = new SelectChannelConnector();
		} else {
			connector = new SocketConnector();
		}

		connector.setPort(getHttpPort());

		if (isValid(getHost())) {
			connector.setHost(getHost());
		}

		if (connector.getPort() == 0) {
			try {
				connector.open();
			} catch (IOException e) {
				// this would be unexpected since we're opening the next
				// available port
				logger.error("{}", e);
			}
		}
		return connector;
	}

	@SuppressWarnings("deprecation")
	private Connector createHttpsConnector() {
		if (!isUseHttps()) {
			return null;
		}

		SslSocketConnector sslConnector = new SslSocketConnector();
		sslConnector.setPort(getHttpsPort());

		if (isValid(getHttpsHost())) {
			sslConnector.setHost(getHttpsHost());
		}

		if (isValid(getSSLKeystore())) {
			sslConnector.setKeystore(getSSLKeystore());
		}

		if (isValid(getSSLPassword())) {
			sslConnector.setPassword(getSSLPassword());
		}

		if (isValid(getSSLKeyPassword())) {
			sslConnector.setKeyPassword(getSSLKeyPassword());
		}

		sslConnector.setNeedClientAuth(isSSLNeedClientAuthentication());
		sslConnector.setWantClientAuth(isSSLWantsClientAuthentication());

		if (isValid(getSSLProtocol())) {
			sslConnector.setProtocol(getSSLProtocol());
		}

		if (isValid(getSSLKeystoreType())) {
			sslConnector.setKeystoreType(getSSLKeystoreType());
		}

		if (sslConnector.getPort() == 0) {
			try {
				sslConnector.open();
			} catch (IOException e) {
				// this would be unexpected since we're opening the next
				// available port
				e.printStackTrace();
			}
		}
		return sslConnector;
	}

	private boolean isValid(String value) {
		return value != null && !value.equals("");
	}

}
