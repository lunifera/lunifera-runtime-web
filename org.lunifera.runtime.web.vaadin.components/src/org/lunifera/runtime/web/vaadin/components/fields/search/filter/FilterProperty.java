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
package org.lunifera.runtime.web.vaadin.components.fields.search.filter;

import java.util.Locale;

import org.lunifera.runtime.web.vaadin.common.IFilterProvider;

import com.vaadin.ui.Component;

public abstract class FilterProperty implements IFilterProperty {

	private Component filterField;
	private Object propertyId;
	private Locale locale;
	private IFilterProvider filterProvider;

	public FilterProperty(Component filterField, Object propertyId,
			Locale locale) {
		this.filterField = filterField;
		this.propertyId = propertyId;
		this.locale = locale;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Object getPropertyId() {
		return propertyId;
	}

	/**
	 * @return the filterProvider
	 */
	public IFilterProvider getFilterProvider() {
		return filterProvider;
	}

	/**
	 * @param filterProvider
	 *            the filterProvider to set
	 */
	public void setFilterProvider(IFilterProvider filterProvider) {
		this.filterProvider = filterProvider;
	}

}
