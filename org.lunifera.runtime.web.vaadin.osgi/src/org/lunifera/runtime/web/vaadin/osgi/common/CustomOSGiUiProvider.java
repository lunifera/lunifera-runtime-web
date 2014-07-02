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

import org.lunifera.runtime.web.vaadin.osgi.webapp.OSGiUIProvider;

import com.vaadin.ui.UI;

/**
 * The base class for customer ui provider implementation.
 */
@SuppressWarnings("serial")
public abstract class CustomOSGiUiProvider extends OSGiUIProvider {

	public CustomOSGiUiProvider(String vaadinApplication,
			Class<? extends UI> uiClass) {
		super(vaadinApplication, uiClass);
	}
}
