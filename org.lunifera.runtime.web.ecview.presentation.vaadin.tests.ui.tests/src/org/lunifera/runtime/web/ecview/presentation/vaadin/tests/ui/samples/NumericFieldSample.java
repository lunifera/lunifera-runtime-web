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
import org.lunifera.ecview.core.extension.model.datatypes.YNumericDatatype;
import org.lunifera.ecview.core.extension.model.extension.YHorizontalLayout;
import org.lunifera.ecview.core.extension.model.extension.YNumericField;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class NumericFieldSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public NumericFieldSample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {

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
	
	public void row2() {
		// test row 2
		YHorizontalLayout row2 = factory.createHorizontalLayout();
		yLayout.addElement(row2);

		YNumericField yText2_1 = factory.createNumericField();
		yText2_1.setLabel("grouping false");
		row2.addElement(yText2_1);
		YNumericDatatype yDt2_2 = factory.createNumericDatatype();
		yDt2_2.setGrouping(false);
		yText2_1.setDatatype(yDt2_2);

		YNumericField yText2_2 = factory.createNumericField();
		yText2_2.setLabel("grouping true");
		row2.addElement(yText2_2);
		YNumericDatatype yDt2_1 = factory.createNumericDatatype();
		yDt2_1.setGrouping(true);
		yText2_2.setDatatype(yDt2_1);

		YNumericField yText2_3 = factory.createNumericField();
		yText2_3.setLabel("mark negative");
		row2.addElement(yText2_3);
		YNumericDatatype yDt2_3 = factory.createNumericDatatype();
		yDt2_3.setMarkNegative(true);
		yText2_3.setDatatype(yDt2_3);
	}

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);
		YNumericField yText1_1 = factory.createNumericField();
		yText1_1.setLabel("Field1");
		YNumericField yText1_2 = factory.createNumericField();
		yText1_2.setLabel("Field2");
		row1.addElement(yText1_1);
		row1.addElement(yText1_2);

		yBindingSet.addBinding(yText1_1.createValueEndpoint(),
				yText1_2.createValueEndpoint());
	}
}
