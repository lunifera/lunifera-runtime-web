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

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.IModelValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.VaadinObservableValueDecorator;

import com.vaadin.ui.Component;

/**
 */
public abstract class AbstractModelProperty extends SimpleValueProperty
		implements IModelValueProperty {

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractModelProperty() {

	}

	public IVaadinObservableValue observe(Object source) {
		return (IVaadinObservableValue) super.observe(source);
	}

	public IObservableValue observe(Realm realm, Object source) {
		return wrapObservable(super.observe(realm, source), (Component) source);
	}

	protected IVaadinObservableValue wrapObservable(
			IObservableValue observable, Component widget) {
		return new VaadinObservableValueDecorator(observable, widget);
	}

}