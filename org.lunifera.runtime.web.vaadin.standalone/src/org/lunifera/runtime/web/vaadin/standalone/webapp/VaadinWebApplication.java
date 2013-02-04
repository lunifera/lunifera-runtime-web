/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.standalone.webapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.lunifera.runtime.web.common.IWebContextRegistry;
import org.lunifera.runtime.web.vaadin.common.OSGiUIProvider;
import org.lunifera.runtime.web.vaadin.standalone.common.Constants;
import org.lunifera.runtime.web.vaadin.standalone.common.IVaadinWebApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.UIProvider;

/**
 * A vaadin web application that is activated by OSGi DS.
 */
public class VaadinWebApplication implements IVaadinWebApplication {

	private Logger logger = LoggerFactory.getLogger(VaadinWebApplication.class);

	private BundleContext bundleContext;
	private String alias;
	private String id;
	private String widgetsetName;

	private HttpServiceTracker tracker;
	private UIProviderTracker uiProviderTracker;
	private List<OSGiUIProvider> uiProviders = new ArrayList<OSGiUIProvider>();

	public void activate(final ComponentContext context) {

		// remove the / from the alias
		alias = (String) context.getProperties().get(
				Constants.PROP_WEBAPP__ALIAS);
		if (alias != null) {
			alias = alias.replace("/", "");
		}

		widgetsetName = (String) context.getProperties().get(
				Constants.PROP_WIDGETSET);

		id = (String) context.getProperties().get("component.name");
		// remember bundle for later use
		bundleContext = context.getBundleContext();

		try {
			uiProviderTracker = new UIProviderTracker(bundleContext, this);
		} catch (InvalidSyntaxException e) {
			new RuntimeException(e);
		}
		uiProviderTracker.open();

		tracker = new HttpServiceTracker(context.getBundleContext(), this);
		logger.debug("The alias that will be tracked is:\"" + alias);
		tracker.open();
	}

	public void deactivate(final ComponentContext context) {
		logger.debug("Tracker for alias" + tracker.getAlias() + " was removed.");
		if (uiProviderTracker != null) {
			uiProviderTracker.close();
			uiProviderTracker = null;
		}
		if (tracker != null) {
			tracker.close();
			tracker = null;
		}

		uiProviders = null;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public String getWidgetSetName() {
		return widgetsetName;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary properties) {
		if (properties.get(Constants.PROP_WIDGETSET) != null) {
			widgetsetName = (String) properties.get(Constants.PROP_WIDGETSET);
		}
	}

	/**
	 * Adds a new {@link UIProvider} to the web application.
	 * 
	 * @param provider
	 */
	public void addOSGiUIProvider(OSGiUIProvider provider) {
		uiProviders.add(provider);
	}

	/**
	 * Removes an {@link UIProvider} from the web application.
	 * 
	 * @param provider
	 */
	public void removeOSGiUIProvider(OSGiUIProvider provider) {
		uiProviders.remove(provider);
	}

	@Override
	public List<OSGiUIProvider> getUiProviders() {
		return Collections.unmodifiableList(uiProviders);
	}

}
