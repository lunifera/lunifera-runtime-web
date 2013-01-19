/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.ISWTObservable (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding;

import org.eclipse.core.databinding.observable.IObservable;

import com.vaadin.ui.Component;

/**
 * {@link IObservable} observing an vaadin Component.
 * 
 * @since 1.1
 * 
 */
public interface IVaadinObservable extends IObservable {

	/**
	 * Returns the Component of this observable
	 * 
	 * @return the Component
	 */
	public Component getComponent();

}
