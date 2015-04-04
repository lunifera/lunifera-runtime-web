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

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.lunifera.ecview.core.common.beans.InMemoryBeanProvider;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableValueEndpoint;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YBeanReferenceField;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBeanReferenceFieldEditpart;
import org.lunifera.runtime.common.annotations.TargetEnumConstraints;
import org.lunifera.runtime.common.state.ISharedStateContext;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactory;
import org.lunifera.runtime.web.vaadin.common.data.StatefulInMemoryBeanSearchService;
import org.lunifera.runtime.web.vaadin.common.services.filter.AnnotationToVaadinFilterConverter;
import org.lunifera.runtime.web.vaadin.components.fields.BeanReferenceField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class BeanReferenceFieldPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BeanReferenceFieldPresentation.class);

	private final ModelAccess modelAccess;
	private BeanReferenceField<?> field;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public BeanReferenceFieldPresentation(IElementEditpart editpart) {
		super((IBeanReferenceFieldEditpart) editpart);
		this.modelAccess = new ModelAccess(
				(YBeanReferenceField) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Component doCreateWidget(Object parent) {
		if (field == null) {
			IBeanSearchServiceFactory searchServiceFactory = getViewContext()
					.getService(IBeanSearchServiceFactory.class.getName());

			IBeanSearchService<?> service = getSearchService(searchServiceFactory);
			ISharedStateContext sharedState = getViewContext().getService(
					ISharedStateContext.class.getName());
			field = new BeanReferenceField(getEditpart().getId(), "",
					modelAccess.yField.getType(), service, createFilter(),
					sharedState);
			field.addStyleName(CSS_CLASS_CONTROL);
			field.setNullRepresentation("");
			field.setImmediate(true);
			setupComponent(field, getCastedModel());
			field.setItemCaptionPropertyId(modelAccess.yField
					.getCaptionPropertyPath());
			field.setItemIconPropertyId(modelAccess.yField
					.getImagePropertyPath());

			associateWidget(field, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				field.setId(modelAccess.getCssID());
			} else {
				field.setId(getEditpart().getId());
			}

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

	/**
	 * Creates a vaadin filter for the field annotations.
	 * 
	 * @return
	 */
	protected Filter createFilter() {
		Class<?> sourceType = modelAccess.yField.getReferenceSourceType();
		String sourceProperty = modelAccess.yField
				.getReferenceSourceTypeProperty();
		if (sourceType == null || sourceProperty == null
				|| sourceProperty.equals("")) {
			return null;
		}

		try {
			java.lang.reflect.Field sourceField = sourceType
					.getDeclaredField(sourceProperty);

			TargetEnumConstraints annotation = sourceField
					.getAnnotation(TargetEnumConstraints.class);
			if (annotation != null) {
				return AnnotationToVaadinFilterConverter
						.createFilter(annotation);
			}

		} catch (Throwable e) {
			LOGGER.warn("{}", e);
			return null;
		}

		return null;
	}

	/**
	 * Returns the bean search service.
	 * 
	 * @param searchServiceFactory
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected IBeanSearchService<?> getSearchService(
			IBeanSearchServiceFactory searchServiceFactory) {
		IBeanSearchService<?> service = null;
		Class<? extends InMemoryBeanProvider> inMemoryProviderClass = (Class<? extends InMemoryBeanProvider>) modelAccess.yField
				.getInMemoryBeanProvider();
		if (inMemoryProviderClass != null) {
			try {
				InMemoryBeanProvider provider = inMemoryProviderClass
						.newInstance();
				StatefulInMemoryBeanSearchService tempService = new StatefulInMemoryBeanSearchService(
						modelAccess.yField.getType());
				tempService.addBeans(provider.getBeans());
				service = tempService;
			} catch (InstantiationException e) {
				LOGGER.error("{}", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("{}", e);
			}
		}
		if (service == null) {
			service = searchServiceFactory.createService(modelAccess.yField
					.getType());
		}
		return service;
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
				modelAccess.getLabelI18nKey(), getLocale(), field);
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
				ExtensionModelPackage.Literals.YBEAN_REFERENCE_FIELD__VALUE);
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YBeanReferenceField yField,
			BeanReferenceField<?> field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_Value(castEObject(getModel()),
				ExtensionModelPackage.Literals.YBEAN_REFERENCE_FIELD__VALUE,
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
		private final YBeanReferenceField yField;

		public ModelAccess(YBeanReferenceField yField) {
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
