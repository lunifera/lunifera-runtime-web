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

import com.vaadin.data.Container;

/**
 */
@SuppressWarnings("serial")
public class ContainerPropertySetChangeListener extends NativePropertyListener
		implements Container.PropertySetChangeListener {

	public ContainerPropertySetChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		Container.PropertySetChangeNotifier notifier = (Container.PropertySetChangeNotifier) source;
		notifier.addPropertySetChangeListener(this);
	}

	protected void doRemoveFrom(Object source) {
		Container.PropertySetChangeNotifier notifier = (Container.PropertySetChangeNotifier) source;
		notifier.removePropertySetChangeListener(this);
	}

	@Override
	public void containerPropertySetChange(
			Container.PropertySetChangeEvent event) {
		fireChange(event.getContainer().getContainerPropertyIds(), null);
	}

}