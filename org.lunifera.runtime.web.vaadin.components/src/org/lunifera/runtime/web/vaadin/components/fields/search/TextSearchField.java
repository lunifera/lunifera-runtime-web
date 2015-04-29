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
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.IFilterProperty;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.TextFilterProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class TextSearchField extends SearchField<String> {

	private Binding valueBinding;
	private TextFilterProperty filterProperty;

	public TextSearchField(String id, Object propertyId,
			DataBindingContext dbContext) {
		super(id, propertyId, dbContext);

		filterProperty = new TextFilterProperty(getPropertyId(), getLocale());
	}

	@Override
	protected Component initContent() {

		TextField textField = new TextField();
		textField.setImmediate(true);
		textField.setNullRepresentation("");
		textField.setInvalidAllowed(false);
		// Create the property
		ObjectProperty<String> property = new ObjectProperty<String>("",
				String.class, false);
		textField.setPropertyDataSource(property);

		// Create the bindings
		DataBindingContext dbContext = getDbContext();
		valueBinding = dbContext.bindValue(VaadinObservables
				.observeValue(textField), PojoObservables.observeValue(
				filterProperty, IFilterProperty.PROP_FILTER_VALUE));

		return textField;
	}

	@Override
	public Class<? extends String> getType() {
		return String.class;
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
