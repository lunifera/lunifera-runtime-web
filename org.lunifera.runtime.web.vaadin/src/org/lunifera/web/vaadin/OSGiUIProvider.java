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
package org.lunifera.web.vaadin;

import java.util.Dictionary;

import org.lunifera.web.vaadin.common.OSGiUI;
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

	@SuppressWarnings("rawtypes")
	public OSGiUIProvider(ComponentFactory factory,
			Class<? extends UI> uiClass, Dictionary properties) {
		super();
		this.factory = factory;
		this.uiClass = uiClass;
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return uiClass;
	}

	@Override
	public UI createInstance(UICreateEvent event) {
		ComponentInstance instance = factory.newInstance(null);
		OSGiUI ui = (OSGiUI) instance.getInstance();
		ui.setComponentInstance(instance);
		return ui;
	}
}
