/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.vaadin.databinding.properties;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.ValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableList;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableListDecorator;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableValueDecorator;

public class VaadinValuePropertyDecorator extends ValueProperty implements
		IVaadinValueProperty {
	private final IVaadinValueProperty delegate;

	/**
	 * @param delegate
	 * @param eStructuralFeature
	 */
	public VaadinValuePropertyDecorator(IVaadinValueProperty delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object getValueType() {
		return delegate.getValueType();
	}

	@Override
	public IVaadinObservableValue observe(Object source) {
		return new VaadinObservableValueDecorator(delegate.observe(source));
	}

	@Override
	public IVaadinObservableValue observeVaadinProperty(Object source) {
		return new VaadinObservableValueDecorator(
				delegate.observeVaadinProperty(source));
	}

	public IObservableValue observe(Realm realm, Object source) {
		return new VaadinObservableValueDecorator(delegate.observe(realm,
				source));
	}

	@Override
	public IVaadinObservableValue observeDelayed(int delay, Object component) {
		return delegate.observeDelayed(delay, component);
	}

	@Override
	public IObservableFactory valueFactory() {
		return delegate.valueFactory();
	}

	@Override
	public IObservableFactory valueFactory(Realm realm) {
		return delegate.valueFactory(realm);
	}

	@Override
	public IObservableValue observeDetail(IObservableValue master) {
		return new VaadinObservableValueDecorator(
				delegate.observeDetail(master));
	}

	@SuppressWarnings("all")
	public IVaadinObservableList observeDetail(IObservableList master) {
		return new VaadinObservableListDecorator(delegate.observeDetail(master));
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
