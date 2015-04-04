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

package org.lunifera.runtime.web.vaadin.databinding.properties;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

public class Util {

	/**
	 * Returns the property datasource if available, the source as a property or
	 * null.
	 * 
	 * @param source
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Property<Object> getProperty(Object source) {
		Property<Object> result = null;
		if (source instanceof Property.Viewer) {
			result = ((Property.Viewer) source).getPropertyDataSource();
		}

		if (result == null && source instanceof Property<?>) {
			result = (Property<Object>) source;
		}
		return result;
	}

	/**
	 * Returns the container datasource for the given element.
	 * 
	 * @param source
	 * @return
	 */
	public static Container getContainer(Object source) {
		// if source is AbstractSelect, then access the container by
		// #getContainerDataSource
		if (source instanceof Container && !(source instanceof AbstractSelect)) {
			return (Container) source;
		}
		Container result = null;
		if (source instanceof Container.Viewer) {
			result = ((Container.Viewer) source).getContainerDataSource();
		}

		return result;
	}

}
