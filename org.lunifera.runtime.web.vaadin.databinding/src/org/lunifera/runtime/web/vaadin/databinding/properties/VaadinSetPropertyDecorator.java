/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas from Eclipse Databinding.
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.vaadin.databinding.properties;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.set.ISetProperty;
import org.eclipse.core.databinding.property.set.SetProperty;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableSet;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableSetDecorator;

public class VaadinSetPropertyDecorator extends SetProperty implements
		IVaadinSetProperty {
	private final ISetProperty delegate;

	/**
	 * @param delegate
	 * @param eStructuralFeature
	 */
	public VaadinSetPropertyDecorator(ISetProperty delegate) {
		this.delegate = delegate;
	}

	public Object getElementType() {
		return delegate.getElementType();
	}

	@Override
	public IVaadinObservableSet observe(Object source) {
		return new VaadinObservableSetDecorator(delegate.observe(source));
	}

	public IObservableSet observe(Realm realm, Object source) {
		return new VaadinObservableSetDecorator(delegate.observe(realm, source));
	}

	@Override
	public IObservableFactory setFactory() {
		return delegate.setFactory();
	}

	@Override
	public IObservableFactory setFactory(Realm realm) {
		return delegate.setFactory(realm);
	}

	@Override
	public IObservableSet observeDetail(IObservableValue master) {
		return new VaadinObservableSetDecorator(delegate.observeDetail(master));
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
