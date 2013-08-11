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
