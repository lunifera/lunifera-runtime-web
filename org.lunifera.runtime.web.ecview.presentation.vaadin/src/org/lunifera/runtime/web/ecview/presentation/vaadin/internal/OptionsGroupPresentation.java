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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.model.core.YEmbeddableBindingEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableCollectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableMultiSelectionEndpoint;
import org.lunifera.ecview.core.common.model.core.YEmbeddableSelectionEndpoint;
import org.lunifera.ecview.core.databinding.emf.model.ECViewModelBindable;
import org.lunifera.ecview.core.extension.model.extension.ExtensionModelPackage;
import org.lunifera.ecview.core.extension.model.extension.YOptionsGroup;
import org.lunifera.ecview.core.extension.model.extension.YSelectionType;
import org.lunifera.ecview.core.ui.core.editparts.extension.IOptionsGroupEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.lunifera.runtime.web.vaadin.common.data.DeepResolvingBeanItemContainer;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchServiceFactory;
import org.lunifera.runtime.web.vaadin.components.fields.BeanServiceLazyLoadingContainer;

import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.OptionGroup;

/**
 * This presenter is responsible to render a list on the given layout.
 */
@SuppressWarnings("restriction")
public class OptionsGroupPresentation extends
		AbstractFieldWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private OptionGroup optionsGroup;
	private ObjectProperty<?> property;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public OptionsGroupPresentation(IElementEditpart editpart) {
		super((IOptionsGroupEditpart) editpart);
		this.modelAccess = new ModelAccess((YOptionsGroup) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Component doCreateWidget(Object parent) {
		if (optionsGroup == null) {

			optionsGroup = new CustomOptionGroup();
			optionsGroup.addStyleName(CSS_CLASS_CONTROL);
			optionsGroup
					.setMultiSelect(modelAccess.yField.getSelectionType() == YSelectionType.MULTI);
			optionsGroup.setImmediate(true);
			setupComponent(optionsGroup, getCastedModel());
			associateWidget(optionsGroup, modelAccess.yField);

			if (modelAccess.isCssIdValid()) {
				optionsGroup.setId(modelAccess.getCssID());
			} else {
				optionsGroup.setId(getEditpart().getId());
			}

			if (optionsGroup.isMultiSelect()) {
				property = new ObjectProperty(new HashSet(), Set.class);
			} else {
				if (modelAccess.yField.getType() != null) {
					property = new ObjectProperty(null,
							modelAccess.yField.getType());
				} else {
					property = new ObjectProperty(null, Object.class);
				}
			}
			optionsGroup.setPropertyDataSource(property);

			if (modelAccess.yField.getType() == String.class) {
				IndexedContainer datasource = new IndexedContainer();
				optionsGroup.setContainerDataSource(datasource);
				optionsGroup.setItemCaptionMode(ItemCaptionMode.ID);
			} else {
				if (modelAccess.yField.getType() != null) {
					if (!modelAccess.yField.isUseBeanService()) {
						DeepResolvingBeanItemContainer datasource = new DeepResolvingBeanItemContainer(
								modelAccess.yField.getType());
						optionsGroup.setContainerDataSource(datasource);
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
							optionsGroup.setContainerDataSource(datasource);
						}
					}
				} else {
					IndexedContainer container = new IndexedContainer();
					container.addContainerProperty("for", String.class, null);
					container.addContainerProperty("preview", String.class,
							null);
					container.addItem(new String[] { "Some value", "other" });
					optionsGroup.setContainerDataSource(container);
				}
			}

			String itemCaptionProperty = modelAccess.yField
					.getCaptionProperty();
			if (itemCaptionProperty != null && !itemCaptionProperty.equals("")) {
				optionsGroup.setItemCaptionPropertyId(itemCaptionProperty);
			}

			String itemImageProperty = modelAccess.yField.getImageProperty();
			if (itemImageProperty != null && !itemImageProperty.equals("")) {
				optionsGroup.setItemIconPropertyId(itemImageProperty);
			}

			// creates the binding for the field
			createBindings(modelAccess.yField, optionsGroup);

			if (modelAccess.isCssClassValid()) {
				optionsGroup.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

			initializeField(optionsGroup);
		}
		return optionsGroup;
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
				modelAccess.getLabelI18nKey(), getLocale(), optionsGroup);
	}

	@Override
	protected Field<?> doGetField() {
		return optionsGroup;
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
		} else if (bindableValue instanceof YEmbeddableMultiSelectionEndpoint) {
			return internalGetMultiSelectionEndpoint();
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
				ExtensionModelPackage.Literals.YOPTIONS_GROUP__COLLECTION)
				.observe(getModel());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableValue internalGetSelectionEndpoint(
			YEmbeddableSelectionEndpoint yEndpoint) {

		String attributePath = ECViewModelBindable.getAttributePath(
				ExtensionModelPackage.Literals.YOPTIONS_GROUP__SELECTION,
				yEndpoint.getAttributePath());

		// return the observable value for text
		return ECViewModelBindable.observeValue(castEObject(getModel()),
				attributePath, modelAccess.yField.getType(),
				modelAccess.yField.getEmfNsURI());
	}

	/**
	 * Returns the observable to observe the selection.
	 * 
	 * @return
	 */
	protected IObservableList internalGetMultiSelectionEndpoint() {
		// return the observable value for text
		return EMFProperties.list(
				ExtensionModelPackage.Literals.YOPTIONS_GROUP__MULTI_SELECTION)
				.observe(getModel());
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yField
	 * @param field
	 */
	protected void createBindings(YOptionsGroup yField, OptionGroup field) {
		// create the model binding from ridget to ECView-model
		registerBinding(createBindings_ContainerContents(
				castEObject(getModel()),
				ExtensionModelPackage.Literals.YOPTIONS_GROUP__COLLECTION,
				field, yField.getType()));

		if (modelAccess.yField.getSelectionType() == YSelectionType.MULTI) {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsMultiSelection(
					castEObject(getModel()),
					ExtensionModelPackage.Literals.YOPTIONS_GROUP__MULTI_SELECTION,
					field, yField.getType()));
		} else {
			// create the model binding from ridget to ECView-model
			registerBinding(createBindingsSelection(castEObject(getModel()),
					ExtensionModelPackage.Literals.YOPTIONS_GROUP__SELECTION,
					field, yField.getType()));

		}

		super.createBindings(yField, field, null);
	}

	@Override
	public Component getWidget() {
		return optionsGroup;
	}

	@Override
	public boolean isRendered() {
		return optionsGroup != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doUnrender() {
		if (optionsGroup != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) optionsGroup
					.getParent());
			if (parent != null) {
				parent.removeComponent(optionsGroup);
			}

			// remove assocations
			unassociateWidget(optionsGroup);

			optionsGroup = null;
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
		private final YOptionsGroup yField;

		public ModelAccess(YOptionsGroup yField) {
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
	private static class CustomOptionGroup extends OptionGroup {
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
				}
			}
			return null;
		}
	}
}
