/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Information:
 * 		Based on original sources of 
 *				- com.c4biz.osgiutils.vaadin.equinox.shiro.SessionWrapper from Cristiano Gaviao
 *
 * Contributors:
 *    Florian Pirchner - migrated to vaadin 7 and copied into org.lunifera namespace
 *    
 *******************************************************************************/
package org.lunifera.web.vaadin.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.osgi.service.component.ComponentInstance;

import com.vaadin.server.VaadinSession;

/**
 * Track the component instance and session and hold an Application object.
 * 
 * @author cvgaviao
 */
public class SessionWrapper implements HttpSessionBindingListener {

	final ComponentInstance instance;
	final HttpSession httpSession;
	private VaadinSession vaadinSession;

	public VaadinSession getApp() {
		return vaadinSession;
	}

	public SessionWrapper(ComponentInstance instance, HttpSession httpSession) {
		this.instance = instance;
		this.httpSession = httpSession;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent param) {
		vaadinSession = (VaadinSession) instance.getInstance();
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		if (vaadinSession != null) {
			// app.close();
			// app = null;
		}
		if (instance != null) {
			// instance.dispose();
		}
	}
}
