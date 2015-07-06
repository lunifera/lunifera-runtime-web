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
import org.eclipse.core.databinding.observable.value.DecoratingObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 */
public class VaadinObservableValueDecorator extends DecoratingObservableValue
		implements IVaadinObservableValue {

	/**
	 * @param decorated
	 * @param widget
	 */
	public VaadinObservableValueDecorator(IObservableValue decorated) {
		super(decorated, true);
	}

	public Object getObserved() {
		IObservable decorated = getDecorated();
		if (decorated instanceof IObserving) {
			return ((IObserving) decorated).getObserved();
		}
		return null;
	}

	public Object getSource() {
		return getObserved();
	}

}
