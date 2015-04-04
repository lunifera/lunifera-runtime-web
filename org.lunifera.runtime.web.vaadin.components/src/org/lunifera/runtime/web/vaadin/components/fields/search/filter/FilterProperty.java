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
package org.lunifera.runtime.web.vaadin.components.fields.search.filter;

import java.util.Locale;

public abstract class FilterProperty implements IFilterProperty {

	private Object propertyId;
	private Locale locale;

	public FilterProperty(Object propertyId, Locale locale) {
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

}
