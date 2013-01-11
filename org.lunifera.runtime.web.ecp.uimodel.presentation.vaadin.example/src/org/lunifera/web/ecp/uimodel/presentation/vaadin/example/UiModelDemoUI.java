/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 * 
 *******************************************************************************/
package org.lunifera.web.ecp.uimodel.presentation.vaadin.example;

import org.lunifera.web.vaadin.common.OSGiUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Specify the class name after the factory name.
 */
@Theme(Reindeer.THEME_NAME)
public class UiModelDemoUI extends OSGiUI {

	private static final Logger logger = LoggerFactory
			.getLogger(UiModelDemoUI.class);

	private static final long serialVersionUID = 1L;

	@Override
	public void init(VaadinRequest request) {
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSizeFull();
		
		TabSheet sheet = new TabSheet();
		sheet.setSizeFull();
		content.addComponent(sheet);
		setContent(content);

		// add all examples
		//
		sheet.addTab(new GridLayoutFactory().createComponent(), "GridLayout");
		sheet.addTab(new HorizontalLayoutFactory().createComponent(), "HorizontalLayout");
		sheet.addTab(new VerticalLayoutFactory().createComponent(), "VerticalLayout");
	}
}
