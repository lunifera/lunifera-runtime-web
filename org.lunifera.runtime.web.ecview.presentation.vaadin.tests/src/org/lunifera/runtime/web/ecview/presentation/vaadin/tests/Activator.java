/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests;

import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context.BundleHelper;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(Activator.class);

	private static Activator instance;
	public static BundleContext context;

	/**
	 * @return the instance
	 */
	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		instance = this;

		BundleAssert.setDefaultBundleContext(context);
		ServiceAssert.setDefaultBundleContext(context);
		
		BundleHelper.ensureSetup();
	}

	/**
	 * Returns the bundle with the given id.
	 * 
	 * @param id
	 * @return
	 */
	public static Bundle findBundle(String id) {
		for (Bundle bundle : context.getBundles()) {
			if (bundle.getSymbolicName().equals(id)) {
				return bundle;
			}
		}
		return null;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		instance = null;
	}
}
