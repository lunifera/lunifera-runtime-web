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
import org.lunifera.runtime.web.vaadin.common.IFilterProvider;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.BooleanFilterProperty;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilterProperty;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.TextFilterProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class TextSearchField extends
		SearchField<BooleanFilterProperty.OptionBean> {

	private Binding valueBinding;
	private TextFilterProperty filterProperty;

	public TextSearchField(String id, Object propertyId,
			DataBindingContext dbContext) {
		super(id, propertyId, dbContext);
		
		filterProperty = new TextFilterProperty(this, getPropertyId(), getLocale());
	}
 
	@Override
	protected Component initContent() { 

		TextField textField = new TextField();
		textField.setImmediate(true);
		textField.setNullRepresentation("");

		// Create the property
		ObjectProperty<String> property = new ObjectProperty<String>("",
				String.class, false);
		textField.setPropertyDataSource(property);

		// Create the bindings
		DataBindingContext dbContext = getDbContext();
		valueBinding = dbContext.bindValue(VaadinObservables
				.observeValue(textField), PojoObservables.observeValue(filterProperty,
				IFilterProperty.PROP_FILTER_VALUE));

		return textField;
	}

	@Override
	public Class<? extends BooleanFilterProperty.OptionBean> getType() {
		return BooleanFilterProperty.OptionBean.class;
	}

	/**
	 * See {@link TextField#setNullRepresentation(String)}
	 * 
	 * @param value
	 */
	public void setNullRepresentation(String value) {
		((TextField) getContent()).setNullRepresentation(value);
	}
	
	@Override
	public Filter getFilter() {
		return filterProperty.getFilter();
	}
	
	/**
	 * @param filterProvider
	 *            the filterProvider to set
	 */
	public void setFilterProvider(IFilterProvider filterProvider) {
		filterProperty.setFilterProvider(filterProvider);
	}

	/**
	 * Dispose the field.
	 */
	public void dispose() {

		filterProperty = null;

		if (valueBinding != null) {
			valueBinding.dispose();
			valueBinding = null;
		}
	}
}
