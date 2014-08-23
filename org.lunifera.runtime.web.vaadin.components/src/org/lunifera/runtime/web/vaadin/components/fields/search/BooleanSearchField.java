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
package org.lunifera.runtime.web.vaadin.components.fields.search;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.BooleanFilter;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.BooleanFilter.OptionBean;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilter;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class BooleanSearchField extends SearchField<BooleanFilter.OptionBean> {

	private Binding selectionBinding;
	private Binding collectionBinding;
	private BooleanFilter filter;

	public BooleanSearchField(String id, DataBindingContext dbContext) {
		super(id, dbContext);
	}

	@Override
	protected Component initContent() {

		filter = new BooleanFilter(getLocale());

		ComboBox combo = new ComboBox();
		combo.setImmediate(true);
		combo.setNullSelectionAllowed(false);

		// Create the container
		BeanItemContainer<BooleanFilter.OptionBean> container = new BeanItemContainer<BooleanFilter.OptionBean>(
				BooleanFilter.OptionBean.class);
		container.addAll(filter.getOptions());
		combo.setContainerDataSource(container);

		// Create the property
		ObjectProperty<BooleanFilter.OptionBean> property = new ObjectProperty<BooleanFilter.OptionBean>(
				filter.getDefaultOption(), BooleanFilter.OptionBean.class,
				false);
		combo.setPropertyDataSource(property);

		// Create the bindings
		DataBindingContext dbContext = getDbContext();
		selectionBinding = dbContext.bindValue(
				VaadinObservables.observeValue(combo),
				PojoObservables.observeValue(filter, IFilter.PROP_SELECTION));
		collectionBinding = dbContext.bindList(VaadinObservables
				.observeContainerItemSetContents(combo, OptionBean.class),
				PojoObservables.observeList(filter, IFilter.PROP_OPTIONS));

		return combo;
	}

	@Override
	public Class<? extends BooleanFilter.OptionBean> getType() {
		return BooleanFilter.OptionBean.class;
	}

	/**
	 * Dispose the field.
	 */
	public void dispose() {

		filter = null;

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
