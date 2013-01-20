/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.jface.internal.databinding.swt.SWTObservableValueDecorator (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.eclipse.core.databinding.observable.value.DecoratingObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinComponentObservableValue;

import com.vaadin.ui.Component;

/**
 */
@SuppressWarnings("serial")
public class VaadinObservableComponentValueDecorator extends DecoratingObservableValue
		implements IVaadinComponentObservableValue, Component.Listener {
	private Component widget;

	/**
	 * @param decorated
	 * @param widget
	 */
	public VaadinObservableComponentValueDecorator(IObservableValue decorated,
			Component widget) {
		super(decorated, true);
		this.widget = widget;
		// ComponentListenerUtil.asyncAddListener(widget, Vaadin.Dispose, this);
	}

	// public void handleEvent(Event event) {
	// if (event.type == Vaadin.Dispose)
	// dispose();
	// }

	public Component getComponent() {
		return widget;
	}

	public synchronized void dispose() {
		if (widget != null) {
			// ComponentListenerUtil.asyncRemoveListener(widget, Vaadin.Dispose,
			// this);
			widget = null;
		}
		super.dispose();
	}

	@Override
	public void componentEvent(com.vaadin.ui.Component.Event event) {
		// TODO Auto-generated method stub

	}
}
