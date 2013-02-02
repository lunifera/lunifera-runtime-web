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
package org.lunifera.runtime.web.common.internal;

import java.util.Locale;

import org.lunifera.runtime.web.common.AbstractDisposable;
import org.lunifera.runtime.web.common.IUserInfo;
import org.lunifera.runtime.web.common.IWebContext;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

public class WebContext extends AbstractDisposable implements IWebContext {

	private PreferencesService preferencesService;
	// TODO
	private final IUserInfo userInfo = null;

	@Override
	public Preferences getPreferences() {
		return preferencesService != null ? preferencesService
				.getUserPreferences(userInfo.getId()) : null;
	}

	@Override
	public void getI18nService() {

	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	protected void internalDispose() {

	}

	@Override
	public void syncExec(Runnable runnable) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void asyncExec(Runnable runnable) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param service
	 */
	protected void setPreferencesService(PreferencesService service) {
		this.preferencesService = service;
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param service
	 */
	protected void unsetPreferencesService(PreferencesService service) {
		this.preferencesService = null;
	}

	@Override
	public IUserInfo getUserInfo() {
		return null;
	}

}
