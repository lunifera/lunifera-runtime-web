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

import org.eclipse.core.databinding.DataBindingContext;

import com.vaadin.ui.CustomField;

@SuppressWarnings("serial")
public abstract class SearchField<C> extends CustomField<C> {

	private String id;
	private DataBindingContext dbContext;

	public SearchField(String id, DataBindingContext dbContext) {
		this.id = id;
		this.dbContext = dbContext;
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
