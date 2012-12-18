/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.web.vaadin;

public class Constants {

	/**
	 * OSGi property component.factory for the vaadin session (vaadin
	 * application in vaadin 7)
	 */
	public static final String OSGI_COMP_FACTORY__VAADIN_SESSION = "org.lunifera.web.vaadin.Session";

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
	public static final String PREFIX__UI_CLASS = OSGI_COMP_FACTORY__VAADIN_UI + "/";

}
