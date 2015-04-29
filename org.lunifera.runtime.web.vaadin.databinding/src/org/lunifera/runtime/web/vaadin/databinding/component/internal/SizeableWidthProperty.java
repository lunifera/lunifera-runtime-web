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

import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;

/**
 */
public class SizeableWidthProperty extends AbstractVaadinValueProperty {

	public SizeableWidthProperty() {
		super();
	}

	public Object getValueType() {
		return String.class;
	}

	protected Object doGetValue(Object source) {
		Sizeable component = (Sizeable) source;

		Unit widthUnit = component.getWidthUnits();
		if (widthUnit == null) {
			widthUnit = Unit.PIXELS;
		}

		return String.format("%s%s", Float.toString(component.getWidth()),
				widthUnit.getSymbol());
	}

	protected void doSetValue(Object source, Object value) {
		Sizeable component = (Sizeable) source;
		component.setWidth((String) value);
	}
}