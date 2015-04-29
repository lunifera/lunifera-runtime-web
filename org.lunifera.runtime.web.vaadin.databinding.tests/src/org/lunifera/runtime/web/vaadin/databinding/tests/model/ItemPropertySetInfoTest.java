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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.properties.PropertyInfo;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class ItemPropertySetInfoTest {

	@Before
	public void setup() {
		VaadinObservables.getRealm(null);
	}

	@SuppressWarnings("unchecked")
	// @Test
	public void test_itemPropertySet() {
		PropertysetItem item = new PropertysetItem();
		WritableValue value = new WritableValue();

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value,
				VaadinObservables.observeItemPropertySetInfoValue(item));

		item.addItemProperty("1", new ObjectProperty<String>("1"));
		Collection<PropertyInfo> infos = (Collection<PropertyInfo>) value
				.getValue();
		Assert.assertEquals(1, infos.size());

		item.addItemProperty("2", new ObjectProperty<String>("2"));
		infos = (Collection<PropertyInfo>) value.getValue();
		Assert.assertEquals(2, infos.size());
	}

	// @Test
	public void test_itemPropertySet_targetToModel() {
		PropertysetItem item = new PropertysetItem();
		WritableValue value = new WritableValue();

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value,
				VaadinObservables.observeItemPropertySetInfoValue(item));

		List<PropertyInfo> props = new ArrayList<PropertyInfo>();
		props.add(new PropertyInfo("1", new ObjectProperty<String>("1")));
		value.setValue(props);
		Assert.assertEquals(1, item.getItemPropertyIds().size());

		props = new ArrayList<PropertyInfo>(props);
		props.add(new PropertyInfo("2", new ObjectProperty<String>("2")));
		value.setValue(props);
		Assert.assertEquals(2, item.getItemPropertyIds().size());
	}

	@Test
	public void test_itemPropertySet_2Items() {
		PropertysetItem item1 = new PropertysetItem();
		PropertysetItem item2 = new PropertysetItem();

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(VaadinObservables.observeItemPropertySetInfoValue(item1),
				VaadinObservables.observeItemPropertySetInfoValue(item2));

		item1.addItemProperty("1", new ObjectProperty<String>("1"));
		Assert.assertEquals(item1.getItemPropertyIds().size(), item2
				.getItemPropertyIds().size());

		item2.addItemProperty("2", new ObjectProperty<String>("2"));
		Assert.assertEquals(item1.getItemPropertyIds().size(), item2
				.getItemPropertyIds().size());

		item1.removeItemProperty("1");
		Assert.assertEquals(item1.getItemPropertyIds().size(), item2
				.getItemPropertyIds().size());

		item2.removeItemProperty("2");
		Assert.assertEquals(item1.getItemPropertyIds().size(), item2
				.getItemPropertyIds().size());

	}

}
