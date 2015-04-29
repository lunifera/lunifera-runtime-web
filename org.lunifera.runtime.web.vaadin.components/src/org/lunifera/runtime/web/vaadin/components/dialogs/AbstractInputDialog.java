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
package org.lunifera.runtime.web.vaadin.components.dialogs;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class AbstractInputDialog extends AbstractDialog {

	protected AbstractInputDialog(DialogConfig config, Option... options) {
		super(config, options);
	}

	/**
	 * Prepare the main layout.
	 */
	protected void prepareLayout() {
		window = new Window();
		window.setModal(true);
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		window.setContent(mainLayout);

		customArea = new FormLayout();
		((FormLayout) customArea).setSpacing(true);
		customArea.setSizeFull();
		mainLayout.addComponent(customArea);
		mainLayout.setExpandRatio(customArea, 1.0f);

		fillForm((FormLayout) customArea);

		optionsArea = new HorizontalLayout();
		optionsArea.setSpacing(true);
		mainLayout.addComponent(optionsArea);
	}

	/**
	 * Fill the form and bind it to a datasource.
	 * 
	 * @param customArea
	 */
	protected abstract void fillForm(FormLayout customArea);

}
