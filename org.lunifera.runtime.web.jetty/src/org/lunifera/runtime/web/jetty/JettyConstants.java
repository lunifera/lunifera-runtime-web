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
package org.lunifera.runtime.web.jetty;

import org.osgi.service.cm.ManagedServiceFactory;

/**
 * Constants for that bundle.
 */
public interface JettyConstants extends org.osgi.framework.Constants {

	/**
	 * The persistence ID for the {@link ManagedServiceFactory}.
	 */
	public static final String OSGI__FACTORY_PID = "org.lunifera.runtime.web.jetty.server.factory";

	/**
	 * The automatic given jetty server id. Can not be specified using cm.
	 */
	public static final String SERVER_ID = "lunifera.jetty.id";

	/**
	 * The name of the jetty server instance that should be created. Default
	 * value is {@link #DEFAULT_SERVER_NAME}
	 */
	public static final String JETTY_SERVER_NAME = "lunifera.jetty.name";

	/**
	 * name="http.enabled" type="Boolean" (default: true)
	 */
	public static final String HTTP_ENABLED = "lunifera.jetty.http.enabled"; //$NON-NLS-1$

	/**
	 * name="http.port" type="Integer" (default: 0 -- first available port)
	 */
	public static final String HTTP_PORT = "lunifera.jetty.http.port"; //$NON-NLS-1$

	/**
	 * name="http.host" type="String" (default: 0.0.0.0 -- all network adapters)
	 */
	public static final String HTTP_HOST = "lunifera.jetty.http.host"; //$NON-NLS-1$

	/**
	 * name="http.nio" type="Boolean" (default: true, with some exceptions for
	 * JREs with known NIO problems)
	 * 
	 * @since 1.1
	 */
	public static final String HTTP_NIO = "lunifera.jetty.http.nio"; //$NON-NLS-1$

	/**
	 * name="https.enabled" type="Boolean" (default: false)
	 */
	public static final String HTTPS_ENABLED = "lunifera.jetty.https.enabled"; //$NON-NLS-1$

	/**
	 * name="https.host" type="String" (default: 0.0.0.0 -- all network
	 * adapters)
	 */
	public static final String HTTPS_HOST = "lunifera.jetty.https.host"; //$NON-NLS-1$

	/**
	 * name="https.port" type="Integer" (default: 0 -- first available port)
	 */
	public static final String HTTPS_PORT = "lunifera.jetty.https.port"; //$NON-NLS-1$

	/**
	 * name="ssl.keystore" type="String"
	 */
	public static final String SSL_KEYSTORE = "lunifera.jetty.ssl.keystore"; //$NON-NLS-1$

	/**
	 * name="ssl.password" type="String"
	 */
	public static final String SSL_PASSWORD = "lunifera.jetty.ssl.password"; //$NON-NLS-1$

	/**
	 * name="ssl.keypassword" type="String"
	 */
	public static final String SSL_KEYPASSWORD = "lunifera.jetty.ssl.keypassword"; //$NON-NLS-1$

	/**
	 * name="ssl.needclientauth" type="Boolean"
	 */
	public static final String SSL_NEEDCLIENTAUTH = "lunifera.jetty.ssl.needclientauth"; //$NON-NLS-1$

	/**
	 * name="ssl.wantclientauth" type="Boolean"
	 */
	public static final String SSL_WANTCLIENTAUTH = "lunifera.jetty.ssl.wantclientauth"; //$NON-NLS-1$

	/**
	 * name="ssl.protocol" type="String"
	 */
	public static final String SSL_PROTOCOL = "lunifera.jetty.ssl.protocol"; //$NON-NLS-1$

	/**
	 * name="ssl.algorithm" type="String"
	 */
	public static final String SSL_ALGORITHM = "lunifera.jetty.ssl.algorithm"; //$NON-NLS-1$

	/**
	 * name="ssl.keystoretype" type="String"
	 */
	public static final String SSL_KEYSTORETYPE = "lunifera.jetty.ssl.keystoretype"; //$NON-NLS-1$

	/**
	 * The default server name.
	 */
	public static final String DEFAULT_SERVER_NAME = "defaultserver";

}
