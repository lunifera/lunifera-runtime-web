/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.web.ecp.uimodel.presentation.vaadin.internal;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.core.CoreModelPackage;
import org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinComponentObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Field;

/**
 * Is used to bind values.
 */
public class BindingManager {

	@SuppressWarnings("unused")
	private final IViewContext viewContext;
	private DataBindingContext dbc = new DataBindingContext();

	public BindingManager(IViewContext viewContext) {
		super();
		this.viewContext = viewContext;
	}

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yVisibleAble
	 * @param field
	 */
	public void bindVisible(YVisibleable yVisibleAble, Field<?> field) {

		IVaadinComponentObservableValue uiObservable = VaadinObservables
				.observeVisible(field);
		IObservableValue modelObservable = EMFObservables.observeValue(
				yVisibleAble,
				CoreModelPackage.eINSTANCE.getYVisibleable_Visible());
		dbc.bindValue(uiObservable, modelObservable);
	}

}
