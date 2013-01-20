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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.eclipse.core.databinding.property.value.DelegatingValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.IComponentValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinComponentObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Component;

abstract class ComponentDelegatingValueProperty extends DelegatingValueProperty
		implements IComponentValueProperty {
	RuntimeException notSupported(Object source) {
		return new IllegalArgumentException(
				"Component [" + source.getClass().getName() + "] is not supported."); //$NON-NLS-1$//$NON-NLS-2$
	}

	public ComponentDelegatingValueProperty() {
	}

	public ComponentDelegatingValueProperty(Object valueType) {
		super(valueType);
	}

	public IVaadinComponentObservableValue observe(Component widget) {
		return (IVaadinComponentObservableValue) observe(
				VaadinObservables.getRealm(widget.getUI()), widget);
	}

	public IVaadinComponentObservableValue observe(Object source) {
		return (IVaadinComponentObservableValue) super.observe(source);
	}

	public IVaadinComponentObservableValue observeDelayed(int delay, Component widget) {
		// return VaadinObservables.observeDelayedValue(delay, observe(widget));
		throw new IllegalStateException("Delayed not allowed");
	}

}