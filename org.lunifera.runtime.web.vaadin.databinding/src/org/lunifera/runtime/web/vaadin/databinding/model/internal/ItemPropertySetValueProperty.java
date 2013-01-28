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

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;

import com.vaadin.data.Item;

/**
 */
public class ItemPropertySetValueProperty extends AbstractModelProperty {

	public ItemPropertySetValueProperty() {

	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new ItemPropertySetChangeListener(this, listener);
	}

	@Override
	public Object getValueType() {
		return Collection.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		Item item = (Item) source;
		return new ArrayList<Object>(item.getItemPropertyIds());
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		throw new UnsupportedOperationException();
	}

}