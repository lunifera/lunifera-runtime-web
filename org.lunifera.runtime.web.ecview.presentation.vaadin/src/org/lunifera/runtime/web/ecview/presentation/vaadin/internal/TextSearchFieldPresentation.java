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
import org.lunifera.ecview.core.extension.model.extension.YTextSearchField;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextSearchFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IBindingManager;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractFieldWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.components.fields.search.TextSearchField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class TextSearchFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> implements
		IFilterProvidingPresentation {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TextSearchFieldPresentation.class);

	private final ModelAccess modelAccess;
	private TextSearchField field;
	private ObjectProperty<String> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public TextSearchFieldPresentation(IElementEditpart editpart) {
		super((ITextSearchFieldEditpart) editpart);
		this.modelAccess = new ModelAccess(
				(YTextSearchField) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (field == null) {
			IBindingManager bm = getViewContext().getService(
					IECViewBindingManager.class.getName());
			field = new TextSearchField(getEditpart().getId(),
					modelAccess.yField.getPropertyPath(),
					bm.getDatabindingContext());
			field.addStyleName(CSS_CLASS_CONTROL);
			field.addStyleName(IConstants.CSS_CLASS_SEARCHFIELD);

			field.setNullRepresentation("");
			field.setImmediate(true);
			setupComponent(field, getCastedModel());

			associateWidget(field, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				field.setId(modelAccess.getCssID());
			} else {
				field.setId(getEditpart().getId());
			}

			property = new ObjectProperty<String>(null, String.class);
			field.setPropertyDataSource(property);

			// creates the binding for the field
			createBindings(modelAccess.yField, field);

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
		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), field);

		field.setDescription(service.getValue(
				IConstants.I18N_TOOLTIP_TEXTSEARCHFIELD, getLocale()));
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
				ExtensionModelPackage.Literals.YTEXT_SEARCH_FIELD__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YTextSearchField yField, TextSearchField field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_Value(castEObject(getModel()),
				ExtensionModelPackage.Literals.YTEXT_SEARCH_FIELD__VALUE, field));

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
		private final YTextSearchField yField;

		public ModelAccess(YTextSearchField yField) {
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
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabel() : null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yField.getDatadescription() != null ? yField
					.getDatadescription().getLabelI18nKey() : null;
		}
	}
}
