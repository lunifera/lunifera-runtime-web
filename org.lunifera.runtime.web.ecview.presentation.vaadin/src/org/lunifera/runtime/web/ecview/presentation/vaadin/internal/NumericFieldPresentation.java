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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableValueEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YValueBindable;
import org.eclipse.emf.ecp.ecview.common.model.datatypes.YDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YNumericDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YNumericField;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.INumericFieldEditpart;
import org.eclipse.emf.ecp.ecview.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.vaadin.components.fields.NumberField;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class NumericFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private NumberField numberField;
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
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			numberField = new NumberField();
			numberField.addStyleName(CSS_CLASS_CONTROL);
			numberField.setImmediate(true);

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

			componentBase.addComponent(numberField);

			if (modelAccess.isCssClassValid()) {
				numberField.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();
			doApplyDatatype(modelAccess.yNumericField.getDatatype());

			initializeField(numberField);

			// creates the binding for the field
			createBindings(modelAccess.yNumericField, numberField);

			// send an event, that the content was rendered again
			sendRenderedLifecycleEvent();

		}
		return componentBase;
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
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			componentBase.setCaption(service.getValue(
					modelAccess.getLabelI18nKey(), getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				componentBase.setCaption(modelAccess.getLabel());
			}
		}
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
	protected void createBindings(YNumericField yField, NumberField field) {
		// create the model binding from ridget to ECView-model

		binding_valueToUI = createModelBinding(castEObject(getModel()),
				ExtensionModelPackage.Literals.YNUMERIC_FIELD__VALUE, field,
				null, null);

		registerBinding(binding_valueToUI);

		super.createBindings(yField, field);
	}

	protected Binding createModelBinding(EObject model,
			EStructuralFeature modelFeature, AbstractField<?> field,
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
					.observeConvertedValue(field);
			return bindingManager.bindValue(uiObservable, modelObservable,
					targetToModel, modelToTarget);
		}
		return null;
	}

	@Override
	public Component getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (componentBase != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
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
		private final YNumericField yNumericField;

		public ModelAccess(YNumericField yNumericField) {
			super();
			this.yNumericField = yNumericField;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yNumericField.getCssClass();
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
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yNumericField.getCssID();
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
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelValid() {
			return yNumericField.getDatadescription() != null
					&& yNumericField.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yNumericField.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yNumericField.getDatadescription() != null
					&& yNumericField.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yNumericField.getDatadescription().getLabelI18nKey();
		}
	}
}
