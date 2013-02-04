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
package org.lunifera.runtime.web.vaadin.common;

import java.security.Principal;
import java.util.Hashtable;

import org.lunifera.runtime.web.common.IDisposable;
import org.lunifera.runtime.web.common.IWebContextRegistry;
import org.lunifera.runtime.web.vaadin.common.internal.VaadinWebContext;
import org.osgi.service.component.ComponentInstance;

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class OSGiUI extends UI implements SessionDestroyListener,
		IDisposable.Listener, IVaadinWebContext.Provider {

	private ComponentInstance instance;
	private IWebContextRegistry registry;
	private IVaadinWebContext webContext;

	/**
	 * Sets the component instance that can be used to dispose the instance of
	 * that UI
	 * 
	 * @param instance
	 */
	public void setComponentInstance(ComponentInstance instance) {
		if (this.instance != null) {
			throw new IllegalArgumentException(
					"Component instance may only be set onece!");
		}
		this.instance = instance;
	}

	@Override
	public void setSession(VaadinSession session) {
		super.setSession(session);

		initContext();
	}

	/**
	 * Initializes the web context.
	 */
	protected void initContext() {
		if (registry != null) {
			Principal user = VaadinService.getCurrentRequest()
					.getUserPrincipal();
			Hashtable<String, Object> properties = new Hashtable<String, Object>();
			webContext = (IVaadinWebContext) registry.createContext(
					user != null ? user.getName() : null, properties);
			webContext.addDisposeListener(this);

			((VaadinWebContext) webContext).setUi(this);
		}
	}

	@Override
	public void attach() {
		super.attach();

		getSession().getService().addSessionDestroyListener(this);
	}

	public void sessionDestroy(SessionDestroyEvent event) {
		dispose();
	}

	@Override
	public void notifyDisposed(IDisposable notifier) {
		// context was disposed
		if (!isClosing()) {
			close();
		}
	}

	@Override
	public void detach() {
		getSession().getService().removeSessionDestroyListener(this);

		super.detach();

		dispose();
	}

	/**
	 * Is called to remove the instance as an OSGi service and to cleanup the
	 * OSGi runtime.
	 */
	protected void dispose() {
		if (webContext != null) {
			webContext.removeDisposeListener(this);
			webContext.dispose();
			webContext = null;
		}

		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}

	/**
	 * Called by OSGi-DS
	 * 
	 * @param registry
	 */
	protected void setWebContextRegistry(IWebContextRegistry registry) {
		this.registry = registry;
	}

	/**
	 * Returns the web context. May return <code>null</code> if no context was
	 * prepared.
	 * 
	 * @return the webContext
	 */
	public IVaadinWebContext getWebContext() {
		return webContext;
	}

}
