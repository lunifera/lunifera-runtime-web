/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.IWidgetValueProperty (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.databinding;

import org.eclipse.core.databinding.property.list.IListProperty;

/**
 * {@link IListProperty} for observing an vaadin Component
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IModelListProperty extends IListProperty {

	/**
	 * Returns an observable list observing this list property on the given
	 * property source.
	 * 
	 * @param source
	 *            the property source
	 * @return an observable list observing this value property on the given
	 *         property source
	 */
	public IVaadinModelObservableList observe(Object source);

}
