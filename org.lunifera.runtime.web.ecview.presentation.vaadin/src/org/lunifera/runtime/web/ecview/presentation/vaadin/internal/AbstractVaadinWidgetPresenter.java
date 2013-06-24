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

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.disposal.AbstractDisposable;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YAction;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YField;
import org.eclipse.emf.ecp.ecview.common.model.core.util.CoreModelUtil;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.common.services.IServiceRegistry;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;

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
	protected void createBindings(YEmbeddable yEmbeddable, AbstractComponent abstractComponent) {

		// initialize the transient values
		//
		CoreModelUtil.initTransientValues(yEmbeddable);

		IBindingManager bindingManger = getViewContext().getService(
				org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class.getName());
		// bind visible
		bindingManger.bindVisible(yEmbeddable, abstractComponent);
	}

	/**
	 * Creates the bindings for the given elements.
	 * 
	 * @param yEmbeddable
	 * @param field
	 */
	protected void createBindings(YAction yAction, AbstractComponent abstractComponent) {

		createBindings((YEmbeddable) yAction, abstractComponent);

		IBindingManager bindingManger = getViewContext().getService(
				org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class.getName());

		// bind enabled
		bindingManger.bindEnabled(yAction, abstractComponent);
	}
	/**
	 * Creates the bindings for the given elements.
	 * 
	 * @param yEmbeddable
	 * @param field
	 */
	protected void createBindings(YField yField, AbstractComponent abstractComponent) {
		
		createBindings((YEmbeddable) yField, abstractComponent);
		
		IBindingManager bindingManger = getViewContext().getService(
				org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class.getName());
		
		// bind enabled
		bindingManger.bindEnabled(yField, abstractComponent);
		
		// bind readonly
		if (abstractComponent instanceof Property.ReadOnlyStatusChangeNotifier) {
			bindingManger.bindReadonly(yField,
					(Property.ReadOnlyStatusChangeNotifier) abstractComponent);
		}
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

}
