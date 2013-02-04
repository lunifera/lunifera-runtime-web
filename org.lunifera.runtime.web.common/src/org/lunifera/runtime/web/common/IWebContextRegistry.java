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
package org.lunifera.runtime.web.common;

import java.util.Map;

/**
 * A registry for web contexts. Web contexts are created by that service. And it
 * also observes the lifecycle of them.
 */
public interface IWebContextRegistry {

	/**
	 * Returns the count of the contained contexts.
	 * 
	 * @return
	 */
	int size();

	/**
	 * Creates a webcontext for the given user.
	 * 
	 * @param user
	 * @param properties
	 *            to be used to create the context
	 * @return
	 */
	IWebContext createContext(String user, Map<String, Object> properties);

	/**
	 * Returns the webcontext for the given id or <code>null</code> if no
	 * context is available.
	 * 
	 * @param id
	 * @return
	 */
	IWebContext getContext(String id);
}