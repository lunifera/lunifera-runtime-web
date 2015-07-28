/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributor:
 * 		Florian Pirchner - initial implementation
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.databinding.tests.model;

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class ItemPropertySetTests {

	@Before
	public void setup() {
		DefaultUI.setCurrent(new DefaultUI());
		VaadinObservables.getRealm(DefaultUI.getCurrent());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_itemPropertySet() {
		// Binded
		// VaadinObservables.observePropertySet(Item.PropertySetChangeNotifier)
		// Am besten ProperySetItem verwenden und manuell properties hinzuf��gen
		PropertysetItem property = new PropertysetItem();
		WritableValue value = new WritableValue();
		Assert.assertNotNull(value);

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeItemPropertySetValue(property));
		
		property.addItemProperty("1", new ObjectProperty<String>("1"));
		Collection<PropertysetItem> properties = (Collection<PropertysetItem>) value.getValue();
		Assert.assertEquals(1, properties.size());
	}
}
