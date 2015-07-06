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
package org.lunifera.runtime.web.vaadin.databinding.values;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.set.DecoratingObservableSet;
import org.eclipse.core.databinding.observable.set.IObservableSet;

public class VaadinObservableSetDecorator extends DecoratingObservableSet
		implements IVaadinObservableSet {

	/**
	 * @param decorated
	 * @param eStructuralFeature
	 */
	public VaadinObservableSetDecorator(IObservableSet decorated) {
		super(decorated, true);
	}

	public Object getObserved() {
		IObservable decorated = getDecorated();
		if (decorated instanceof IObserving) {
			return ((IObserving) decorated).getObserved();
		}
		return null;
	}

	@Override
	public Object getSource() {
		return getObserved();
	}

}
