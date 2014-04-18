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

package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;

import com.vaadin.data.Property;

/**
 */
public class PropertyValueProperty extends AbstractModelValueProperty {

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
		Property<?> property = (Property<?>) source;
		return property.getValue();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doSetValue(Object source, Object value) {
		Property property = (Property) source;
		property.setValue(value);
	}

}