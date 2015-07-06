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

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinObservable;

/**
 * {@link IObservableList} observing a vaadin component.
 */
public interface IVaadinObservableSet extends IVaadinObservable, IObservableSet {

	/**
	 * Returns the model element that is observed.
	 * 
	 * @return the model source object
	 */
	public Object getSource();

}
