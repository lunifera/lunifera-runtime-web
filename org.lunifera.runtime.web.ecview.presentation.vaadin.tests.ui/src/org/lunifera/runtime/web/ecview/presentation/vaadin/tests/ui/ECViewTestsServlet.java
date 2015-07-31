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
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 * An implementation of VaadinServlet that uses SimpleUI as its base UI.
 */
@SuppressWarnings("serial")
@VaadinServletConfiguration(ui = ECViewTestsUI.class, productionMode = false)
public class ECViewTestsServlet extends VaadinServlet {

	@Override
	protected VaadinServletService createServletService(
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		ECViewTestsServletService service = new ECViewTestsServletService(this,
				deploymentConfiguration);
		service.init();
		return service;
	}

}
