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
import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableCollectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.databinding.emf.model.ECViewModelBindable;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YComboBox;
import org.lunifera.ecview.core.ui.core.editparts.extension.IComboBoxEditpart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
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

			associateWidget(combo, modelAccess.yCombo);
			if (modelAccess.isCssIdValid()) {
				combo.setId(modelAccess.getCssID());
			} else {
				combo.setId(getEditpart().getId());
			}

			try {
				property = new ObjectProperty(null,
						modelAccess.yCombo.getType());
				combo.setPropertyDataSource(property);

				if (modelAccess.yCombo.getType() != null) {
					BeanItemContainer datasource = null;
					datasource = new BeanItemContainer(
							modelAccess.yCombo.getType());
					combo.setContainerDataSource(datasource);
				}

				String itemCaptionProperty = modelAccess.yCombo
						.getItemCaptionProperty();
				if (itemCaptionProperty != null
						&& !itemCaptionProperty.equals("")) {
					combo.setItemCaptionPropertyId(itemCaptionProperty);
					combo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				} else {
					combo.setItemCaptionMode(ItemCaptionMode.ID);
				}

				String itemImageProperty = modelAccess.yCombo
						.getItemImageProperty();
				if (itemImageProperty != null && !itemImageProperty.equals("")) {
					combo.setItemIconPropertyId(itemImageProperty);
				}

				// creates the binding for the field
				createBindings(modelAccess.yCombo, combo);

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
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			combo.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				combo.setCaption(modelAccess.getLabel());
			}
		}
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
				attributePath, modelAccess.yCombo.getType(),
				modelAccess.yCombo.getEmfNsURI());
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
		private final YComboBox yCombo;

		public ModelAccess(YComboBox yCombo) {
			super();
			this.yCombo = yCombo;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yCombo.getCssClass();
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
			return yCombo.getCssID();
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
			return yCombo.getDatadescription() != null
					&& yCombo.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yCombo.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yCombo.getDatadescription() != null
					&& yCombo.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yCombo.getDatadescription().getLabelI18nKey();
		}
	}

	/**
	 * Converts the string value of the item icon property to
	 * {@link ThemeResource}.
	 */
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
				if (icon instanceof String) {
					return new ThemeResource((String) icon);
				}
			}
			return null;
		}
	}

}
