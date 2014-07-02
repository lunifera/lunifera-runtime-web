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

package org.lunifera.runtime.web.common.tests;

import java.io.IOException;
import java.net.MalformedURLException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static BundleContext context;

	// private static boolean locationRegistered;

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	/**
	 * Registers the user area default location as an OSGi service.
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void registerUserLocation() throws IOException,
			MalformedURLException {
		// if (locationRegistered) {
		// return;
		// }
		// BasicLocation location = new BasicLocation("", null, false, null);
		// location.set(new File(System.getProperty("user.home"),
		// "lunifera_test")
		// .toURL(), false);
		// Hashtable<String, Object> properties = new Hashtable<String,
		// Object>();
		// properties.put("type", "osgi.user.area");
		// context.registerService(Location.class, location, properties);
		// locationRegistered = true;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}

}
