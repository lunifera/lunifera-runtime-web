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

package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.PropertyInfo;

import com.vaadin.data.Item;

/**
 */
public class ItemPropertySetInfoValueProperty extends
		AbstractVaadinValueProperty {

	public ItemPropertySetInfoValueProperty() {
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new ItemPropertySetInfoChangeListener(this, listener);
	}

	@Override
	public Object getValueType() {
		return Collection.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		Item item = (Item) source;

		List<PropertyInfo> infos = new ArrayList<PropertyInfo>();
		for (Object id : item.getItemPropertyIds()) {
			infos.add(new PropertyInfo(id, item.getItemProperty(id)));
		}

		return infos;
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		Item item = (Item) source;
		@SuppressWarnings("unchecked")
		Collection<PropertyInfo> props = (Collection<PropertyInfo>) value;

		for (Object id : item.getItemPropertyIds().toArray()) {
			item.removeItemProperty(id);
		}

		for (PropertyInfo info : props) {
			item.addItemProperty(info.getId(), info.getProperty());
		}

	}

}