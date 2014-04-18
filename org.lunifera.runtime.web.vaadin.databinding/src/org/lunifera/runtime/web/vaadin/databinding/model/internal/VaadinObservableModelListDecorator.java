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

package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinModelObservableList;

/**
 */
public class VaadinObservableModelListDecorator extends
		DecoratingObservableList implements IVaadinModelObservableList {
	private Object source;

	/**
	 * @param decorated
	 * @param widget
	 */
	public VaadinObservableModelListDecorator(IObservableList decorated,
			Object widget) {
		super(decorated, true);
		this.source = widget;
	}

	public Object getSource() {
		return source;
	}

	public synchronized void dispose() {
		if (source != null) {
			// ComponentListenerUtil.asyncRemoveListener(widget, Vaadin.Dispose,
			// this);
			source = null;
		}
		super.dispose();
	}
}
