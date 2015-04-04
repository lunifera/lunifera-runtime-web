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
import java.util.List;

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.data.Item;
import com.vaadin.data.Item.PropertySetChangeEvent;

/**
 */
@SuppressWarnings("serial")
public class ItemPropertySetChangeListener extends NativePropertyListener
		implements Item.PropertySetChangeListener {

	public ItemPropertySetChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	@Override
	protected void doAddTo(Object source) {
		Item.PropertySetChangeNotifier notifier = (Item.PropertySetChangeNotifier) source;
		notifier.addPropertySetChangeListener(this);
	}

	@Override
	protected void doRemoveFrom(Object source) {
		Item.PropertySetChangeNotifier notifier = (Item.PropertySetChangeNotifier) source;
		notifier.removePropertySetChangeListener(this);
	}

	@Override
	public void itemPropertySetChange(PropertySetChangeEvent event) {
		List<Object> propertyList = new ArrayList<Object>(event.getItem()
				.getItemPropertyIds());
		fireChange(propertyList, null);
	}

}