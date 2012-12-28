/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.runtime.web.gyrex.vaadin.internal;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class VaadinActivator extends BaseBundleActivator {

	private static final String SYMBOLIC_NAME = "org.lunifera.runtime.web.gyrex.vaadin";

	/** the shared instance */
	private static VaadinActivator sharedInstance;

	public VaadinActivator() {
		super(SYMBOLIC_NAME);
	}

	/**
	 * Returns the instance.
	 * 
	 * @return the instance
	 */
	public static VaadinActivator getInstance() throws IllegalStateException {
		final VaadinActivator instance = sharedInstance;
		if (null == instance) {
			throw new IllegalStateException(
					"Gyrex HTTP Core has not been started.");
		}
		return instance;
	}

	@Override
	protected Class<?> getDebugOptions() {
		return VaadinDebug.class;
	}

	@Override
	protected void doStart(BundleContext context) throws Exception {
		super.doStart(context);

		sharedInstance = this;
	}

	@Override
	protected void doStop(BundleContext context) throws Exception {
		super.doStop(context);

		sharedInstance = null;
	}

}