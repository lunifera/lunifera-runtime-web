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

public class ItemPropertySetTest {

	@Before
	public void setup() {
		VaadinObservables.getRealm(null);
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
