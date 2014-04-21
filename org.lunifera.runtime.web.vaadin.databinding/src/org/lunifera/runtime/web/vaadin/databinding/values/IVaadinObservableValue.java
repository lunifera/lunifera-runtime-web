/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.ISWTObservableList (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding.values;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinObservable;

/**
 * {@link IObservableValue} observing a vaadin component.
 */
public interface IVaadinObservableValue extends IVaadinObservable,
		IObservableValue {

	/**
	 * Returns the model element that is observed.
	 * 
	 * @return the model source object
	 */
	public Object getSource();

}
