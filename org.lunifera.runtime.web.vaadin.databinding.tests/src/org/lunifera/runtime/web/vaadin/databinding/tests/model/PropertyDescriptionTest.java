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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Table;

public class PropertyDescriptionTest {

	@Before
	public void setup() {
		VaadinObservables.getRealm(null);
	}

	@Test
	public void test_table_propertyDescription() {
		Table table = new Table();

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeDescription(table));

		Assert.assertEquals("", table.getDescription());
		Assert.assertEquals("", value.getValue());

		value.setValue("HUHU");
		Assert.assertEquals("HUHU", table.getDescription());
	}

	@Test
	public void test_table_propertyDescription_updateFromTarget() {
		Table table = new Table();

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeDescription(table));

		Assert.assertEquals("", table.getDescription());
		Assert.assertEquals("", value.getValue());

		table.setDescription("HUHU");
		Assert.assertEquals("", value.getValue());
		Assert.assertEquals("HUHU", table.getDescription());
		dbc.updateTargets();

		Assert.assertEquals("HUHU", value.getValue());
	}
}
