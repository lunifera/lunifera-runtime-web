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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.lunifera.runtime.web.vaadin.databinding.AbstractComponentValueProperty;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

/**
 */
public class TabSheetSelectedTabProperty extends AbstractComponentValueProperty {

	public TabSheetSelectedTabProperty() {
		super();
	}

	public Object getValueType() {
		return Component.class;
	}

	protected Object doGetValue(Object source) {
		TabSheet component = (TabSheet) source;
		return component.getSelectedTab();
	}

	protected void doSetValue(Object source, Object value) {
		TabSheet component = (TabSheet) source;
		component.setSelectedTab((Component) value);
	}
}