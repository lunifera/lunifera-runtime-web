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

import com.vaadin.data.util.NestedMethodProperty;

public interface INestedPropertyAble<BEANTYPE> {

	/**
	 * Adds a nested container property for the container, e.g.
	 * "manager.address.street".
	 * 
	 * All intermediate getters must exist and should return non-null values
	 * when the property value is accessed. If an intermediate getter returns
	 * null, a null value will be returned.
	 * 
	 * @see NestedMethodProperty
	 * 
	 * @param propertyId
	 * @return true if the property was added
	 */
	public abstract boolean addNestedContainerProperty(String propertyId);

}