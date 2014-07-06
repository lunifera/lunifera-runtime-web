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
import org.eclipse.emf.ecp.ecview.extension.model.datatypes.YDecimalDatatype;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.eclipse.emf.ecp.ecview.util.emf.ModelUtil;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;
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
public class DecimalFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private DecimalField decimalField;
	private Binding binding_valueToUI;
	private ObjectProperty<Double> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public DecimalFieldPresentation(IElementEditpart editpart) {
		super((IDecimalFieldEditpart) editpart);
		this.modelAccess = new ModelAccess((YDecimalField) editpart.getModel());
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

			decimalField = new DecimalField();
			decimalField.addStyleName(CSS_CLASS_CONTROL);
			decimalField.setImmediate(true);

			property = new ObjectProperty<Double>(0d, Double.class);
			decimalField.setPropertyDataSource(property);

			decimalField
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							if (binding_valueToUI != null) {
								updateUiToECViewModel();
							}
						}
					});

			componentBase.addComponent(decimalField);

			if (modelAccess.isCssClassValid()) {
				decimalField.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			doApplyDatatype(modelAccess.yDecimalField.getDatatype());

			initializeField(decimalField);

			// creates the binding for the field
			createBindings(modelAccess.yDecimalField, decimalField);

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
		if (decimalField == null) {
			return;
		}

		int oldPrecision = decimalField.getPrecision();
		if (yDt == null) {
			decimalField.setPrecision(2);
			decimalField.setUseGrouping(true);
			decimalField.setMarkNegative(true);
		} else {
			YDecimalDatatype yCasted = (YDecimalDatatype) yDt;
			decimalField.setPrecision(yCasted.getPrecision());
			decimalField.setUseGrouping(yCasted.isGrouping());
			decimalField.setMarkNegative(yCasted.isMarkNegative());
		}

		if (isRendered()) {
			// if the precision changed, then update the value from the ui field
			// to the ECViewModel
			if (oldPrecision != decimalField.getPrecision()) {
				updateUiToECViewModel();
			}
		}

	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// update the captions
		applyCaptions();

		// tell the number field about the locale change
		decimalField.setLocale(locale);
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
		return decimalField;
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
				ExtensionModelPackage.Literals.YDECIMAL_FIELD__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YDecimalField yField, DecimalField field) {
		// create the model binding from ridget to ECView-model

		binding_valueToUI = createModelBinding(castEObject(getModel()),
				ExtensionModelPackage.Literals.YDECIMAL_FIELD__VALUE, field,
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
			decimalField = null;

			sendUnrenderedLifecycleEvent();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();
			binding_valueToUI = null;
		} finally {
			super.internalDispose();
		}
	}

	protected void updateUiToECViewModel() {
		if (binding_valueToUI != null) {
			binding_valueToUI.updateTargetToModel();
		}

		Binding domainToEObjectBinding = ModelUtil
				.getValueBinding((YValueBindable) getModel());
		if (domainToEObjectBinding != null) {
			domainToEObjectBinding.updateTargetToModel();
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YDecimalField yDecimalField;

		public ModelAccess(YDecimalField yDecimalField) {
			super();
			this.yDecimalField = yDecimalField;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yDecimalField.getCssClass();
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
			return yDecimalField.getCssID();
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
			return yDecimalField.getDatadescription() != null
					&& yDecimalField.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yDecimalField.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yDecimalField.getDatadescription() != null
					&& yDecimalField.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yDecimalField.getDatadescription().getLabelI18nKey();
		}
	}
}
