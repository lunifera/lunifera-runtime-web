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

import java.io.File;

public interface IJetty {

	/**
	 * Returns the id of that server. Can not be changed.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Returns the name for the server.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Returns the port.
	 * 
	 * @return
	 */
	int getHttpPort();

	/**
	 * Returns true, if http should be used.
	 * 
	 * @return the useHttp
	 */
	public boolean isUseHttp();

	/**
	 * Returns true, if NIO should be used.
	 * 
	 * @return the useNio
	 */
	public boolean isUseNio();

	/**
	 * Returns the host of the server.
	 * 
	 * @return the host
	 */
	public String getHost();

	/**
	 * Returns the working directory of the server.
	 * 
	 * @return the workDir
	 */
	public File getWorkDir();

	/**
	 * Returns the port.
	 * 
	 * @return
	 */
	int getHttpsPort();

	/**
	 * Returns the https host of the server.
	 * 
	 * @return the httpsHost
	 */
	public String getHttpsHost();

	/**
	 * Returns true, if http should be used.
	 * 
	 * @return the useHttp
	 */
	public boolean isUseHttps();

	/**
	 * Returns the ssl keystore.
	 * 
	 * @return
	 */
	String getSSLKeystore();

	/**
	 * Returns the type of the ssl keystore.
	 * 
	 * @return
	 */
	String getSSLKeystoreType();

	/**
	 * Returns the password of the ssl connection.
	 * 
	 * @return
	 */
	String getSSLPassword();

	/**
	 * Returns the key password of the ssl connection.
	 * 
	 * @return
	 */
	String getSSLKeyPassword();

	/**
	 * Returns true if the client needs authentication.
	 * 
	 * @return
	 */
	boolean isSSLNeedClientAuthentication();

	/**
	 * Returns true if the client wants authentication.
	 * 
	 * @return
	 */
	boolean isSSLWantsClientAuthentication();

	/**
	 * Returns the protocol of the ssl conntection.
	 * 
	 * @return
	 */
	String getSSLProtocol();

	/**
	 * Returns true, if the jetty server is started.
	 * 
	 * @return
	 */
	boolean isStarted();

	/**
	 * Is called to start the jetty server. Calling this method twice has no
	 * effect.
	 */
	void start();

	/**
	 * Is called to stop the jetty server. Calling this method twice has no
	 * effect.
	 */
	void stop();

}
