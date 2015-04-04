/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.common.model.core.YValueBindable;
import org.lunifera.ecview.core.common.model.datatypes.YDatatype;
import org.lunifera.ecview.core.extension.model.datatypes.YNumericDatatype;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YNumericField;
import org.lunifera.ecview.core.ui.core.editparts.extension.INumericFieldEditpart;
import org.lunifera.ecview.core.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.components.fields.NumericField;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class NumericFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private NumericField numberField;
	private Binding binding_valueToUI;
	private ObjectProperty<Double> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public NumericFieldPresentation(IElementEditpart editpart) {
		super((INumericFieldEditpart) editpart);
		this.modelAccess = new ModelAccess((YNumericField) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("serial")
	@Override
	public Component doCreateWidget(Object parent) {
		if (numberField == null) {

			numberField = new NumericField();
			numberField.addStyleName(CSS_CLASS_CONTROL);
			numberField.setImmediate(true);
			setupComponent(numberField, getCastedModel());

			associateWidget(numberField, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				numberField.setId(modelAccess.getCssID());
			} else {
				numberField.setId(getEditpart().getId());
			}

			property = new ObjectProperty<Double>(0d, Double.class);
			numberField.setPropertyDataSource(property);

			numberField
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							if (binding_valueToUI != null) {
								binding_valueToUI.updateTargetToModel();

								Binding domainToEObjectBinding = ModelUtil
										.getValueBinding((YValueBindable) getModel());
								if (domainToEObjectBinding != null) {
									domainToEObjectBinding
											.updateTargetToModel();
								}
							}
						}
					});

			if (modelAccess.isCssClassValid()) {
				numberField.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();
			doApplyDatatype(modelAccess.yField.getDatatype());

			initializeField(numberField);

			// creates the binding for the field
			createBindings(modelAccess.yField, numberField);

			// send an event, that the content was rendered again
			sendRenderedLifecycleEvent();

		}
		return numberField;
	}

	/**
	 * Applies the datatype options to the field.
	 * 
	 * @param yDt
	 */
	protected void doApplyDatatype(YDatatype yDt) {
		if (numberField == null) {
			return;
		}
		if (yDt == null) {
			numberField.setUseGrouping(true);
			numberField.setMarkNegative(true);
		} else {
			YNumericDatatype yCasted = (YNumericDatatype) yDt;
			numberField.setUseGrouping(yCasted.isGrouping());
			numberField.setMarkNegative(yCasted.isMarkNegative());
		}
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// update the captions
		applyCaptions();

		// tell the number field about the locale change
		numberField.setLocale(locale);
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), numberField);
	}

	@Override
	protected Field<?> doGetField() {
		return numberField;
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
				ExtensionModelPackage.Literals.YNUMERIC_FIELD__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YNumericField yField, NumericField field) {
		// create the model binding from ridget to ECView-model

		binding_valueToUI = createModelBinding(castEObject(getModel()),
				ExtensionModelPackage.Literals.YNUMERIC_FIELD__VALUE, field,
				null, null);

		registerBinding(binding_valueToUI);

		super.createBindings(yField, field, null);
	}

	protected Binding createModelBinding(EObject model,
			EStructuralFeature modelFeature, AbstractField<?> field,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		IBindingManager bindingManager = getViewContext()
				.getService(
						org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
								.getName());
		if (bindingManager != null) {
			// bind the value of yText to textRidget
			IObservableValue modelObservable = EMFObservables.observeValue(
					model, modelFeature);
			IObservableValue uiObservable = VaadinObservables
					.observeConvertedValue(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	@Override
	public Component getWidget() {
		return numberField;
	}

	@Override
	public boolean isRendered() {
		return numberField != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (numberField != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) numberField
					.getParent());
			if (parent != null) {
				parent.removeComponent(numberField);
			}

			// remove assocations
			unassociateWidget(numberField);

			numberField = null;
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
		private final YNumericField yField;

		public ModelAccess(YNumericField yField) {
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
