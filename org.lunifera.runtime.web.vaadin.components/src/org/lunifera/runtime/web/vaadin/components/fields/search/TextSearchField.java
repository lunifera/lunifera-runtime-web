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
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilter;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.TextFilter;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class TextSearchField extends SearchField<BooleanFilter.OptionBean> {

	private Binding valueBinding;
	private TextFilter filter;

	public TextSearchField(String id, DataBindingContext dbContext) {
		super(id, dbContext);
	} 

	@Override
	protected Component initContent() {

		filter = new TextFilter(getLocale());

		TextField textField = new TextField();
		textField.setImmediate(true);

		// Create the property
		ObjectProperty<String> property = new ObjectProperty<String>("",
				String.class, false);
		textField.setPropertyDataSource(property);

		// Create the bindings
		DataBindingContext dbContext = getDbContext();
		valueBinding = dbContext
				.bindValue(VaadinObservables.observeValue(textField),
						PojoObservables.observeValue(filter,
								IFilter.PROP_FILTER_VALUE));

		return textField;
	}

	@Override
	public Class<? extends BooleanFilter.OptionBean> getType() {
		return BooleanFilter.OptionBean.class;
	}

	/**
	 * See {@link TextField#setNullRepresentation(String)}
	 * 
	 * @param value
	 */
	public void setNullRepresentation(String value) {
		((TextField) getContent()).setNullRepresentation(value);
	}

	/**
	 * Dispose the field.
	 */
	public void dispose() {

		filter = null;

		if (valueBinding != null) {
			valueBinding.dispose();
			valueBinding = null;
		}
	}
}
