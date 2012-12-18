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
package org.lunifera.web.vaadin.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lunifera.web.vaadin.Constants;
import org.lunifera.web.vaadin.OSGiUIProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.ComponentFactory;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class OSGiVaadinServiceSession extends VaadinSession {

	private Map<ComponentFactory, OSGiUIProvider> uiProviders = new HashMap<ComponentFactory, OSGiUIProvider>();
	private BundleContext context;

	private List<Pair> pendingFactories = new ArrayList<Pair>();

	public OSGiVaadinServiceSession(VaadinService service) {
		super(service);
	}

	public void activate(BundleContext context) throws InvalidSyntaxException {
		this.context = context;

		for (Pair pair : pendingFactories) {
			addFactory(pair);
		}
		pendingFactories.clear();
	}

	public void deactivate() {
		context = null;
	}

	/**
	 * Called by OSGi-DS.
	 * 
	 * @param factory
	 * @param properties
	 */
	public void addUI(ComponentFactory factory, Map<String, Object> properties) {
		pendingFactories.add(new Pair(factory, properties));
	}

	@SuppressWarnings("unchecked")
	public void addFactory(Pair pair) {
		ComponentFactory factory = pair.getFactory();
		String name = (String) pair.getProperties().get("component.factory");
		String className = name.substring(Constants.PREFIX__UI_CLASS.length());
		OSGiUIProvider uiProvider = null;
		try {
			uiProvider = new OSGiUIProvider(factory,
					(Class<? extends UI>) context.getBundle().loadClass(
							className), null);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
		if (uiProvider != null) {
			uiProviders.put(factory, uiProvider);
			addUIProvider(uiProvider);
		}
	}

	/**
	 * A pair of factory and properties.
	 */
	private static class Pair {
		private final ComponentFactory factory;
		private final Map<String, Object> properties;

		public Pair(ComponentFactory factory, Map<String, Object> properties) {
			super();
			this.factory = factory;
			this.properties = properties;
		}

		public ComponentFactory getFactory() {
			return factory;
		}

		public Map<String, Object> getProperties() {
			return properties;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((factory == null) ? 0 : factory.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (factory == null) {
				if (other.factory != null)
					return false;
			} else if (!factory.equals(other.factory))
				return false;
			return true;
		}

	}
}
