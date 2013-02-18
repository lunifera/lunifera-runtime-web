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

import java.util.Dictionary;
import java.util.Hashtable;

import org.lunifera.runtime.web.vaadin.osgi.common.OSGiUI;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class OSGiUIProvider extends UIProvider {

	private final ComponentFactory factory;
	private final Class<? extends UI> uiClass;
	private final String vaadinApplication;

	public OSGiUIProvider(ComponentFactory factory,
			Class<? extends UI> uiClass, String vaadinApplication) {
		super();
		this.factory = factory;
		this.uiClass = uiClass;
		this.vaadinApplication = vaadinApplication;
	}

	/**
	 * Returns the vaadin application this UI provider belongs to. If
	 * <code>null</code> then this UI provider can be used for each vaadin
	 * application.
	 * 
	 * @return the vaadinApplication
	 */
	protected String getVaadinApplication() {
		return vaadinApplication;
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return uiClass;
	}

	@Override
	public UI createInstance(UICreateEvent event) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		ComponentInstance instance = factory.newInstance(properties);
		OSGiUI ui = (OSGiUI) instance.getInstance();
		ui.setComponentInstance(instance);

		return ui;
	}

	// /**
	// * Initializes the web context.
	// */
	// protected void initContext() {
	// if (registry != null) {
	// Principal user = VaadinService.getCurrentRequest()
	// .getUserPrincipal();
	// Hashtable<String, Object> properties = new Hashtable<String, Object>();
	// webContext = (IVaadinWebContext) registry.createContext(
	// user != null ? user.getName() : null, properties);
	// webContext.addDisposeListener(this);
	//
	// ((VaadinWebContext) webContext).setUi(this);
	// }
	// }
}
