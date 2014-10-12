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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Locale;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YProgressBar;
import org.lunifera.ecview.core.ui.core.editparts.extension.IProgressBarEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.ProgressBar;

/**
 * This presenter is responsible to render a progress bar on the given layout.
 */
public class ProgressBarPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private ProgressBar progressBar;
	private ObjectProperty<Float> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public ProgressBarPresentation(IElementEditpart editpart) {
		super((IProgressBarEditpart) editpart);
		this.modelAccess = new ModelAccess((YProgressBar) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (progressBar == null) {

			progressBar = new ProgressBar();
			progressBar.addStyleName(CSS_CLASS_CONTROL);
			progressBar.setImmediate(true);
			setupComponent(progressBar, getCastedModel());

			associateWidget(progressBar, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				progressBar.setId(modelAccess.getCssID());
			} else {
				progressBar.setId(getEditpart().getId());
			}

			property = new ObjectProperty<Float>(0f, Float.class);
			progressBar.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yField, progressBar);

			if (modelAccess.isCssClassValid()) {
				progressBar.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(progressBar);
		}
		return progressBar;
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// no need to set the locale to the ui elements. Is handled by vaadin
		// internally.

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), progressBar);
	}

	@Override
	protected Field<?> doGetField() {
		return progressBar;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new IllegalArgumentException(
					"BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableValueEndpoint) {
			return internalGetValueEndpoint();
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe value.
	 * 
	 * @return
	 */
	protected IObservableValue internalGetValueEndpoint() {
		// return the observable value for text
		return EMFObservables.observeValue(castEObject(getModel()),
				ExtensionModelPackage.Literals.YPROGRESS_BAR__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YProgressBar yField, ProgressBar field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_Value(castEObject(getModel()),
				ExtensionModelPackage.Literals.YPROGRESS_BAR__VALUE,
				progressBar));

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return progressBar;
	}

	@Override
	public boolean isRendered() {
		return progressBar != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (progressBar != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) progressBar
					.getParent());
			if (parent != null) {
				parent.removeComponent(progressBar);
			}

			// remove assocations
			unassociateWidget(progressBar);

			progressBar = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YProgressBar yField;

		public ModelAccess(YProgressBar yField) {
			super();
			this.yField = yField;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yField.getCssClass();
		}

		/**
		 * Returns true, if the css class is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssClassValid() {
			return getCssClass() != null && !getCssClass().equals("");
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yField.getCssID();
		}

		/**
		 * Returns true, if the css id is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssIdValid() {
			return getCssID() != null && !getCssID().equals("");
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yField.getDatadescription() != null ? yField.getDatadescription().getLabel() : null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yField.getDatadescription() != null ? yField.getDatadescription().getLabelI18nKey() : null;
		}
	}
}
