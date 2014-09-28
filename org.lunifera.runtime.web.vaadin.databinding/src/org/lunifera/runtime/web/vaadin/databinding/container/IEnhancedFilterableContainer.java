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
package org.lunifera.runtime.web.vaadin.databinding.container;

import java.util.List;

import com.vaadin.data.Container;

/**
 * ECView databinding needs to access ALL item ids. Filters and sort orders must
 * not change the underlying domain model.
 *
 * @param <ITEM>
 */
public interface IEnhancedFilterableContainer<ITEM> extends
		Container.Filterable {

	/**
	 * Returns a collection of unfiltered item ides.
	 */
	List<ITEM> getUnfilteredItemIds();

	/**
	 * Returns the size of the unfiltered item ids.
	 * 
	 * @return
	 */
	int sizeUnfiltered();

}
