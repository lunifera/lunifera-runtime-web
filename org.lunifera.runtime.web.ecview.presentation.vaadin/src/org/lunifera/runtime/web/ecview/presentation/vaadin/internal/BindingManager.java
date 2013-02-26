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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.core.CoreModelPackage;
import org.eclipse.emf.ecp.ecview.common.model.core.YEditable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEnable;
import org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinComponentObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.IVaadinModelObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.ui.Field;

/**
 * Is used to bind values.
 */
public class BindingManager implements IBindingManager {

	@SuppressWarnings("unused")
	private final IViewContext viewContext;
	private final Realm validationRealm;
	private DataBindingContext dbc;

	public BindingManager(IViewContext viewContext, Realm validationRealm) {
		super();
		this.viewContext = viewContext;
		this.validationRealm = validationRealm;
		dbc = new DataBindingContext();
	}

	public Realm getValidationRealm() {
		return validationRealm;
	}

	/* (non-Javadoc)
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.internal.IBindingManager#bindVisible(org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable, com.vaadin.ui.Field)
	 */
	@Override
	public void bindVisible(YVisibleable yVisibleAble, Field<?> field) {
		IVaadinComponentObservableValue uiObservable = VaadinObservables
				.observeVisible(field);
		IObservableValue modelObservable = EMFObservables.observeValue(
				yVisibleAble,
				CoreModelPackage.eINSTANCE.getYVisibleable_Visible());
		dbc.bindValue(uiObservable, modelObservable);
	}

	/* (non-Javadoc)
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.internal.IBindingManager#bindEnabled(org.eclipse.emf.ecp.ecview.common.model.core.YEnable, com.vaadin.ui.Field)
	 */
	@Override
	public void bindEnabled(YEnable yEnable, Field<?> field) {
		IVaadinComponentObservableValue uiObservable = VaadinObservables
				.observeEnabled(field);
		IObservableValue modelObservable = EMFObservables.observeValue(yEnable,
				CoreModelPackage.eINSTANCE.getYEnable_Enabled());
		dbc.bindValue(uiObservable, modelObservable);
	}

	/* (non-Javadoc)
	 * @see org.lunifera.runtime.web.ecview.presentation.vaadin.internal.IBindingManager#bindReadonly(org.eclipse.emf.ecp.ecview.common.model.core.YEditable, com.vaadin.data.Property.ReadOnlyStatusChangeNotifier)
	 */
	@Override
	public void bindReadonly(YEditable yEditable,
			Property.ReadOnlyStatusChangeNotifier field) {
		IVaadinModelObservableValue uiObservable = VaadinObservables
				.observeReadonly(field);
		IObservableValue modelObservable = EMFObservables.observeValue(
				yEditable, CoreModelPackage.eINSTANCE.getYEditable_Editable());
		dbc.bindValue(uiObservable, modelObservable, new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE)
				.setConverter(new BoolNegator()), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE)
				.setConverter(new BoolNegator()));
	}

	/**
	 * Negates booleans.
	 */
	private static class BoolNegator implements IConverter {

		@Override
		public Object getFromType() {
			return Boolean.class;
		}

		@Override
		public Object getToType() {
			return Boolean.class;
		}

		@Override
		public Object convert(Object fromObject) {
			return !((Boolean) fromObject);
		}

	}
}
