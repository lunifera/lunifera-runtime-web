/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.disposal.AbstractDisposable;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YAction;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YField;
import org.eclipse.emf.ecp.ecview.common.model.core.util.CoreModelUtil;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * An abstract implementation of the {@link IWidgetPresentation}.
 */
public abstract class AbstractVaadinWidgetPresenter<A extends Component>
		extends AbstractDisposable implements IWidgetPresentation<A> {

	/**
	 * See {@link IConstants#CSS_CLASS__CONTROL_BASE}.
	 */
	public static final String CSS_CLASS__CONTROL_BASE = IConstants.CSS_CLASS__CONTROL_BASE;

	/**
	 * See {@link IConstants#CSS_CLASS__CONTROL}.
	 */
	public static final String CSS_CLASS__CONTROL = IConstants.CSS_CLASS__CONTROL;

	private final IEmbeddableEditpart editpart;

	public AbstractVaadinWidgetPresenter(IEmbeddableEditpart editpart) {
		this.editpart = editpart;
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return the editpart
	 */
	protected IEmbeddableEditpart getEditpart() {
		return editpart;
	}

	@Override
	public Object getModel() {
		return getEditpart().getModel();
	}

	/**
	 * Returns the view context.
	 * 
	 * @return viewContext
	 */
	public IViewContext getViewContext() {
		return getEditpart().getView().getContext();
	}

	/**
	 * Creates the bindings for the given elements.
	 * 
	 * @param yEmbeddable
	 * @param field
	 */
	protected void createBindings(YEmbeddable yEmbeddable,
			AbstractComponent abstractComponent) {

		// initialize the transient values
		//
		CoreModelUtil.initTransientValues(yEmbeddable);

		IBindingManager bindingManger = getViewContext()
				.getService(
						org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class
								.getName());
		// bind visible
		bindingManger.bindVisible(yEmbeddable, abstractComponent);
	}

	/**
	 * Creates the bindings for the given elements.
	 * 
	 * @param yEmbeddable
	 * @param field
	 */
	protected void createBindings(YAction yAction,
			AbstractComponent abstractComponent) {

		createBindings((YEmbeddable) yAction, abstractComponent);

		IBindingManager bindingManger = getViewContext()
				.getService(
						org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class
								.getName());

		// bind enabled
		bindingManger.bindEnabled(yAction, abstractComponent);
	}

	/**
	 * Creates the bindings for the given elements.
	 * 
	 * @param yEmbeddable
	 * @param field
	 */
	protected void createBindings(YField yField,
			AbstractComponent abstractComponent) {

		createBindings((YEmbeddable) yField, abstractComponent);

		IBindingManager bindingManger = getViewContext()
				.getService(
						org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class
								.getName());

		// bind enabled
		bindingManger.bindEnabled(yField, abstractComponent);

		// bind readonly
		if (abstractComponent instanceof Property.ReadOnlyStatusChangeNotifier) {
			bindingManger.bindReadonly(yField,
					(Property.ReadOnlyStatusChangeNotifier) abstractComponent);
		} else {
			bindingManger.bindReadonlyOneway(yField, abstractComponent);
		}
	}

	/**
	 * Creates the model binding from ridget to ECView-model.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 * @return
	 */
	protected Object createModelBinding(EObject model,
			EStructuralFeature modelFeature, Field<?> field) {
		return createModelBinding(model, modelFeature, field, null, null);
	}

	/**
	 * Creates the model binding from ridget to ECView-model.
	 * 
	 * @param model
	 * @param modelFeature
	 * @param field
	 * @return binding
	 */
	protected Binding createModelBinding(EObject model,
			EStructuralFeature modelFeature, Field<?> field,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeValue(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	@Override
	public IObservable getObservableValue(Object model) {
		return internalGetObservableEndpoint((YEmbeddableBindingEndpoint) model);
	}

	/**
	 * Has to provide an instance of IObservable for the given bindableValue.
	 * 
	 * @param bindableValue
	 * @return
	 */
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		throw new UnsupportedOperationException("Must be overridden!");
	}

	protected EObject castEObject(Object model) {
		return (EObject) model;
	}

}
