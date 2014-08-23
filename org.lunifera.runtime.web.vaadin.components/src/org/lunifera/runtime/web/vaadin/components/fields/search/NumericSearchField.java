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

import java.text.DecimalFormatSymbols;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilter;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.TextFilter;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class NumericSearchField extends SearchField<String> {

	private Binding valueBinding;
	private TextFilter filter;

	public NumericSearchField(String id, DataBindingContext dbContext) {
		super(id, dbContext);
	}

	@Override
	protected Component initContent() {

		filter = new TextFilter(getLocale());

		TextField textField = new TextField();
		textField.setImmediate(true);
		textField.addValidator(new RegexpValidator(getRegexp(), "Not a valid format!"));

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

	/**
	 * Creates the reg expression for the values allowed.
	 * 
	 * @return
	 */
	private String getRegexp() {
		DecimalFormatSymbols symbols = getLocale() != null ? DecimalFormatSymbols.getInstance(getLocale()) : DecimalFormatSymbols.getInstance();
		return String.format("(>|<|>=|<=|\\!=)?( )*\\d+(\\%s\\d{1,4})?",
				symbols.getDecimalSeparator());
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
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

	public void setNullRepresentation(String value) {
		((TextField) getContent()).setNullRepresentation(value);
	}
}
