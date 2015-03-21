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
package org.lunifera.runtime.web.vaadin.common.data;

import com.vaadin.data.Container.Filterable;

/**
 * Used to remove the container filter without notification. Notifications are
 * sent by adding filters or by calling {@link #refreshFilters()}.
 */
public interface ILazyRefreshFilterable extends Filterable {

	/**
	 * Will refresh the filter.
	 */
	void refreshFilters();

}