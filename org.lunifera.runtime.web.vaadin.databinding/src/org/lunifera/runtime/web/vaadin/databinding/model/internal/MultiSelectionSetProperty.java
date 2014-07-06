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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinSetProperty;
import org.lunifera.runtime.web.vaadin.databinding.properties.Util;

import com.vaadin.data.Property;

/**
 */
public class MultiSelectionSetProperty extends AbstractVaadinSetProperty {

	private Class<?> collectionType;

	public MultiSelectionSetProperty(Class<?> collectionType) {
		this.collectionType = collectionType;
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new MultiSelectionSetChangeListener(this, listener);
	}

	public Object getElementType() {
		return collectionType;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Set doGetSet(Object source) {
		Set result = (Set) Util.getProperty(source).getValue();
		return result != null ? result : new HashSet();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doSetSet(Object source, Set set, SetDiff diff) {
		Property<Object> property = Util.getProperty(source);
		property.setValue(set);
	}
}