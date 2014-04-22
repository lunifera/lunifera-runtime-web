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
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinListProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.Util;

import com.vaadin.data.Property;

/**
 */
public class MultiSelectionListProperty extends AbstractVaadinListProperty {

	private Class<?> collectionType;

	public MultiSelectionListProperty(Class<?> collectionType) {
		this.collectionType = collectionType;
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new MultiSelectionListChangeListener(this, listener);
	}

	public Object getElementType() {
		return collectionType;
	}

	@Override
	protected List<?> doGetList(Object source) {
		Property<Object> property = Util.getProperty(source);
		Collection<?> values = (Collection<?>) property.getValue();
		return (List<?>) ((values instanceof List) ? values
				: values != null ? new ArrayList<Object>(values)
						: new ArrayList<Object>());
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void doSetList(final Object source, List list, ListDiff diff) {
		Property<Object> property = Util.getProperty(source);
		Collection<?> values = (Collection<?>) property.getValue();
		// convert values to list to apply listDiff
		List<?> newValues = values != null ? new ArrayList<Object>(values)
				: new ArrayList<Object>();
		diff.applyTo(newValues);
		// set the hashSet
		property.setValue(new HashSet<Object>(newValues));
	}
}