/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.common;

/**
 * An accessor class that returns objects that are registered with the current
 * thread.
 */
public class ContextAccess {

	private static final ThreadLocal<IWebContext> webContext = new ThreadLocal<IWebContext>();

	/**
	 * Returns the context that was registered with the current thread. If no
	 * context was registered then <code>null</code> will be returned.
	 * 
	 * @return
	 */
	public static IWebContext getContext() {
		return webContext.get();
	}

	/**
	 * Registers the given web context with the current thread.
	 * 
	 * @param session
	 */
	public static void setContext(IWebContext session) {
		webContext.set(session);
	}

}
