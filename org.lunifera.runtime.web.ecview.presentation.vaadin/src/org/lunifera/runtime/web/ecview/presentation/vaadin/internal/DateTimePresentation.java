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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableValueEndpoint;
import org.eclipse.emf.ecp.ecview.extension.model.extension.ExtensionModelPackage;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YDateTime;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDateTimeEditpart;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;

/**
 * This presenter is responsible to render a text area on the given layout.
 */
public class DateTimePresentation extends
		AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private DateField dateField;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public DateTimePresentation(IElementEditpart editpart) {
		super((IDateTimeEditpart) editpart);
		this.modelAccess = new ModelAccess((YDateTime) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
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

			dateField = new DateField();
			dateField.addStyleName(CSS_CLASS__CONTROL);
			dateField.setSizeFull();

			// creates the binding for the field
			createBindings(modelAccess.yDateTime, dateField);

			componentBase.addComponent(dateField);

			if (modelAccess.isCssClassValid()) {
				dateField.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				dateField.setCaption(modelAccess.getLabel());
			}

			dateField.setDateFormat(modelAccess.getDateformat());

			dateField
					.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(Property.ValueChangeEvent event) {
							Binding binding = BindingUtil
									.getValueBinding(modelAccess.yDateTime);
							if (binding != null) {
								binding.updateTargetToModel();
							}
						}
					});
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
				ExtensionModelPackage.Literals.YDATE_TIME__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YDateTime yField, DateField field) {
		// create the model binding from ridget to ECView-model
		createModelBinding(castEObject(getModel()),
				ExtensionModelPackage.Literals.YDATE_TIME__VALUE, field);

		super.createBindings(yField, field);
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
			dateField = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		// unrender the ui component
		unrender();
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YDateTime yDateTime;

		public ModelAccess(YDateTime yDateTime) {
			super();
			this.yDateTime = yDateTime;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yDateTime.getCssClass();
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
			return yDateTime.getCssID();
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
			return yDateTime.getDatadescription() != null
					&& yDateTime.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yDateTime.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the date format is valid.
		 * 
		 * @return
		 */
		public boolean isDateformatValid() {
			return yDateTime.getDatadescription() != null
					&& yDateTime.getDatatype().getFormat() != null;
		}

		/**
		 * Returns the date format.
		 * 
		 * @return
		 */
		public String getDateformat() {
			if (yDateTime.getDatatype() == null) {
				return "yyyy.MM.dd HH:mm";
			}

			switch (yDateTime.getDatatype().getFormat()) {
			case DATE:
				return "yyyy.MM.dd";
			case DATE_TIME:
				return "yyyy.MM.dd HH:mm";
			case TIME:
				return "HH:mm:ss";
			}
			return "yyyy.MM.dd HH:mm";
		}
	}
}
