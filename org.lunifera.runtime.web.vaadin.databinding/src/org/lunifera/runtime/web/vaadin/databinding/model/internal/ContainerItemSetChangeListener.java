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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.Util;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeNotifier;

/**
 */
@SuppressWarnings("serial")
public class ContainerItemSetChangeListener extends NativePropertyListener
		implements Container.ItemSetChangeListener {

	private ArrayList<Object> oldItems = new ArrayList<Object>();
	private Object source;

	public ContainerItemSetChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		this.source = source;
		getNotifier(source).addItemSetChangeListener(this);

		cacheOldItems(source);
	}

	protected void cacheOldItems(Object source) {
		Container container = getContainer(source);
		oldItems = new ArrayList<Object>(container.getItemIds());
	}

	protected Container.ItemSetChangeNotifier getNotifier(Object source) {
		Container.ItemSetChangeNotifier notifierToUse = (Container.ItemSetChangeNotifier) source;

		// if the container can be used, then use it
		if (source instanceof Container.Viewer) {
			Container ds = getContainer(source);
			if (ds instanceof Container.ItemSetChangeNotifier) {
				notifierToUse = (ItemSetChangeNotifier) ds;
			}
		}
		return notifierToUse;
	}

	protected Container getContainer(Object source) {
		Container ds = Util.getContainer(source);
		return ds;
	}

	protected void doRemoveFrom(Object source) {
		getNotifier(source).removeItemSetChangeListener(this);
	}

	@Override
	public void containerItemSetChange(Container.ItemSetChangeEvent event) {
		List<Object> oldValues = oldItems;
		ListDiff diffs = Diffs.computeListDiff(oldValues, convertToList(event
				.getContainer().getItemIds()));
		cacheOldItems(source);

		fireChange(source, diffs);
	}

	@SuppressWarnings("unchecked")
	private List<Object> convertToList(Collection<?> itemIds) {
		return (List<Object>) ((itemIds instanceof List) ? itemIds
				: new ArrayList<Object>(itemIds));
	}

}