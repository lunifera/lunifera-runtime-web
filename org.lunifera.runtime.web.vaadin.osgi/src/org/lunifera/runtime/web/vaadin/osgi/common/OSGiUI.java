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
package org.lunifera.runtime.web.vaadin.osgi.common;

import org.lunifera.runtime.common.dispose.IDisposable;
import org.osgi.service.component.ComponentInstance;

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class OSGiUI extends UI implements SessionDestroyListener,
		IDisposable.Listener {

	private ComponentInstance instance;

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
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}
}
