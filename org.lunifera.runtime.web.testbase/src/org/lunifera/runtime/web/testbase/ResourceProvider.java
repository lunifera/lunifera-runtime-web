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
package org.lunifera.runtime.web.testbase;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

/**
 * Resource provider is responsible to look for resources requested by the
 * HttpService. Therefore it uses the classpath of registered bundles.
 */
public class ResourceProvider implements HttpContext {

	private List<Bundle> resources = new ArrayList<Bundle>();

	@Override
	public URL getResource(String uri) {
		URL resource = null;
		// iterate over the vaadin bundles and try to find the requested
		// resource
		for (Bundle bundle : resources) {
			resource = bundle.getResource(uri);
			if (resource != null) {
				break;
			}
		}
		return resource;
	}

	/**
	 * Adds a bundle that may potentially contain a requested resource.
	 * 
	 * @param bundle
	 */
	public void add(Bundle bundle) {
		if(!resources.contains(bundle)){
			resources.add(bundle);
		}
	}

	/**
	 * Removes a bundle that may potentially contain a requested resource.
	 * 
	 * @param bundle
	 */
	public void remove(Bundle bundle) {
		resources.remove(bundle);
	}
	
	@Override
	public String getMimeType(String arg0) {
		return null;
	}
	
	@Override
	public boolean handleSecurity(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return true;
	}
}
