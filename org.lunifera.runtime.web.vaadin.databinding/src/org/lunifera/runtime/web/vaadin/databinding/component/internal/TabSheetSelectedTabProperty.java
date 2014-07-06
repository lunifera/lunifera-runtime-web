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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;

/**
 */
public class TabSheetSelectedTabProperty extends AbstractVaadinValueProperty {

	public TabSheetSelectedTabProperty() {
		super(SelectedTabChangeEvent.class);
	}

	@Override
	public Object getValueType() {
		return Component.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		TabSheet component = (TabSheet) source;
		return component.getSelectedTab();
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		TabSheet component = (TabSheet) source;
		component.setSelectedTab((Component) value);
	}
}