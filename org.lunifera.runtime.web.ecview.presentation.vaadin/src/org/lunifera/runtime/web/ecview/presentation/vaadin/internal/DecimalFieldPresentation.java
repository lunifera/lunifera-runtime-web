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
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.binding.IBindingEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableValueEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YValueBindable;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDecimalField;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding.BindingUtil;
import org.lunifera.runtime.web.vaadin.components.fields.DecimalField;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class DecimalFieldPresentation extends
		AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private DecimalField decimalField;
	private Binding eObjectToUIBinding;

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
	public Component createWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS__CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			decimalField = new DecimalField();
			decimalField.addStyleName(CSS_CLASS__CONTROL);
			decimalField.setSizeFull();

			decimalField
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							if (eObjectToUIBinding != null) {
								eObjectToUIBinding.updateTargetToModel();

								Binding domainToEObjectBinding = BindingUtil
										.getValueBinding((YValueBindable) getModel());
								if (domainToEObjectBinding != null) {
									domainToEObjectBinding
											.updateTargetToModel();
								}
							}
						}
					});

			// creates the binding for the field
			createBindings(modelAccess.yDecimalField, decimalField);

			componentBase.addComponent(decimalField);

			if (modelAccess.isCssClassValid()) {
				decimalField.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				decimalField.setCaption(modelAccess.getLabel());
			}

			decimalField.setPrecision(modelAccess.getPrecision());
			decimalField.setUseGrouping(modelAccess.isGrouping());

		}
		return componentBase;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new NullPointerException("BindableValue must not be null!");
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

		eObjectToUIBinding = createModelBinding(castEObject(getModel()),
				ExtensionModelPackage.Literals.YDECIMAL_FIELD__VALUE, field,
				null, null);

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
	public void unrender() {
		if (componentBase != null) {
			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			decimalField = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		// unrender the ui component
		unrender();

		eObjectToUIBinding = null;

		// dispose the binding too
		IBindingEditpart valueBindingEditpart = BindingUtil
				.getValueBindingEditpart((YValueBindable) getModel());
		if (valueBindingEditpart != null) {
			valueBindingEditpart.dispose();
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
		 * Returns the precision of the decimal field.
		 * 
		 * @return
		 */
		public int getPrecision() {
			return yDecimalField.getDatatype() != null ? yDecimalField
					.getDatatype().getPrecision() : 2;
		}

		/**
		 * Returns the grouping of the decimal field.
		 * 
		 * @return
		 */
		public boolean isGrouping() {
			return yDecimalField.getDatatype() != null ? yDecimalField
					.getDatatype().isGrouping() : true;
		}
	}
}
