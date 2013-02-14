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

import org.eclipse.jetty.server.Handler;

/**
 * Provides a {@link Handler jetty handler} that will become added to a given
 * jetty.
 */
public interface IHandlerProvider {

	/**
	 * Returns a jetty handler that will be added to the root handler collection
	 * of the assigned jetty server.
	 * 
	 * @return
	 */
	Handler getHandler();

}
