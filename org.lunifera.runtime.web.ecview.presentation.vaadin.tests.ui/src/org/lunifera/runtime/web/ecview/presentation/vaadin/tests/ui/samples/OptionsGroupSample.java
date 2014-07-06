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

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YLabel;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YOptionsGroup;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSelectionType;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YVerticalLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class OptionsGroupSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public OptionsGroupSample() {
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
	// YOptionsGroup yOptionsGroup2_1 = factory.createOptionsGroup();
	// yOptionsGroup2_1.setLabel("minLength 3");
	// row2.addElement(yOptionsGroup2_1);
	//
	// YOptionsGroup yOptionsGroup2_2 = factory.createOptionsGroup();
	// yOptionsGroup2_2.setLabel("maxLength 10:");
	// row2.addElement(yOptionsGroup2_2);
	//
	// YOptionsGroup yOptionsGroup2_3 = factory.createOptionsGroup();
	// yOptionsGroup2_3.setLabel("regexp: \\d+");
	// row2.addElement(yOptionsGroup2_3);
	// }

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);

		YLabel label = factory.createLabel();
		label.setLabel("SingleSelection");
		row1.addElement(label);
		YOptionsGroup yOptionsGroup1_1 = factory.createOptionsGroup();
		yOptionsGroup1_1.setId("Field1");
		yOptionsGroup1_1.setLabel("Field1");
		yOptionsGroup1_1.setType(String.class);
		YOptionsGroup yOptionsGroup1_2 = factory.createOptionsGroup();
		yOptionsGroup1_2.setId("Field2");
		yOptionsGroup1_2.setLabel("Field2");
		yOptionsGroup1_2.setType(String.class);
		row1.addElement(yOptionsGroup1_1);
		row1.addElement(yOptionsGroup1_2);

		yOptionsGroup1_2.getCollection().add("Sabrina");
		yOptionsGroup1_2.getCollection().add("Klemens");
		yOptionsGroup1_2.getCollection().add("Flo");

		yBindingSet.addBinding(yOptionsGroup1_1.createCollectionEndpoint(),
				yOptionsGroup1_2.createCollectionEndpoint());
		yBindingSet.addBinding(yOptionsGroup1_1.createSelectionEndpoint(),
				yOptionsGroup1_2.createSelectionEndpoint());
	}

	public void row2() {
		// test row 2
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);

		YLabel label = factory.createLabel();
		label.setLabel("Multi Selection");
		row1.addElement(label);
		YOptionsGroup yOptionsGroup1_1 = factory.createOptionsGroup();
		yOptionsGroup1_1.setId("Field3");
		yOptionsGroup1_1.setLabel("Field3");
		yOptionsGroup1_1.setType(String.class);
		yOptionsGroup1_1.setSelectionType(YSelectionType.MULTI);
		YOptionsGroup yOptionsGroup1_2 = factory.createOptionsGroup();
		yOptionsGroup1_2.setId("Field4");
		yOptionsGroup1_2.setLabel("Field4");
		yOptionsGroup1_2.setType(String.class);
		yOptionsGroup1_2.setSelectionType(YSelectionType.MULTI);
		row1.addElement(yOptionsGroup1_1);
		row1.addElement(yOptionsGroup1_2);

		yOptionsGroup1_2.getCollection().add("Sabrina");
		yOptionsGroup1_2.getCollection().add("Klemens");
		yOptionsGroup1_2.getCollection().add("Flo");

		yBindingSet.addBinding(yOptionsGroup1_1.createCollectionEndpoint(),
				yOptionsGroup1_2.createCollectionEndpoint());
		yBindingSet.addBinding(yOptionsGroup1_1.createMultiSelectionEndpoint(),
				yOptionsGroup1_2.createMultiSelectionEndpoint());
	}
}
