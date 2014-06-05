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

import org.eclipse.core.databinding.observable.Realm;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme(Reindeer.THEME_NAME)
public class ECViewTestsUI extends UI {

	private static final String SAMPLE_PACKAGE = "org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui.samples";
	private VerticalLayout layout;

	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);

		layout.addComponent(new Label("Insert URL fragment to open testclass"));

		getPage().addUriFragmentChangedListener(
				new Page.UriFragmentChangedListener() {
					@Override
					public void uriFragmentChanged(final UriFragmentChangedEvent event) {
						getUI().accessSynchronously(new Runnable() {
							@Override
							public void run() {
								VaadinObservables.getRealm(getUI());
								String fragment = event.getUriFragment();
								handleFragment(SAMPLE_PACKAGE + "." + fragment);
							}
						});
					}
				});
	}

	protected void handleFragment(String className) {
		try {
			Class<?> clazz = Activator.getContext().getBundle()
					.loadClass(className);
			if (Component.class.isAssignableFrom(clazz)) {
				layout.removeAllComponents();

				Component component = (Component) clazz.newInstance();
				component.setSizeFull();
				layout.addComponent(component);
			} else {
				handleError(className);
			}
		} catch (ClassNotFoundException e) {
			handleError(className);
		} catch (InstantiationException e) {
			handleError(className);
		} catch (IllegalAccessException e) {
			handleError(className);
		}
	}

	private void handleError(String clazzname) {
		layout.removeAllComponents();
		layout.addComponent(new Label(clazzname
				+ " is not a valid test sample!"));
	}
}
