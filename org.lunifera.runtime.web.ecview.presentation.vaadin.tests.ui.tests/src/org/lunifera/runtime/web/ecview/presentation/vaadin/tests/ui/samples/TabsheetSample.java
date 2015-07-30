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
import org.lunifera.ecview.core.extension.model.extension.YTab;
import org.lunifera.ecview.core.extension.model.extension.YTabSheet;
import org.lunifera.ecview.core.extension.model.extension.YTextField;
import org.lunifera.ecview.core.extension.model.extension.YVerticalLayout;
import org.lunifera.ecview.core.extension.model.extension.util.SimpleExtensionModelFactory;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class TabsheetSample extends CustomComponent {

	private final SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();

	private CssLayout layout;

	private YView yView;

	private YBindingSet yBindingSet;

	private YVerticalLayout yLayout;

	public TabsheetSample() {
		layout = new CssLayout();
		setCompositionRoot(layout);

		init();
	}

	protected void init() {

		yView = factory.createView();
		yLayout = factory.createVerticalLayout();
		yView.setContent(yLayout);

		YTabSheet yTabsheet = factory.createTabSheet();
		yLayout.getElements().add(yTabsheet);
		

		tab1(yTabsheet);

		tab2(yTabsheet);

		// render now, fill in values later
		// to avoid overwriting values with bindings to empty fields
		VaadinRenderer renderer = new VaadinRenderer();
		try {
			renderer.render(layout, yView, null);
		} catch (ContextException e) {
			layout.addComponent(new Label(e.toString()));
		}
	}
	
	public void tab1(YTabSheet yTabsheet) {
		YTab yTab1 = factory.createTab();
		yTab1.setLabel("Tab 1");
		
		YVerticalLayout yTab1Content = factory.createVerticalLayout();
		yTab1.setEmbeddable(yTab1Content);
		
		YTextField yTextfield1 = factory.createTextField();
		yTextfield1.setLabel("Huhu");
		yTextfield1.setValue("Huhu");
		yTab1Content.addElement(yTextfield1);
		
		yTabsheet.getTabs().add(yTab1);
	}

	public void tab2(YTabSheet yTabsheet) {
		YTab yTab2 = factory.createTab();
		yTab2.setLabel("Tab 2");
		
		YVerticalLayout yTab2Content = factory.createVerticalLayout();
		yTab2.setEmbeddable(yTab2Content);
		
		YTextField yTextfield2 = factory.createTextField();
		yTextfield2.setLabel("Haha");
		yTextfield2.setValue("Haha");
		yTab2Content.addElement(yTextfield2);
		
		yTabsheet.getTabs().add(yTab2);
	}
}
