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
import org.eclipse.core.databinding.observable.list.WritableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.data.util.IndexedContainer;

public class ContainerItemSetTest {

	@Before
	public void setup() {
		DefaultUI.setCurrent(new DefaultUI());
		VaadinObservables.getRealm(DefaultUI.getCurrent());
	}

	@Test
	public void test_containerItemSet() {
		// Binded
		// VaadinObservables.observeItemSet(Container.ItemSetChangeNotifier)
		// Am besten IndexedContainer verwenden und manuell item hinzuf��gen
		IndexedContainer container = new IndexedContainer();
		WritableList itemIds = new WritableList();

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindList(itemIds, VaadinObservables
				.observeContainerItemSetContents(container, String.class));

		Assert.assertEquals(0, itemIds.size());
		container.addItem("1");
		Assert.assertEquals(1, itemIds.size());
	}

	@Test
	public void test_targetToModel() {
		// Binded
		// VaadinObservables.observeItemSet(Container.ItemSetChangeNotifier)
		// Am besten IndexedContainer verwenden und manuell item hinzuf��gen
		IndexedContainer container = new IndexedContainer();
		WritableList itemIds = new WritableList();

		DataBindingContext dbc = new DataBindingContext();
		dbc.bindList(itemIds, VaadinObservables
				.observeContainerItemSetContents(container, String.class));

		Assert.assertEquals(0, container.size());
		itemIds.add("1");
		Assert.assertEquals(1, container.size());
	}

}
