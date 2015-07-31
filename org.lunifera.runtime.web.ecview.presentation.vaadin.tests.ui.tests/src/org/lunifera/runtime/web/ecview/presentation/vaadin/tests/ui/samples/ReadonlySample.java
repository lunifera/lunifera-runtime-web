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

import java.util.Date;
import java.util.Locale;

import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.model.binding.YBindingSet;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.extension.model.datatypes.ExtDatatypesFactory;
import org.lunifera.ecview.core.extension.model.datatypes.YDateTimeDatatype;
import org.lunifera.ecview.core.extension.model.datatypes.YDateTimeFormat;
import org.lunifera.ecview.core.extension.model.extension.YButton;
import org.lunifera.ecview.core.extension.model.extension.YDateTime;
import org.lunifera.ecview.core.extension.model.extension.YHorizontalLayout;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.listener.YButtonClickListener;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class ReadonlySample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	private IViewContext context;

	public ReadonlySample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {

		Locale.setDefault(Locale.GERMANY);

		yView = factory.createView();
		yLayout = factory.createVerticalLayout();
		yView.setContent(yLayout);

		yBindingSet = yView.getOrCreateBindingSet();

		row2();

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			context = renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}
	}

	public void row2() {
		// test row 2
		YHorizontalLayout row2 = factory.createHorizontalLayout();
		yLayout.addElement(row2);

		final YTextField yText = factory.createTextField();
		yText.setLabel("text");
		yText.setValue("Huhu");
		row2.addElement(yText);

		final YDateTime yDate = factory.createDateTime();
		yDate.setLabel("time");
		yDate.setValue(new Date());
		row2.addElement(yDate);
		YDateTimeDatatype yDt2_1 = createDateTimeDatatype();
		yDt2_1.setFormat(YDateTimeFormat.TIME);
		yDate.setDatatype(yDt2_1);

		YButton yLocaleSwitcher = factory.createButton();
		yLocaleSwitcher.setLabel("readonly");
		row2.addElement(yLocaleSwitcher);
		yLocaleSwitcher.addClickListener(new YButtonClickListener() {
			@Override
			public void clicked(YButton yButton) {
				yText.setEditable(!yText.isEditable());
				yDate.setEditable(!yDate.isEditable());
			}
		});
	}

	protected YDateTimeDatatype createDateTimeDatatype() {
		return ExtDatatypesFactory.eINSTANCE.createYDateTimeDatatype();
	}

}
