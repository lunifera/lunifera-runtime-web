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

import com.vaadin.data.Container.Filter;

public interface IFilterProperty {

	String PROP_OPTIONS = "options";
	String PROP_SELECTION = "selection";
	String PROP_FILTER_VALUE = "value";
	
	/**
	 * Returns the vaadin filter for the property.
	 * @return
	 */
	Filter getFilter();
	
	/**
	 * Returns the property id for the search field.
	 * @return
	 */
	Object getPropertyId();
}
