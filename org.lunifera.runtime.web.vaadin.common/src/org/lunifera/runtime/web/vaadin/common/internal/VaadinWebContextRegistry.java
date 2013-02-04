/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.common.internal;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.lunifera.runtime.web.common.IConstants;
import org.lunifera.runtime.web.common.IWebContext;
import org.lunifera.runtime.web.common.context.AbstractWebContextRegistry;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;

public class VaadinWebContextRegistry extends AbstractWebContextRegistry {

	@Override
	public IWebContext doCreateContext(ComponentFactory factory, String user,
			Map<String, Object> inProperties) {

		// prepare the properties
		//
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		String id = UUID.randomUUID().toString();
		if (inProperties != null) {
			properties.putAll(inProperties);
		}
		properties.put(IConstants.OSGI_PROPERTY__WEB_CONTEXT__ID, id);
		properties.put(IConstants.OSGI_PROPERTY__WEB_CONTEXT__USER, user);

		// create the instance of the web context
		//
		ComponentInstance instance = factory.newInstance(properties);

		// register the registry as a dispose listener to observe the lifecycle
		// of the context
		//
		VaadinWebContext context = (VaadinWebContext) instance.getInstance();
		context.addDisposeListener(this);

		return context;
	}
}
