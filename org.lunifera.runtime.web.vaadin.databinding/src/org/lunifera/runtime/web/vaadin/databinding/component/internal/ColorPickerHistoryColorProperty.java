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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorPickerHistory;

/**
 */
public class ColorPickerHistoryColorProperty extends
		AbstractVaadinValueProperty {

	public ColorPickerHistoryColorProperty() {
		super(ColorChangeEvent.class);
	}

	public Object getValueType() {
		return Color.class;
	}

	protected Object doGetValue(Object source) {
		ColorPickerHistory component = (ColorPickerHistory) source;
		return component.getColor();
	}

	protected void doSetValue(Object source, Object value) {
		ColorPickerHistory component = (ColorPickerHistory) source;
		component.setColor((Color) value);
	}
}