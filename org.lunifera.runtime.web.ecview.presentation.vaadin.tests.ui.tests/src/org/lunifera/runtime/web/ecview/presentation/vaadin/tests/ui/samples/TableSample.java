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

package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples;

import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.extension.model.extension.YHorizontalLayout;
import org.lunifera.ecview.core.extension.model.extension.YLabel;
import org.lunifera.ecview.core.extension.model.extension.YSelectionType;
import org.lunifera.ecview.core.extension.model.extension.YTable;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class TableSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public TableSample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {

		// TODO - fix me FP

		yView = factory.createView();
		yLayout = factory.createVerticalLayout();
		yView.setContent(yLayout);

		yBindingSet = yView.getOrCreateBindingSet();

		row1();

		row2();

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}
	}

	// public void row2() {
	// // test row 2
	// YHorizontalLayout row2 = factory.createHorizontalLayout();
	// yLayout.addElement(row2);
	//
	// YTable yTable2_1 = factory.createTable();
	// yTable2_1.setLabel("minLength 3");
	// row2.addElement(yTable2_1);
	//
	// YTable yTable2_2 = factory.createTable();
	// yTable2_2.setLabel("maxLength 10:");
	// row2.addElement(yTable2_2);
	//
	// YTable yTable2_3 = factory.createTable();
	// yTable2_3.setLabel("regexp: \\d+");
	// row2.addElement(yTable2_3);
	// }

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);

		YLabel label = factory.createLabel();
		label.setLabel("SingleSelection");
		row1.addElement(label);
		YTable yTable1_1 = factory.createTable();
		yTable1_1.setId("Field1");
		yTable1_1.setLabel("Field1");
		yTable1_1.setType(Bean.class);
		YTable yTable1_2 = factory.createTable();
		yTable1_2.setId("Field2");
		yTable1_2.setLabel("Field2");
		yTable1_2.setType(Bean.class);
		row1.addElement(yTable1_1);
		row1.addElement(yTable1_2);

		yTable1_2.getCollection().add(new Bean("Sabrina"));
		yTable1_2.getCollection().add(new Bean("Klemens"));
		yTable1_2.getCollection().add(new Bean("Flo"));

		yBindingSet.addBinding(yTable1_1.createCollectionEndpoint(),
				yTable1_2.createCollectionEndpoint());
		yBindingSet.addBinding(yTable1_1.createSelectionEndpoint(),
				yTable1_2.createSelectionEndpoint());
	}

	public void row2() {
		// test row 2
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);

		YLabel label = factory.createLabel();
		label.setLabel("Multi Selection");
		row1.addElement(label);
		YTable yTable1_1 = factory.createTable();
		yTable1_1.setId("Field3");
		yTable1_1.setLabel("Field3");
		yTable1_1.setType(Bean.class);
		yTable1_1.setSelectionType(YSelectionType.MULTI);
		YTable yTable1_2 = factory.createTable();
		yTable1_2.setId("Field4");
		yTable1_2.setLabel("Field4");
		yTable1_2.setType(Bean.class);
		yTable1_2.setSelectionType(YSelectionType.MULTI);
		row1.addElement(yTable1_1);
		row1.addElement(yTable1_2);

		yTable1_2.getCollection().add(new Bean("Sabrina"));
		yTable1_2.getCollection().add(new Bean("Klemens"));
		yTable1_2.getCollection().add(new Bean("Flo"));

		yBindingSet.addBinding(yTable1_1.createCollectionEndpoint(),
				yTable1_2.createCollectionEndpoint());
		yBindingSet.addBinding(yTable1_1.createMultiSelectionEndpoint(),
				yTable1_2.createMultiSelectionEndpoint());
	}

	public static final class Bean {
		private String name;

		public Bean(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
