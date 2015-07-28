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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.TextField;

public class PropertyValueTests {

	@Before
	public void setup() {
		DefaultUI.setCurrent(new DefaultUI());
		VaadinObservables.getRealm(DefaultUI.getCurrent());
	}
	
	@Test
	public void test_propertyValue() {
		VaadinObservables.getRealm(DefaultUI.getCurrent());
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
	public void test_propertyValue_updateFromTarget() {
		VaadinObservables.getRealm(DefaultUI.getCurrent());
		ObjectProperty<String> property = new ObjectProperty<String>("Huhu");

		WritableValue value = new WritableValue();
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value, VaadinObservables.observeValue(property));
		Assert.assertEquals("Huhu", property.getValue());

		value.setValue("Hahaha");
		Assert.assertEquals("Hahaha", property.getValue());
	}
	
	@Test
	public void test_propertyValue_updateModelToTarget() {
		TextField text = new TextField();

		WritableValue value = new WritableValue();
		Assert.assertNull(value.getValue());

		DataBindingContext dbc = new DataBindingContext();
		Binding binding = dbc.bindValue(value, VaadinObservables.observeInputPrompt(text));
		Assert.assertNull(text.getInputPrompt());

		text.setInputPrompt("Huhu");
		Assert.assertNull(value.getValue());
		binding.updateModelToTarget();
		
		Assert.assertEquals("Huhu", value.getValue());
	}

}
