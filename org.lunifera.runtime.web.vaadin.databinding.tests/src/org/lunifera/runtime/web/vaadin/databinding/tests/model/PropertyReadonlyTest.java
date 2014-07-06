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
package org.lunifera.runtime.web.vaadin.databinding.tests.model;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.CheckBox;

public class PropertyReadonlyTest {

	@Before
	public void setup() {
		VaadinObservables.getRealm(null);
	}

	@Test
	public void test_propertyReadonly() {
		CheckBox property = new CheckBox();

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeReadonly(property));
		Assert.assertEquals(false, value.getValue());

		property.setReadOnly(true);
		Assert.assertEquals(true, value.getValue());
	}

	@Test
	public void test_propertyReadonly_updateFromTarget() {
		CheckBox property = new CheckBox();

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeReadonly(property));
		Assert.assertEquals(false, value.getValue());

		value.setValue(true);
		Assert.assertTrue(property.isReadOnly());
		value.setValue(false);
		Assert.assertFalse(property.isReadOnly());
	}
	
}
