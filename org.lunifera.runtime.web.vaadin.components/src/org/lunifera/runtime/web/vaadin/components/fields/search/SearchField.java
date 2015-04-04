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

import org.eclipse.core.databinding.DataBindingContext;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public abstract class SearchField<C> extends CustomField<C> {

	private final String id;
	private final Object propertyId;
	private DataBindingContext dbContext;

	public SearchField(String id, Object propertyId,
			DataBindingContext dbContext) {
		this.id = id;
		this.propertyId = propertyId;
		this.dbContext = dbContext;
	}
	
	/**
	 * Returns the vaadin filter for the property.
	 * @return
	 */
	public abstract Filter getFilter();

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the propertyId
	 */
	public Object getPropertyId() {
		return propertyId;
	}

	/**
	 * @return the dbContext
	 */
	public DataBindingContext getDbContext() {
		return dbContext;
	}

	/**
	 * @param dbContext
	 *            the dbContext to set
	 */
	public void setDbContext(DataBindingContext dbContext) {
		this.dbContext = dbContext;
	}

}
