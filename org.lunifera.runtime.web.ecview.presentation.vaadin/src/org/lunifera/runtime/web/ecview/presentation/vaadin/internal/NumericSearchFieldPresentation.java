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
import org.lunifera.ecview.core.common.binding.IECViewBindingManager;
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.filter.IFilterProvidingPresentation;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YNumericSearchField;
import org.lunifera.ecview.core.ui.core.editparts.extension.INumericSearchFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.vaadin.common.IFilterProvider;
import org.lunifera.runtime.web.vaadin.components.fields.search.NumericSearchField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class NumericSearchFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> implements IFilterProvidingPresentation {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NumericSearchFieldPresentation.class);
	
	private final ModelAccess modelAccess;
	private NumericSearchField field;
	private ObjectProperty<String> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public NumericSearchFieldPresentation(IElementEditpart editpart) {
		super((INumericSearchFieldEditpart) editpart);
		this.modelAccess = new ModelAccess(
				(YNumericSearchField) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (field == null) {

			IBindingManager bm = getViewContext().getService(
					IECViewBindingManager.class.getName());
			field = new NumericSearchField(getEditpart().getId(),
					modelAccess.yText.getName(), bm.getDatabindingContext());
			field.addStyleName(CSS_CLASS_CONTROL);
			field.addStyleName(IConstants.CSS_CLASS_SEARCHFIELD);
			
			IFilterProvider filterProvider = getViewContext().getService(
					IFilterProvider.class.getName());
			if (filterProvider != null) {
				field.setFilterProvider(filterProvider);
			} else {
				LOGGER.error("No IFilterProvider available. Filters can not be provided!");
			}
			
			field.setNullRepresentation("");
			field.setImmediate(true);
			setupComponent(field, getCastedModel());

			associateWidget(field, modelAccess.yText);
			if (modelAccess.isCssIdValid()) {
				field.setId(modelAccess.getCssID());
			} else {
				field.setId(getEditpart().getId());
			}

			property = new ObjectProperty<String>(null, String.class);
			field.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yText, field);

			if (modelAccess.isCssClassValid()) {
				field.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(field);
		}
		return field;
	}
	
	@Override
	public Object getFilter() {
		return field != null ? field.getFilter() : null;
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
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			field.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				field.setCaption(modelAccess.getLabel());
			}
		}
		
		field.setDescription(service.getValue(IConstants.I18N_TOOLTIP_NUMBERSEARCHFIELD, getLocale()));
	}

	@Override
	protected Field<?> doGetField() {
		return field;
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
				ExtensionModelPackage.Literals.YNUMERIC_SEARCH_FIELD__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YNumericSearchField yField,
			NumericSearchField field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_Value(castEObject(getModel()),
				ExtensionModelPackage.Literals.YNUMERIC_SEARCH_FIELD__VALUE,
				field));

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return field;
	}

	@Override
	public boolean isRendered() {
		return field != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (field != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) field.getParent());
			if (parent != null) {
				parent.removeComponent(field);
			}

			// remove assocations
			unassociateWidget(field);

			field = null;
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
		private final YNumericSearchField yText;

		public ModelAccess(YNumericSearchField yText) {
			super();
			this.yText = yText;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yText.getCssClass();
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
			return yText.getCssID();
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
			return yText.getDatadescription() != null
					&& yText.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yText.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yText.getDatadescription() != null
					&& yText.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yText.getDatadescription().getLabelI18nKey();
		}
	}
}
