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
import java.util.Map;

import org.lunifera.runtime.web.common.AbstractDisposable;
import org.lunifera.runtime.web.common.IConstants;
import org.lunifera.runtime.web.common.IUserInfo;
import org.lunifera.runtime.web.common.IWebContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

public class WebContext extends AbstractDisposable implements IWebContext {

	private IUserInfo userInfo = null;
	private String id;

	private PreferencesService preferencesService;
	// private Location userLocation;
	private Map<String, Object> properties;

	/**
	 * Called by OSGi-DS.
	 */
	public WebContext() {

	}

	protected void activate(ComponentContext context,
			Map<String, Object> properties) {
		this.properties = properties;
		id = (String) properties.get(IConstants.OSGI_PROPERTY__WEB_CONTEXT__ID);
		// prepare the user info
		userInfo = new AbstractUserInfo(
				(String) properties
						.get(IConstants.OSGI_PROPERTY__WEB_CONTEXT__USER)) {
			@Override
			public Preferences getPreferences(String qualifier) {
				if (preferencesService == null || getId() == null) {
					return null;
				}
				return preferencesService.getUserPreferences(getId());
			}

			// @Override
			// public String getLocation() {
			// return System.getProperty("user.home") + "/" + getId();
			// }
		};

	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public IUserInfo getUserInfo() {
		return userInfo;
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
		userInfo = null;
	}

	/**
	 * Called by OSGi-DS
	 */
	protected void deactivate() {
		if (!isDisposed()) {
			// should not happen for normal. But if the context was deactivated
			// by ComponentInstance.dispose() we ensure that WebContext.dispose
			// is called too.
			dispose();
		}
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

	// /**
	// * Called by OSGi-DS
	// *
	// * @param service
	// */
	// public void setUserLocation(Location userLocation) {
	// this.userLocation = userLocation;
	// }
	//
	// /**
	// * Called by OSGi-DS
	// *
	// * @param service
	// */
	// public void unsetUserLocation(Location userLocation) {
	// this.userLocation = null;
	// }

	@Override
	public <A> A getUI() {
		return (A) null;
	}

	@Override
	public Object getProperty(String property) {
		return properties.get(property);
	}

}
