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

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.lunifera.runtime.web.common.IConstants;
import org.lunifera.runtime.web.common.IDisposable;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.IWebContextRegistry;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

public class WebContextRegistry implements IDisposable.Listener,
		IWebContextRegistry {

	private ComponentFactory factory;
	private Map<String, ComponentInstance> instances = Collections
			.synchronizedMap(new HashMap<String, ComponentInstance>());

	/**
	 * Called by OSGi-DS. Sets the component factory that is used to create
	 * instances of web context.
	 * 
	 * @param factory
	 */
	protected void setFactory(ComponentFactory factory) {
		this.factory = factory;
	}

	/**
	 * Called by OSGi-DS. Unsets the component factory that is used to create
	 * instances of web context.
	 * 
	 * @param factory
	 */
	protected void unsetFactory(ComponentFactory factory) {
		this.factory = null;
	}

	@Override
	public IWebContext createContext(String user,
			Map<String, Object> inProperties) {

		// prepare the properties
		//
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		String id = UUID.randomUUID().toString();
		properties.put(IConstants.OSGI_PROPERTY__WEB_CONTEXT__ID, id);
		properties.put(IConstants.OSGI_PROPERTY__WEB_CONTEXT__USER, user);
		properties.putAll(inProperties);

		// create the instance of the web context
		//
		ComponentInstance instance = factory.newInstance(properties);
		instances.put(id, instance);

		// register the registry as a dispose listener to observe the lifecycle
		// of the context
		//
		IWebContext context = (IWebContext) instance.getInstance();
		context.addDisposeListener(this);

		return context;
	}

	@Override
	public int size() {
		return instances.size();
	}

	@Override
	public IWebContext getContext(String id) {
		return (IWebContext) (instances.containsKey(id) ? instances.get(id)
				.getInstance() : null);
	}

	/**
	 * Notifies that a web context was disposed.
	 */
	@Override
	public void notifyDisposed(IDisposable notifier) {

		// remove this as a dispose listener
		//
		notifier.removeDisposeListener(this);

		// look for the context
		//
		String disposedId = null;
		synchronized (instances) {
			for (Map.Entry<String, ComponentInstance> entry : instances
					.entrySet()) {
				if (notifier == entry.getValue().getInstance()) {
					disposedId = entry.getKey();
					break;
				}
			}
		}

		// dispose its instance
		//
		if (disposedId != null) {
			ComponentInstance instance = instances.remove(disposedId);
			instance.dispose();
		}
	}
}
