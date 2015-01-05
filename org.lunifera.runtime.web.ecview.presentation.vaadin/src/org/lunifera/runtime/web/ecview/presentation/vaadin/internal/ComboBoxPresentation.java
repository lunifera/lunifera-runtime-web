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
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableCollectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.databinding.emf.model.ECViewModelBindable;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YComboBox;
import org.lunifera.ecview.core.ui.core.editparts.extension.IComboBoxEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactory;
import org.lunifera.runtime.web.vaadin.components.fields.BeanServiceLazyLoadingContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * This presenter is responsible to render a combo box on the given layout.
 */
@SuppressWarnings("restriction")
public class ComboBoxPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComboBoxPresentation.class);

	private final ModelAccess modelAccess;
	private ComboBox combo;
	@SuppressWarnings("rawtypes")
	private ObjectProperty property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public ComboBoxPresentation(IElementEditpart editpart) {
		super((IComboBoxEditpart) editpart);
		this.modelAccess = new ModelAccess((YComboBox) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Component doCreateWidget(Object parent) {
		if (combo == null) {

			combo = new CustomComboBox();
			combo.addStyleName(CSS_CLASS_CONTROL);
			combo.setImmediate(true);
			setupComponent(combo, getCastedModel());

			associateWidget(combo, modelAccess.yField);
			if (modelAccess.isCssIdValid()) {
				combo.setId(modelAccess.getCssID());
			} else {
				combo.setId(getEditpart().getId());
			}

			try {
				property = new ObjectProperty(null,
						modelAccess.yField.getType());
				combo.setPropertyDataSource(property);

				if (modelAccess.yField.getType() != null) {
					if (!modelAccess.yField.isUseBeanService()) {
						DeepResolvingBeanItemContainer datasource = new DeepResolvingBeanItemContainer(
								modelAccess.yField.getType());
						combo.setContainerDataSource(datasource);
					} else {
						IBeanSearchServiceFactory factory = getViewContext()
								.getService(
										IBeanSearchServiceFactory.class
												.getName());
						if (factory != null) {
							BeanServiceLazyLoadingContainer<?> datasource = new BeanServiceLazyLoadingContainer(
									factory.createService(modelAccess.yField
											.getType()),
									modelAccess.yField.getType());
							combo.setContainerDataSource(datasource);
						}
					}
				}

				String itemCaptionProperty = modelAccess.yField
						.getCaptionProperty();
				if (itemCaptionProperty != null
						&& !itemCaptionProperty.equals("")) {
					combo.setItemCaptionPropertyId(itemCaptionProperty);
					combo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				} else {
					combo.setItemCaptionMode(ItemCaptionMode.ID);
				}

				String itemImageProperty = modelAccess.yField
						.getImageProperty();
				if (itemImageProperty != null && !itemImageProperty.equals("")) {
					combo.setItemIconPropertyId(itemImageProperty);
				}

				// creates the binding for the field
				createBindings(modelAccess.yField, combo);

				if (modelAccess.isCssClassValid()) {
					combo.addStyleName(modelAccess.getCssClass());
				}

				applyCaptions();

				initializeField(combo);
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}
		}
		return combo;
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
				modelAccess.getLabelI18nKey(), getLocale(), combo);
	}

	@Override
	protected Field<?> doGetField() {
		return combo;
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new IllegalArgumentException(
					"BindableValue must not be null!");
		}

		if (bindableValue instanceof YEmbeddableCollectionEndpoint) {
			return internalGetCollectionEndpoint();
		} else if (bindableValue instanceof YEmbeddableSelectionEndpoint) {
			return internalGetSelectionEndpoint((YEmbeddableSelectionEndpoint) bindableValue);
		}
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	/**
	 * Returns the observable to observe the collection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetCollectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YCOMBO_BOX__COLLECTION).observe(
				getModel());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YCOMBO_BOX__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yField.getType(),
				modelAccess.yField.getEmfNsURI());
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YComboBox yField, ComboBox field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_ContainerContents(
				castEObject(getModel()),
				ExtensionModelPackage.Literals.YCOMBO_BOX__COLLECTION, field,
				yField.getType()));

		// create the model binding from ridget to ECView-model
		registerBinding(createBindingsSelection(castEObject(getModel()),
				ExtensionModelPackage.Literals.YCOMBO_BOX__SELECTION, field,
				yField.getType()));

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return combo;
	}

	@Override
	public boolean isRendered() {
		return combo != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (combo != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) combo.getParent());
			if (parent != null) {
				parent.removeComponent(combo);
			}

			// remove assocations
			unassociateWidget(combo);

			combo = null;
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
		private final YComboBox yField;

		public ModelAccess(YComboBox yField) {
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

	/**
	 * Converts the string value of the item icon property to
	 * {@link ThemeResource}.
	 */
	@SuppressWarnings("serial")
	private static class CustomComboBox extends ComboBox {
		private Object itemIconPropertyId;

		@Override
		public void setItemIconPropertyId(Object propertyId)
				throws IllegalArgumentException {
			if (propertyId == null) {
				super.setItemIconPropertyId(propertyId);
			} else if (!getContainerPropertyIds().contains(propertyId)) {
				super.setItemIconPropertyId(propertyId);
			} else if (String.class.isAssignableFrom(getType(propertyId))) {
				itemIconPropertyId = propertyId;
			} else {
				super.setItemIconPropertyId(propertyId);
			}
		}

		public Object getItemIconPropertyId() {
			return itemIconPropertyId != null ? itemIconPropertyId : super
					.getItemIconPropertyId();
		}

		public Resource getItemIcon(Object itemId) {
			if (itemIconPropertyId == null) {
				return super.getItemIcon(itemId);
			} else {
				final Property<?> ip = getContainerProperty(itemId,
						getItemIconPropertyId());
				if (ip == null) {
					return null;
				}
				final Object icon = ip.getValue();
				try {
					if (icon instanceof String) {
						return new ThemeResource((String) icon);
					}
				} catch (Exception e) {
					// nothing to do
				}
			}
			return null;
		}
	}

}
