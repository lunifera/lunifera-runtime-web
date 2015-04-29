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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class AbstractMessageDialog extends AbstractDialog {

	protected Embedded messageIcon;
	protected Label messageText;

	protected AbstractMessageDialog(DialogConfig config, Option... options) {
		super(config, options);
	}

	/**
	 * Config the dialog.
	 */
	protected void configDialog() {
		super.configDialog();

		if (config != null) {
			if (messageIcon != null) {
				messageIcon.setSource(config.getIcon());
			}
			if (messageText != null) {
				messageText.setValue(config.getMessage());
			}
		}
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

		customArea = new HorizontalLayout();
		((HorizontalLayout) customArea).setSpacing(true);
		customArea.setSizeFull();
		mainLayout.addComponent(customArea);
		mainLayout.setExpandRatio(customArea, 1.0f);

		messageIcon = new Embedded();
		customArea.addComponent(messageIcon);

		messageText = new Label();
		messageText.setContentMode(ContentMode.TEXT);
		customArea.addComponent(messageText);
		messageText.setSizeFull();
		((HorizontalLayout) customArea).setExpandRatio(messageText, 1.0f);

		optionsArea = new HorizontalLayout();
		optionsArea.setSpacing(true);
		mainLayout.addComponent(optionsArea);
	}

}
