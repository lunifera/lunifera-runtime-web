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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.gyrex.http.application.context.IResourceProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class VaadinResourceProvider implements IResourceProvider,
		BundleListener {

	private Set<Bundle> resourceBundles = new HashSet<Bundle>();
	private final Bundle bundle;

	public VaadinResourceProvider(final Bundle bundle) {
		this.bundle = bundle;

		init(bundle.getBundleContext());
	}

	@Override
	public URL getResource(final String path) throws MalformedURLException {
		URL resource = null;
		// iterate the server bundle, client bundle and fragments
		for (Bundle bundle : resourceBundles) {
			String uri = path.startsWith("/") ? path : "/" + path;

			String root = (String) bundle.getHeaders().get("Vaadin-Resources");
			if (isValid(root)) {
				uri = "/" + root + uri;
			}
			if (null != (resource = bundle.getResource(uri))) {
				break;
			}
		}
		return resource;
	}

	@Override
	public Set<String> getResourcePaths(final String path) {
		final HashSet<String> result = new HashSet<String>();

		// iterate the server bundle, client bundle and fragments
		for (Bundle bundle : resourceBundles) {
			String uri = path.startsWith("/") ? path : "/" + path;

			String root = (String) bundle.getHeaders().get("Vaadin-Resources");
			if (isValid(root)) {
				uri = "/" + root + uri;
			}
			final Enumeration<String> entryPaths = bundle.getEntryPaths(uri);
			if (entryPaths != null) {
				while (entryPaths.hasMoreElements()) {
					result.add(entryPaths.nextElement());
				}
				break;
			}
		}
		return result;
	}

	private boolean isValid(String root) {
		return root != null && !root.equals("") && !".".equals(root);
	}

	private void checkBundleForResources(Bundle bundle) {
		if (isClientBundle(bundle) || isServerBundle(bundle)
				|| isThemesBundle(bundle)) {
			resourceBundles.add(bundle);
		} else if (null != bundle.getHeaders().get("Vaadin-Resources")) {
			resourceBundles.add(bundle);
		} else {
			resourceBundles.remove(bundle);
		}
	}

	/**
	 * Returns true if the bundle is the vaadin server bundle.
	 * 
	 * @param bundle
	 * @return
	 */
	private boolean isServerBundle(Bundle bundle) {
		return bundle.getSymbolicName().equals("com.vaadin.server");
	}

	/**
	 * Returns true, if the bundle is the vaadin client bundle.
	 * 
	 * @param bundle
	 * @return
	 */
	private boolean isClientBundle(Bundle bundle) {
		return bundle.getSymbolicName().equals("com.vaadin.client")
				|| bundle.getSymbolicName()
						.equals("com.vaadin.client-compiled");
	}

	/**
	 * Returns true, if the bundle is the vaadin themes bundle.
	 * 
	 * @param bundle
	 * @return
	 */
	private boolean isThemesBundle(Bundle bundle) {
		return bundle.getSymbolicName().equals("com.vaadin.themes");
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (event.getBundle() == this.bundle) {
			if (event.getType() == BundleEvent.STOPPED) {
				this.bundle.getBundleContext().removeBundleListener(this);
				return;
			}
		}

		if (event.getType() == BundleEvent.UNINSTALLED) {
			resourceBundles.remove(event.getBundle());
		} else {
			checkBundleForResources(event.getBundle());
		}
	}

	public void init(BundleContext ctx) {
		ctx.addBundleListener(this);
		for (Bundle bundle : ctx.getBundles()) {
			checkBundleForResources(bundle);
		}
	}

}
