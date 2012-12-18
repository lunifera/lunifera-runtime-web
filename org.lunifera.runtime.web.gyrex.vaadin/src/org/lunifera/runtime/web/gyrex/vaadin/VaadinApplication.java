/**
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package org.lunifera.runtime.web.gyrex.vaadin;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.http.application.Application;
import org.eclipse.gyrex.http.application.context.IApplicationContext;
import org.lunifera.web.vaadin.servlet.VaadinOSGiServlet;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinServlet;

/**
 * Base class for HTTP Applications with support for vaadin sessions.
 * <p>
 * This class may be instantiated or extended by clients.
 * </p>
 */
public class VaadinApplication extends Application {

	private static final org.slf4j.Logger LOG = LoggerFactory
			.getLogger(VaadinApplication.class);

	/**
	 * Creates a new instance.
	 * 
	 * @param id
	 * @param context
	 */
	protected VaadinApplication(final String id, final IRuntimeContext context) {
		super(id, context);
	}

	/**
	 * Called by {@link #doInit()} to creates the vaadin servlet object.
	 * 
	 * @return the vaadin servlet object (must not be <code>null</code>)
	 */
	protected VaadinServlet createVaadinServlet() {
		return new VaadinOSGiServlet(null, null);
	}

	/**
	 * Initializes the vaadin servlet and registers it at the configured
	 * {@link #getAlias() alias}.
	 * <p>
	 * Subclasses may override and perform additional initialization. However,
	 * they must call super to initialize the vaadin servlet.
	 * </p>
	 * 
	 * @throws IllegalStateException
	 *             in case the initialization can not be completed currently but
	 *             may be repeated at a later time
	 * @throws Exception
	 *             in case of unrecoverable initialization failures
	 */
	@Override
	protected void doInit() throws IllegalStateException, Exception {
		final VaadinOSGiServlet servlet = createVaadinServlet();
		if (null == jaxRsApplication) {
			throw new IllegalStateException(
					"no application returned by createJaxRsApplication");
		}

		// install the SLF4J bridge if necessary
		installSlf4jBridgeIfNecessary();

		// register
		getApplicationContext().registerServlet(getAlias(),
				new ServletContainer(jaxRsApplication), null);
	}

	/**
	 * Returns an alias for registering the vaadin servlet.
	 * <p>
	 * The default implementation returns the root alias '/'. Subclasses may
	 * override to return a custom alias. However, the returned alias must
	 * comply to the alias rules spec'd by
	 * {@link IApplicationContext#registerServlet(String, javax.servlet.Servlet, java.util.Map)}
	 * for proper registration.
	 * </p>
	 * 
	 * @return the alias for registering the vaadin servlet
	 */
	protected String getAlias() {
		return "/";
	}
}
