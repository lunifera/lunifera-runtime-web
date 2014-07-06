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

import java.util.HashMap;
import java.util.Map;

import org.lunifera.runtime.web.testbase.api.ITestPart;
import org.lunifera.runtime.web.testbase.api.Testable;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme(Reindeer.THEME_NAME)
@Push
public class TestbaseUI extends UI {
	private Map<String, ITestPart> parts = new HashMap<String, ITestPart>();

	private CssLayout content = new CssLayout();

	@Override
	protected void init(VaadinRequest request) {
		initializeParts();
		setContent(content);
		content.addComponent(new Label("Started properly"));

		getPage().addUriFragmentChangedListener(
				new Page.UriFragmentChangedListener() {
					public void uriFragmentChanged(
							Page.UriFragmentChangedEvent source) {
						showTest(source.getUriFragment());
					}
				});
	}

	/**
	 * Shows the test for the given uri fragment
	 * 
	 * @param uriFragment
	 */
	protected void showTest(final String uriFragment) {
		accessSynchronously(new Runnable() {
			@Override
			public void run() {
				content.removeAllComponents();
				if (parts.containsKey(uriFragment)) {
					content.addComponent(parts.get(uriFragment)
							.getTestComponent());
				}
			}
		});
	}

	/**
	 * Is called to initialize all parts that should possibly become tested.
	 */
	protected void initializeParts() {
		for (Bundle bundle : Activator.getTestBaseBundles()) {
			BundleWiring wiring = bundle.adapt(BundleWiring.class);
			for (String name : wiring.listResources("target/classes/", "*.class",
					BundleWiring.FINDENTRIES_RECURSE)) {
				try {
					Class<?> clazz = bundle.loadClass(name.replace("target/classes/", "").replace("/", ".").replace(".class", ""));
					if (ITestPart.class.isAssignableFrom(clazz)
							&& clazz.isAnnotationPresent(Testable.class)) {
						Testable annotation = clazz
								.getAnnotation(Testable.class);
						parts.put(annotation.uriFragment(),
								(ITestPart) clazz.newInstance());
					}
				} catch (ClassNotFoundException e) {
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
