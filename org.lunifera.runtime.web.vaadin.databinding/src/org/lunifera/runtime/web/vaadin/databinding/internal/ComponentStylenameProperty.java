/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas of org.eclipse.jface.databinding.swt (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding.internal;

import org.lunifera.runtime.web.vaadin.databinding.AbstractComponentValueProperty;

import com.vaadin.ui.Component;

/**
 */
public class ComponentStylenameProperty extends
		AbstractComponentValueProperty {
	public String toString() {
		return "ComponentStylenameProperty"; //$NON-NLS-1$
	}

	public ComponentStylenameProperty() {
		super();
	}

	public Object getValueType() {
		return String.class;
	}

	protected Object doGetValue(Object source) {
		Component component = (Component) source;
		return component.getStyleName();
	}

	protected void doSetValue(Object source, Object value) {
		Component component = (Component) source;
		component.setStyleName((String) value);
	}
}