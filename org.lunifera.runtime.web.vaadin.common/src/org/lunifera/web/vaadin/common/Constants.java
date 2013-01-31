/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.web.vaadin.common;

public class Constants {
	
	/**
	 * The OSGi property for specifying the widgetset.
	 */
	public static final String PROP_WIDGETSET = "lunifera.web.vaadin.widgetset";

	/**
	 * The OSGi property value that should be used as alias to register the servlet.
	 */
	public static final String PROP_WEBAPP__ALIAS = "lunifera.web.vaadin.alias";
	
	/**
	 * The OSGi property for specifying the name of a component.
	 */
	public static final String PROP_COMPONENT = "lunifera.web.vaadin.component";
	
	/**
	 * OSGi property component.factory for the vaadin UI (tab sheet). The
	 * vaadin.ui.class name is part of the factory name and putted after the /.
	 * The class name is required for lazy loading issues.
	 * <p>
	 * Example:
	 * 
	 * factory=
	 * "org.lunifera.web.vaadin.UI/org.lunifera.web.vaadin.example.Vaadin7DemoUI"
	 */
	public static final String OSGI_COMP_FACTORY__VAADIN_UI = "org.lunifera.web.vaadin.UI";

	/**
	 * The prefix of the factory component name before the UI class name starts. <br>
	 * UI-Class name: org.lunifera.web.vaadin.example.Vaadin7DemoUI<br>
	 * Factory name: org.lunifera.web.vaadin.UI/org.lunifera.web.vaadin.example.
	 * Vaadin7DemoUI
	 */
	public static final String PREFIX__UI_CLASS = OSGI_COMP_FACTORY__VAADIN_UI
			+ "/";
	
}
