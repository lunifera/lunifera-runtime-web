/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas from Eclipse Databinding.
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
import com.vaadin.ui.AbstractField;

/**
 */
public class PropertyValueProperty extends AbstractVaadinValueProperty {

	public PropertyValueProperty() {

	}

	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new PropertyValueChangeListener(this, listener);
	}

	@Override
	public Object getValueType() {
		return Object.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		Property<?> property = Util.getProperty(source);
		return property.getValue();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doSetValue(Object source, Object value) {
		if (source instanceof AbstractField) {
			AbstractField field = (AbstractField) source;
			if (!field.isValid()) {
				// workaround - if value is invalid, then property#setValue will
				// not change the field value. So we need to reset the value
				// internally to the datasource value
				field.discard();
			}
		}

		Property<Object> property = Util.getProperty(source);
		property.setValue(value);
	}

}