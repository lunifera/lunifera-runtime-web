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

	private ComponentFactory uiFactory;
	private final Class<? extends UI> uiClass;
	private final String vaadinApplication;

	public OSGiUIProvider(String vaadinApplication, Class<? extends UI> uiClass) {
		super();
		this.uiClass = uiClass;
		this.vaadinApplication = vaadinApplication;
	}

	/**
	 * Sets the component factory used to create new UI instances.
	 * 
	 * @param uiFactory
	 */
	void setUIFactory(ComponentFactory uiFactory) {
		this.uiFactory = uiFactory;
	}

	/**
	 * Returns the component factory.
	 * 
	 * @return factory
	 */
	ComponentFactory getUIFactory() {
		return this.uiFactory;
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

		ComponentInstance instance = uiFactory.newInstance(properties);
		OSGiUI ui = (OSGiUI) instance.getInstance();
		ui.setComponentInstance(instance);

		return ui;
	}

}
