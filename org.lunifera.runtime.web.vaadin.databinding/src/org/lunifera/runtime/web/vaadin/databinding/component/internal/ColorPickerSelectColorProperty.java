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

import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorPickerSelect;

/**
 */
public class ColorPickerSelectColorProperty extends
		AbstractVaadinValueProperty {

	public ColorPickerSelectColorProperty() {
		super(ColorChangeEvent.class);
	}

	public Object getValueType() {
		return Color.class;
	}

	protected Object doGetValue(Object source) {
		ColorPickerSelect component = (ColorPickerSelect) source;
		return component.getColor();
	}

	protected void doSetValue(Object source, Object value) {
		ColorPickerSelect component = (ColorPickerSelect) source;
		component.setColor((Color) value);
	}
}