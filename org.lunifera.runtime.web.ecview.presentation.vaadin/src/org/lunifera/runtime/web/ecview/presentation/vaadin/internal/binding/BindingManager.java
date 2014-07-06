/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFUpdateListStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.model.core.CoreModelPackage;
import org.eclipse.emf.ecp.ecview.common.model.core.YEditable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEnable;
import org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable;
import org.eclipse.emf.ecp.ecview.databinding.emf.common.ECViewDatabindingContext;
import org.eclipse.emf.ecp.ecview.databinding.emf.common.ECViewUpdateValueStrategy;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableValue;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Is used to bind values.
 */
public class BindingManager extends
		org.eclipse.emf.ecp.ecview.common.binding.AbstractBindingManager
		implements IBindingManager {

	public BindingManager(IViewContext viewContext, Realm validationRealm) {
		super(validationRealm);
	}

	/**
	 * Casts element to EObject.
	 * 
	 * @param element
	 * @return
	 */
	protected EObject castEObject(Object element) {
		return (EObject) element;
	}

	@Override
	protected DataBindingContext createDatabindingContext(Realm validationRealm) {
		return new ECViewDatabindingContext(validationRealm);
	}

	@Override
	public Binding bindValue(IObservableValue target, IObservableValue model) {
		return bindValue(
				target,
				model,
				new ECViewUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE),
				new ECViewUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
	}

	@Override
	public Binding bindList(IObservableList target, IObservableList model) {
		return getDatabindingContext().bindList(target, model,
				new EMFUpdateListStrategy(UpdateListStrategy.POLICY_UPDATE),
				new EMFUpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));
	}

	@Override
	public Binding bindVisible(YVisibleable yVisibleAble,
			AbstractComponent abstractComponent) {
		IVaadinObservableValue uiObservable = VaadinObservables
				.observeVisible(abstractComponent);
		IObservableValue modelObservable = EMFObservables.observeValue(
				castEObject(yVisibleAble),
				CoreModelPackage.eINSTANCE.getYVisibleable_Visible());
		return getDatabindingContext().bindValue(uiObservable, modelObservable);
	}

	@Override
	public Binding bindEnabled(YEnable yEnable,
			AbstractComponent abstractComponent) {
		IVaadinObservableValue uiObservable = VaadinObservables
				.observeEnabled(abstractComponent);
		IObservableValue modelObservable = EMFObservables.observeValue(
				castEObject(yEnable),
				CoreModelPackage.eINSTANCE.getYEnable_Enabled());
		return getDatabindingContext().bindValue(uiObservable, modelObservable);
	}

	@Override
	public Binding bindReadonly(YEditable yEditable,
			Property.ReadOnlyStatusChangeNotifier field) {
		IVaadinObservableValue uiObservable = VaadinObservables
				.observeReadonly(field);
		IObservableValue modelObservable = EMFObservables.observeValue(
				castEObject(yEditable),
				CoreModelPackage.eINSTANCE.getYEditable_Editable());
		return getDatabindingContext()
				.bindValue(
						uiObservable,
						modelObservable,
						new ECViewUpdateValueStrategy(
								UpdateValueStrategy.POLICY_UPDATE)
								.setConverter(new BoolNegator()),
						new ECViewUpdateValueStrategy(
								UpdateValueStrategy.POLICY_UPDATE)
								.setConverter(new BoolNegator()));
	}

	@Override
	public Binding bindReadonlyOneway(YEditable yEditable, Component field) {
		IObservableValue uiObservable = BeansObservables.observeValue(field,
				"readOnly");
		IObservableValue modelObservable = EMFObservables.observeValue(
				castEObject(yEditable),
				CoreModelPackage.eINSTANCE.getYEditable_Editable());
		return getDatabindingContext()
				.bindValue(
						uiObservable,
						modelObservable,
						new ECViewUpdateValueStrategy(
								UpdateValueStrategy.POLICY_NEVER)
								.setConverter(new BoolNegator()),
						new ECViewUpdateValueStrategy(
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
