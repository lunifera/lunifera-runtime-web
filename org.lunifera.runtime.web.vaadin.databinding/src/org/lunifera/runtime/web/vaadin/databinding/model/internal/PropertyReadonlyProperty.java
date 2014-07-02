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

import com.vaadin.ui.AbstractField;

/**
 */
public class PropertyReadonlyProperty extends AbstractVaadinValueProperty {

	public PropertyReadonlyProperty() {
		super();
	}

	public Object getValueType() {
		return Boolean.class;
	}

	protected Object doGetValue(Object source) {
		AbstractField<?> component = (AbstractField<?>) source;
		return component.isReadOnly();
	}

	protected void doSetValue(Object source, Object value) {
		AbstractField<?> component = (AbstractField<?>) source;
		component.setReadOnly((Boolean) value);
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new PropertyReadonlyChangeListener(this, listener);
	}
}