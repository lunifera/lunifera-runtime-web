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
package org.lunifera.runtime.web.vaadin.components.fields;

import org.eclipse.core.databinding.Binding;
import org.lunifera.runtime.web.vaadin.common.data.IBeanSearchService;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class BeanReferenceField<BEAN> extends CustomField<BEAN> {

	private ObjectProperty<BEAN> property;
	private IBeanSearchService<BEAN> searchService;
	private final Class<BEAN> type;
	private CustomComboBox comboBox;
	private Binding valueBinding;
	private Object itemCaptionPropertyId;
	private Object itemIconPropertyId;

	public BeanReferenceField(String id, Object propertyId, Class<BEAN> type,
			IBeanSearchService<BEAN> searchService) {
		this.type = type;
		this.searchService = searchService;

		property = new ObjectProperty<BEAN>(null, type, false);
		super.setPropertyDataSource(property);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Component initContent() {

		HorizontalLayout root = new HorizontalLayout();
		root.addStyleName("l-beansearchfield");
		comboBox = new CustomComboBox();
		comboBox.setImmediate(true);
		comboBox.setNullSelectionAllowed(false);
		comboBox.setInvalidAllowed(false);
		comboBox.setContainerDataSource(new BeanServiceLazyLoadingContainer(
				searchService, type));
		comboBox.setFilteringMode(FilteringMode.CONTAINS);

		if (itemCaptionPropertyId != null) {
			comboBox.setItemCaptionPropertyId(itemCaptionPropertyId);
		}

		if (itemIconPropertyId != null) {
			comboBox.setItemIconPropertyId(itemIconPropertyId);
		}

		NativeButton searchButton = new NativeButton();
		searchButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				openSearchDialog();
			}
		});
		searchButton.setWidth("26px");
		searchButton.setIcon(new ThemeResource("icons/SearchButton.png"));

		root.addComponent(comboBox);
		root.addComponent(searchButton);

		// Create the property
		comboBox.setPropertyDataSource(property);

		// Create the bindings
		// valueBinding =
		// dbc.bindValue(VaadinObservables.observeValue(comboBox),
		// PojoObservables.observeValue(property, "value"));

		return root;
	}

	protected void openSearchDialog() {

	}

	@Override
	public Class<? extends BEAN> getType() {
		return type;
	}

	/**
	 * See {@link TextField#setNullRepresentation(String)}
	 * 
	 * @param value
	 */
	public void setNullRepresentation(String value) {
	}

	@Override
	public Property<BEAN> getPropertyDataSource() {
		return property;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setPropertyDataSource(Property newDataSource) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param propertyId
	 * @see com.vaadin.ui.AbstractSelect#setItemCaptionPropertyId(java.lang.Object)
	 */
	public void setItemCaptionPropertyId(Object propertyId) {
		this.itemCaptionPropertyId = propertyId;
		if (comboBox != null) {
			comboBox.setItemCaptionPropertyId(propertyId);
		}
	}

	/**
	 * @param propertyId
	 * @throws IllegalArgumentException
	 * @see com.vaadin.ui.AbstractSelect#setItemIconPropertyId(java.lang.Object)
	 */
	public void setItemIconPropertyId(Object propertyId)
			throws IllegalArgumentException {
		this.itemIconPropertyId = propertyId;

		if (comboBox != null) {
			comboBox.setItemIconPropertyId(propertyId);
		}
	}

	/**
	 * Dispose the field.
	 */
	public void dispose() {
		if (valueBinding != null) {
			valueBinding.dispose();
			valueBinding = null;
		}
	}

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
