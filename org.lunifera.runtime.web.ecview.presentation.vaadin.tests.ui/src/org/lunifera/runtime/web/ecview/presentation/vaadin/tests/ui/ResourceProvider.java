 /**
 * Copyright 2013 Lunifera GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui;

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
