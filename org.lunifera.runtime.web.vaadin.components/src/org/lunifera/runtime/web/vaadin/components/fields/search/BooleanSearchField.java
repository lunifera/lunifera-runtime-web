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
package org.lunifera.runtime.web.vaadin.components.fields.search;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.BooleanFilterProperty;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.BooleanFilterProperty.OptionBean;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilterProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class BooleanSearchField extends
		SearchField<BooleanFilterProperty.OptionBean> {

	private Binding selectionBinding;
	private Binding collectionBinding;
	private BooleanFilterProperty filterProperty;

	public BooleanSearchField(String id, Object propertyId,
			DataBindingContext dbContext) {
		super(id, propertyId, dbContext);

		filterProperty = new BooleanFilterProperty(getPropertyId(), getLocale());
	}

	@Override
	protected Component initContent() {

		ComboBox combo = new ComboBox();
		combo.setImmediate(true);
		combo.setNullSelectionAllowed(false);
		combo.setItemCaptionPropertyId("description");

		// Create the container
		BeanItemContainer<BooleanFilterProperty.OptionBean> container = new BeanItemContainer<BooleanFilterProperty.OptionBean>(
				BooleanFilterProperty.OptionBean.class);
		container.addAll(filterProperty.getOptions());
		combo.setContainerDataSource(container);

		// Create the property
		ObjectProperty<BooleanFilterProperty.OptionBean> property = new ObjectProperty<BooleanFilterProperty.OptionBean>(
				filterProperty.getDefaultOption(),
				BooleanFilterProperty.OptionBean.class, false);
		combo.setPropertyDataSource(property);

		// Create the bindings
		DataBindingContext dbContext = getDbContext();
		selectionBinding = dbContext.bindValue(VaadinObservables
				.observeValue(combo), PojoObservables.observeValue(
				filterProperty, IFilterProperty.PROP_SELECTION));
		collectionBinding = dbContext.bindList(VaadinObservables
				.observeContainerItemSetContents(combo, OptionBean.class),
				PojoObservables.observeList(filterProperty,
						IFilterProperty.PROP_OPTIONS));

		return combo;
	}

	@Override
	public Class<? extends BooleanFilterProperty.OptionBean> getType() {
		return BooleanFilterProperty.OptionBean.class;
	}

	@Override
	public Filter getFilter() {
		return filterProperty.getFilter();
	}

	/**
	 * Dispose the field.
	 */
	public void dispose() {

		filterProperty = null;

		if (selectionBinding != null) {
			selectionBinding.dispose();
			selectionBinding = null;
		}

		if (collectionBinding != null) {
			collectionBinding.dispose();
			collectionBinding = null;
		}
	}

}
