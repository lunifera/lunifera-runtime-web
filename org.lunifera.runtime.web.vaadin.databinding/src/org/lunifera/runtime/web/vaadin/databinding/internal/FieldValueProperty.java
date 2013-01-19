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

package org.lunifera.runtime.web.vaadin.databinding.internal;

import org.lunifera.runtime.web.vaadin.databinding.AbstractComponentValueProperty;

import com.vaadin.data.Property;
import com.vaadin.data.Property.Viewer;
import com.vaadin.ui.Field;

/**
 */
public class FieldValueProperty extends AbstractComponentValueProperty {
	public String toString() {
		return "FieldValueProperty"; //$NON-NLS-1$
	}

	public FieldValueProperty() {
		super(Field.ValueChangeEvent.class);
	}

	public Object getValueType() {
		return Object.class;
	}

	protected Object doGetValue(Object source) {
		Property.Viewer viewer = (Viewer) source;
		Property<?> property = (Property<?>) (viewer.getPropertyDataSource() != null ? viewer
				.getPropertyDataSource() : viewer);
		return property.getValue();
	}

	@SuppressWarnings("unchecked")
	protected void doSetValue(Object source, Object value) {
		Property.Viewer viewer = (Viewer) source;
		@SuppressWarnings("rawtypes")
		Property property = (Property) (viewer.getPropertyDataSource() != null ? viewer
				.getPropertyDataSource() : viewer);
		property.setValue(value);
	}
}