/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.common;

public class Constants {

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
	public static final String OSGI_COMP_FACTORY_VAADIN_UI = "org.lunifera.web.vaadin.UI";

	/**
	 * The prefix of the factory component name before the UI class name starts. <br>
	 * UI-Class name: org.lunifera.web.vaadin.example.Vaadin7DemoUI<br>
	 * Factory name: org.lunifera.web.vaadin.UI/org.lunifera.web.vaadin.example.
	 * Vaadin7DemoUI
	 */
	public static final String PREFIX_UI_CLASS = OSGI_COMP_FACTORY_VAADIN_UI
			+ "/";

}
