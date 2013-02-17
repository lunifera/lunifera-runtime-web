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
package org.lunifera.runtime.web.vaadin.osgi.webapp;

import java.util.HashMap;
import java.util.Map;

import org.lunifera.runtime.web.vaadin.common.Constants;
import org.lunifera.runtime.web.vaadin.common.OSGiUIProvider;
import org.lunifera.runtime.web.vaadin.osgi.common.IVaadinApplication;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;
import org.osgi.util.tracker.ServiceTracker;

import com.vaadin.ui.UI;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UIProviderTracker extends ServiceTracker {
	private final IVaadinApplication webApplication;
	private final Map<ComponentFactory, OSGiUIProvider> providerMapping = new HashMap<ComponentFactory, OSGiUIProvider>();

	public UIProviderTracker(BundleContext ctx,
			IVaadinApplication webApplication) throws InvalidSyntaxException {
		super(ctx, ctx.createFilter("(component.factory="
				+ Constants.OSGI_COMP_FACTORY__VAADIN_UI + "/*)"), null);
		this.webApplication = webApplication;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		Object o = super.addingService(reference);
		ComponentFactory factory = (ComponentFactory) o;

		String name = (String) reference.getProperty("component.factory");
		String className = name.substring(Constants.PREFIX__UI_CLASS.length());
		OSGiUIProvider uiProvider = null;
		try {
			uiProvider = new OSGiUIProvider(factory,
					(Class<? extends UI>) reference.getBundle().loadClass(
							className));
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (uiProvider != null) {
			providerMapping.put(factory, uiProvider);
			webApplication.addOSGiUIProvider(uiProvider);
		}

		return o;
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		ComponentFactory factory = (ComponentFactory) service;

		if (providerMapping.containsKey(factory)) {
			webApplication
					.removeOSGiUIProvider(providerMapping.remove(factory));
		}
	}

}
