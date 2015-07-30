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

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

@SuppressWarnings("serial")
public class ECViewTestsServletService extends VaadinServletService {

	public ECViewTestsServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		super(servlet, deploymentConfiguration);
	}

	@Override
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}

}
