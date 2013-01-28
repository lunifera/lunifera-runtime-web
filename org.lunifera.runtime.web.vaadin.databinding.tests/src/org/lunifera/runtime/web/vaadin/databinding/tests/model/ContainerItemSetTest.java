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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;

public class ContainerItemSetTest {

	@Before
	public void setup() {
		VaadinObservables.getRealm(null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_containerItemSet() {
		// Binded
		// VaadinObservables.observeItemSet(Container.ItemSetChangeNotifier)
		// Am besten IndexedContainer verwenden und manuell item hinzuf��gen
		IndexedContainer container = new IndexedContainer();
		WritableValue value = new WritableValue();
		Assert.assertNotNull(value);

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value,
				VaadinObservables.observeContainerItemSetValue(container));

		Collection<Object> itemIds = (Collection<Object>) value.getValue();
		Assert.assertEquals(0, itemIds.size());
		container.addItem("1");
		itemIds = (Collection<Object>) value.getValue();
		Assert.assertEquals(1, itemIds.size());
	}
	
	@Test
	public void test_targetToModel() {
		// Binded
		// VaadinObservables.observeItemSet(Container.ItemSetChangeNotifier)
		// Am besten IndexedContainer verwenden und manuell item hinzuf��gen
		IndexedContainer container = new IndexedContainer();
		WritableValue value = new WritableValue();
		Assert.assertNotNull(value);
		
		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(value,
				VaadinObservables.observeContainerItemSetValue(container));
		
		Assert.assertEquals(0, container.size());
		Collection<Object> itemIds = new ArrayList<Object>();
		itemIds.add("1");
		value.setValue(itemIds);
		Assert.assertEquals(1, container.size());
	}

}
