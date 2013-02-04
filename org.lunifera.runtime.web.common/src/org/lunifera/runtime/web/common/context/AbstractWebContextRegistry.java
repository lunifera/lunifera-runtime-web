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
package org.lunifera.runtime.web.common.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lunifera.runtime.web.common.IDisposable;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.IWebContextRegistry;
import org.osgi.service.component.ComponentFactory;

public abstract class AbstractWebContextRegistry implements
		IDisposable.Listener, IWebContextRegistry {

	private ComponentFactory factory;
	private Map<String, IWebContext> contexts = Collections
			.synchronizedMap(new HashMap<String, IWebContext>());

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
	public int size() {
		return contexts.size();
	}

	@Override
	public IWebContext getContext(String id) {
		return (IWebContext) (contexts.containsKey(id) ? contexts.get(id)
				: null);
	}

	@Override
	public IWebContext createContext(String user, Map<String, Object> properties) {
		IWebContext context = doCreateContext(factory, user, properties);
		contexts.put(context.getId(), context);
		return context;
	}

	/**
	 * Delegates the creation of the context to the subclass.
	 * 
	 * @param factory
	 * @param user
	 * @param properties
	 * @return
	 */
	protected abstract IWebContext doCreateContext(ComponentFactory factory,
			String user, Map<String, Object> properties);

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
		synchronized (contexts) {
			for (Map.Entry<String, IWebContext> entry : contexts.entrySet()) {
				if (notifier == entry.getValue()) {
					disposedId = entry.getKey();
					break;
				}
			}

			if (disposedId != null) {
				contexts.remove(disposedId);
			}
		}
	}
}
