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
import org.lunifera.ecview.core.extension.model.datatypes.YTextDatatype;
import org.lunifera.ecview.core.extension.model.extension.YCheckBox;
import org.lunifera.ecview.core.extension.model.extension.YHorizontalLayout;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class TextFieldSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public TextFieldSample() {
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

	public void row3() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);
		YCheckBox yCheckbox = factory.createCheckBox();
		yCheckbox.setLabel("Click for readonly");
		YTextField yText = factory.createTextField();
		yText.setLabel("Becomes read only");
		row1.addElement(yCheckbox);
		row1.addElement(yText);

		// yText.setEditable(value);

		yBindingSet.addBinding(yText.createValueEndpoint(),
				yText.createValueEndpoint());
	}

	public void row2() {
		// test row 2
		YHorizontalLayout row2 = factory.createHorizontalLayout();
		yLayout.addElement(row2);

		YTextField yText2_1 = factory.createTextField();
		yText2_1.setLabel("minLength 3");
		yText2_1.setCssID("minLength");
		row2.addElement(yText2_1);
		YTextDatatype yDt2_2 = factory.createTextDatatype();
		yDt2_2.setMinLength(3);
		yText2_1.setDatatype(yDt2_2);

		YTextField yText2_2 = factory.createTextField();
		yText2_2.setLabel("maxLength 10:");
		yText2_2.setCssID("maxLength");
		row2.addElement(yText2_2);
		YTextDatatype yDt2_1 = factory.createTextDatatype();
		yDt2_1.setMaxLength(10);
		yText2_2.setDatatype(yDt2_1);

		YTextField yText2_3 = factory.createTextField();
		yText2_3.setLabel("regexp: \\d+");
		yText2_3.setCssID("regexp");
		row2.addElement(yText2_3);
		YTextDatatype yDt2_3 = factory.createTextDatatype();
		yDt2_3.setRegExpression("\\d+");
		yText2_3.setDatatype(yDt2_3);
	}

	public void row1() {
		// test row 1
		YHorizontalLayout row1 = factory.createHorizontalLayout();
		yLayout.addElement(row1);
		YTextField yText1_1 = factory.createTextField();
		yText1_1.setCssID("field1");
		yText1_1.setLabel("A (binds to B):");
		YTextField yText1_2 = factory.createTextField();
		yText1_2.setLabel("B (binds to A):");
		yText1_2.setCssID("field2");
		row1.addElement(yText1_1);
		row1.addElement(yText1_2);

		yBindingSet.addBinding(yText1_1.createValueEndpoint(),
				yText1_2.createValueEndpoint());
	}
}
