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

import java.util.Locale;

import org.osgi.service.prefs.Preferences;

/**
 * A web context is an object that offers access to features that are related
 * with a browser tab. One http sessoin may have many web contexts. The web
 * context is attached to the current thread as far as a request is processed by
 * the http servlet. For concurrent threads no web context is attached to them.
 * So concurrent threads have to query the web context and use the sync-methods
 * to run a runnable in a web context.
 */
public interface IWebContext extends IDisposable {

	/**
	 * Returns the preferences associated with that context.
	 * 
	 * @return
	 */
	Preferences getPreferences();

	/**
	 * Returns the i18n service configured with the settings of that context.
	 * For instance the locale of the context will be used to return the proper
	 * i18n service.
	 * 
	 * @return
	 */
	void getI18nService();

	/**
	 * Returns the locale of that context.
	 * 
	 * @return
	 */
	Locale getLocale();
	
	/**
	 * Returns the user info this context is assigned to. Never <code>null</code>.
	 * @return
	 */
	IUserInfo getUserInfo();

	/**
	 * Executes the given runnable in the context of that web context. Therefore
	 * it will sync with the web environment before executing the runnable.
	 * 
	 * @param runnable
	 */
	public void syncExec(Runnable runnable);

	/**
	 * Executes the given runnable in the context of that web context in an
	 * async mode. Therefore it will sync with the web environment before
	 * executing the runnable.
	 * 
	 * @param runnable
	 */
	public void asyncExec(Runnable runnable);
}
