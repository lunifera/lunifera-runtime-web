package org.lunifera.runtime.web.common.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.eclipse.core.runtime.internal.adaptor.BasicLocation;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static BundleContext context;
	private static boolean locationRegistered;

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
	@SuppressWarnings({ "restriction", "deprecation" })
	public static void registerUserLocation() throws IOException,
			MalformedURLException {
		if (locationRegistered) {
			return;
		}
		BasicLocation location = new BasicLocation("", null, false, null);
		location.set(new File(System.getProperty("user.home"), "lunifera_test")
				.toURL(), false);
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put("type", "osgi.user.area");
		context.registerService(Location.class, location, properties);
		locationRegistered = true;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}

}
