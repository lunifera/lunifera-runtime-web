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
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinListProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.Util;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.AbstractSelect;

/**
 */
public class ContainerItemSetContentProperty extends AbstractVaadinListProperty {

	private Class<?> collectionType;

	public ContainerItemSetContentProperty(Class<?> collectionType) {
		this.collectionType = collectionType;
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new ContainerItemSetChangeListener(this, listener);
	}

	public Object getElementType() {
		return collectionType;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List<?> doGetList(Object source) {

		Container ds = Util.getContainer(source);
		Collection<?> result = ds.getItemIds();
		return (List<?>) ((result instanceof List) ? result : new ArrayList(
				result));
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void doSetList(final Object source, List list, ListDiff diff) {
		final Container ds = Util.getContainer(source);
		// set changes on datasource. It will notify listener - BUT ensure
		// that selection is unset. Vaadin does not!
		diff.accept(new ListDiffVisitor() {
			@Override
			public void handleRemove(int index, Object element) {
				//
				if (source instanceof AbstractSelect) {
					AbstractSelect select = (AbstractSelect) source;
					select.unselect(element);
				}
				ds.removeItem(element);
			}

			@Override
			public void handleAdd(int index, Object element) {
				if (ds instanceof Container.Indexed) {
					Container.Indexed indexedDs = (Indexed) ds;
					indexedDs.addItemAt(index, element);
				} else {
					ds.addItem(element);
				}
			}
		});
	}
}