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

import com.vaadin.data.Container;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.Viewer;
import com.vaadin.ui.AbstractSelect;

/**
 */
public class MultiSelectionProperty extends AbstractVaadinListProperty {

	private Class<?> collectionType;

	public MultiSelectionProperty(Class<?> collectionType) {
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

	@Override
	protected List<?> doGetList(Object source) {
		AbstractSelect select = (AbstractSelect) source;
		Collection<?> values = (Collection<?>) select.getValue();
		return (List<?>) ((values instanceof List) ? values
				: new ArrayList<Object>(values));
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void doSetList(final Object source, List list, ListDiff diff) {
		AbstractSelect select = (AbstractSelect) source;
		Collection<?> values = (Collection<?>) select.getValue();
		
		List<?> newValues = new ArrayList<Object>(values);
		diff.applyTo(newValues);
		select.setValue(newValues);
	}
}