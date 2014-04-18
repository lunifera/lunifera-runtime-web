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
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.list.SimpleListProperty;
import org.lunifera.runtime.web.vaadin.databinding.IModelListProperty;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinModelObservableList;

/**
 */
public abstract class AbstractModelListProperty extends SimpleListProperty
		implements IModelListProperty {

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractModelListProperty() {

	}

	public IVaadinModelObservableList observe(Object source) {
		return (IVaadinModelObservableList) wrapObservable(
				super.observe(source), source);
	}

	public IObservableList observe(Realm realm, Object source) {
		return super.observe(realm, source);
	}

	protected IVaadinModelObservableList wrapObservable(
			IObservableList observable, Object source) {
		return new VaadinObservableModelListDecorator(observable, source);
	}

}