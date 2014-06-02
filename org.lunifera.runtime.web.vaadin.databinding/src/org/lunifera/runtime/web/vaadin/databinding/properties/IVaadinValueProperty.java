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
package org.lunifera.runtime.web.vaadin.databinding.properties;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableValue;

/**
 * {@link IValueProperty} for observing an vaadin Component
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IVaadinValueProperty extends IValueProperty {

	/**
	 * Returns an observable value observing this value property on the given
	 * property source.
	 * 
	 * @param source
	 *            the property source
	 * @return an observable value observing this value property on the given
	 *         property source
	 */
	IVaadinObservableValue observe(Object source);

	/**
	 * Is used to observe the com.vaadin.data.Property#value attribute.
	 * 
	 * @param realm
	 * @param source
	 * @return
	 */
	IVaadinObservableValue observeVaadinProperty(Object source);

	/**
	 * Returns an {@link IVaadinComponentObservableValue} observing this value
	 * property on the given Component, which delays notification of value
	 * changes until at least <code>delay</code> milliseconds have elapsed since
	 * that last change event, or until a FocusOut event is received from the
	 * Component (whichever happens first).
	 * <p>
	 * This method is equivalent to
	 * <code>vaadinObservables.observeDelayedValue(delay, observe(Component))</code>.
	 * <p>
	 * Attention: Currently without functionality
	 * 
	 * @param delay
	 *            the delay in milliseconds.
	 * @param Component
	 *            the source Component
	 * @return an observable value observing this value property on the given
	 *         Component, and which delays change notifications for
	 *         <code>delay</code> milliseconds.
	 */
	public IVaadinObservableValue observeDelayed(int delay, Object component);

}
