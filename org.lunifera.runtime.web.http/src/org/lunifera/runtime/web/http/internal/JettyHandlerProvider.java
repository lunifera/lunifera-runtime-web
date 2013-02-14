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
package org.lunifera.runtime.web.http.internal;

import org.eclipse.jetty.server.Handler;
import org.lunifera.runtime.web.jetty.IHandlerProvider;
import org.lunifera.runtime.web.jetty.IJetty;

/**
 * Provides the {@link HttpApplicationServletContextHandler} as a
 * ServletContextHandler to the assigned {@link IJetty jetty server}.
 */
public class JettyHandlerProvider implements IHandlerProvider {

	private final HttpApplicationServletContextHandler handler;

	public JettyHandlerProvider(HttpApplicationServletContextHandler handler) {
		this.handler = handler;
	}

	@Override
	public Handler getHandler() {
		return handler;
	}

}
