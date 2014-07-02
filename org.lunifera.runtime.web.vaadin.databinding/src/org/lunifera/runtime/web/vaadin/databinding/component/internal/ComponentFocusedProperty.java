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

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.Field;

public class ComponentFocusedProperty extends AbstractVaadinValueProperty {

	public String toString() {
		return "ComponentFocusedProperty"; //$NON-NLS-1$
	}

	public ComponentFocusedProperty() {
		super(FocusEvent.class);
	}

	public Object getValueType() {
		return Boolean.class;
	}

	protected Object doGetValue(Object source) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected void doSetValue(Object source, Object value) {
		Boolean booleanValue = (Boolean) value;
		if (booleanValue) {
			Field field = (Field) source;
			field.focus();
		}
	}

}
