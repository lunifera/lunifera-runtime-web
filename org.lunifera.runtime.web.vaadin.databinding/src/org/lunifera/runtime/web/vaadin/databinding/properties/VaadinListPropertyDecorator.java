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
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.core.databinding.property.list.ListProperty;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableList;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableListDecorator;

public class VaadinListPropertyDecorator extends ListProperty implements
		IVaadinListProperty {
	private final IListProperty delegate;

	/**
	 * @param delegate
	 * @param eStructuralFeature
	 */
	public VaadinListPropertyDecorator(IListProperty delegate) {
		this.delegate = delegate;
	}

	public Object getElementType() {
		return delegate.getElementType();
	}

	@Override
	public IVaadinObservableList observe(Object source) {
		return new VaadinObservableListDecorator(delegate.observe(source));
	}

	public IObservableList observe(Realm realm, Object source) {
		return new VaadinObservableListDecorator(
				delegate.observe(realm, source));
	}

	@Override
	public IObservableFactory listFactory() {
		return delegate.listFactory();
	}

	@Override
	public IObservableFactory listFactory(Realm realm) {
		return delegate.listFactory(realm);
	}

	@Override
	public IObservableList observeDetail(IObservableValue master) {
		return new VaadinObservableListDecorator(delegate.observeDetail(master));
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
