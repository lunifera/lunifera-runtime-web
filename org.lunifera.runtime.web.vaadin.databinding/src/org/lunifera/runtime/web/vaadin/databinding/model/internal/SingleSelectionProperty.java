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

package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.Util;

import com.vaadin.data.Property;

/**
 * A property that is responsible for single selections.
 */
public class SingleSelectionProperty extends AbstractVaadinValueProperty {

	private final Class<?> type;

	public SingleSelectionProperty(Class<?> type) {
		this.type = type;
	}

	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new PropertyValueChangeListener(this, listener);
	}

	@Override
	public Object getValueType() {
		return type;
	}

	@Override
	protected Object doGetValue(Object source) {
		Property<?> property = Util.getProperty(source);
		return property.getValue();
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		Property<Object> property = Util.getProperty(source);
		property.setValue(value);
	}

}