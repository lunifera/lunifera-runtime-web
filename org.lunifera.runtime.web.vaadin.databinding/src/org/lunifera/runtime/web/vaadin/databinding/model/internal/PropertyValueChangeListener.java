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

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

/**
 */
@SuppressWarnings("serial")
public class PropertyValueChangeListener extends NativePropertyListener
		implements Property.ValueChangeListener {

	public PropertyValueChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		if (source instanceof Property.ValueChangeNotifier) {
			Property.ValueChangeNotifier notifier = (Property.ValueChangeNotifier) source;
			notifier.addValueChangeListener(this);
		}
	}

	protected void doRemoveFrom(Object source) {
		if (source instanceof Property.ValueChangeNotifier) {
			Property.ValueChangeNotifier notifier = (Property.ValueChangeNotifier) source;
			notifier.removeValueChangeListener(this);
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		fireChange(event.getProperty(), null);
	}

}