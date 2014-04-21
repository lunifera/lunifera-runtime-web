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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.PropertyInfo;

import com.vaadin.data.Item;
import com.vaadin.data.Item.PropertySetChangeEvent;

/**
 */
@SuppressWarnings("serial")
public class ItemPropertySetInfoChangeListener extends NativePropertyListener
		implements Item.PropertySetChangeListener {

	public ItemPropertySetInfoChangeListener(IProperty property,
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
		Item item = event.getItem();

		List<PropertyInfo> infos = new ArrayList<PropertyInfo>();
		for (Object id : item.getItemPropertyIds()) {
			infos.add(new PropertyInfo(id, item.getItemProperty(id)));
		}
		fireChange(infos, null);
	}
}