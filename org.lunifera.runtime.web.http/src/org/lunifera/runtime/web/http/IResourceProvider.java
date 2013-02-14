/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on org.eclipse.gyrex.http.application.context.IResourceProvider (Gunnar Wagenknecht)
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.http;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

/**
 * Resource provider is used to provide resources for a given path.
 */
public interface IResourceProvider {

	/**
	 * Maps a resource path to a URL.
	 * <p>
	 * Called by the application to map a resource path to a URL. For servlets
	 * registrations the container will call this method to support the
	 * {@link ServletContext} methods {@link ServletContext#getResource(String)}
	 * and {@link ServletContext#getResourceAsStream(String)}. For resource
	 * registrations, the container will call this method to locate the
	 * resource.
	 * </p>
	 * 
	 * @param path
	 *            a <code>String</code> specifying the path to the resource
	 * @return URL to the resource located at the named path, or
	 *         <code>null</code> if there is no resource at that path
	 * @throws MalformedURLException
	 *             if the pathname is not given in the correct form
	 */
	public abstract URL getResource(final String path)
			throws MalformedURLException;
}
