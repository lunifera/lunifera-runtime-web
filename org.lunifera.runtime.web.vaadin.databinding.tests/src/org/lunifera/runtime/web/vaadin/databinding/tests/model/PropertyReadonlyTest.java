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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.ObjectProperty;

public class PropertyReadonlyTest {

	@Test
	public void test_propertyReadonly() {
		VaadinObservables.getRealm(null);
		ObjectProperty<String> property = new ObjectProperty<String>("");

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeValue(property));
		Assert.assertEquals("", value.getValue());

		property.setValue("Huhu");
		Assert.assertEquals("Huhu", value.getValue());
	}

	@Test
	public void test_propertyReadonly_updateFromTarget() {
		VaadinObservables.getRealm(null);
		ObjectProperty<String> property = new ObjectProperty<String>("Huhu");

		WritableValue value = new WritableValue();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeValue(property));
		Assert.assertEquals("Huhu", property.getValue());

		value.setValue("Hahaha");
		Assert.assertEquals("Hahaha", property.getValue());
	}
}
