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
