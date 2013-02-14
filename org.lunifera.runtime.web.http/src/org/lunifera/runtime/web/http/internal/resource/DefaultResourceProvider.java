/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.internal.httpservice.DefaultResourceProvider (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http.internal.resource;

import java.net.MalformedURLException;
import java.net.URL;

import org.lunifera.runtime.web.http.IResourceProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

/**
 * Uses the {@link HttpContext} and the {@link Bundle} to look for resources.
 */
public class DefaultResourceProvider implements IResourceProvider {

	private final Bundle bundle;
	private final HttpContext context;

	public DefaultResourceProvider(final Bundle bundle,
			final HttpContext context) {
		this.bundle = bundle;
		this.context = context;
	}

	@Override
	public URL getResource(final String path) throws MalformedURLException {
		if (null != context) {
			return context.getResource(path);
		}
		return bundle.getEntry(path);
	}

}
