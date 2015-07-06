/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.vaadin.common.resource.impl;

import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;
import org.osgi.service.component.annotations.Component;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

/**
 * Default implementation.
 */
@Component(service = IResourceProvider.class)
public class ThemeResourceProvider implements IResourceProvider {

	@Override
	public Resource getResource(String resourcePath) {
		return new ThemeResource(resourcePath);
	}

}
