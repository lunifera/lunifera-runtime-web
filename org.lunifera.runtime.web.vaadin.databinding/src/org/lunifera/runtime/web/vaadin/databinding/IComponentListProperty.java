/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.IWidgetListProperty (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding;

import org.eclipse.core.databinding.property.list.IListProperty;

import com.vaadin.ui.Component;

/**
 * {@link IListProperty} for observing an Vaadin Component
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IComponentListProperty extends IListProperty {
	/**
	 * Returns an {@link IVaadinObservableList} observing this list property on
	 * the given Component
	 * 
	 * @param component
	 *            the source Component
	 * @return an observable list observing this list property on the given
	 *         Component
	 */
	public IVaadinObservableList observe(Component component);
}
