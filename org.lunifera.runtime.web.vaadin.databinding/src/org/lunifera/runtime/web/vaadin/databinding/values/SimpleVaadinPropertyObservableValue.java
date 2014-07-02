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
package org.lunifera.runtime.web.vaadin.databinding.values;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;

import com.vaadin.data.Property;
import com.vaadin.data.Property.Viewer;
import com.vaadin.ui.AbstractSelect;

@SuppressWarnings("restriction")
public class SimpleVaadinPropertyObservableValue extends
		SimplePropertyObservableValue {

	public SimpleVaadinPropertyObservableValue(Realm realm, Object source,
			SimpleValueProperty property) {
		super(realm, source, property);
	}

	@Override
	public Object getValueType() {
		// delegate call to getValueType to vaadin property
		com.vaadin.data.Property<?> property = (com.vaadin.data.Property<?>) getObserved();

		if (property instanceof AbstractSelect) {
			AbstractSelect select = (AbstractSelect) property;
			if (select.isMultiSelect()) {
				return select.getType();
			}
		}

		if (property instanceof Property.Viewer) {
			Property.Viewer viewer = (Viewer) property;
			com.vaadin.data.Property<?> internalProperty = viewer
					.getPropertyDataSource();
			if (internalProperty != null) {
				return internalProperty.getType();
			}
		}

		return property.getType();
	}

}
