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
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.ui;

import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
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
					public void uriFragmentChanged(
							final UriFragmentChangedEvent event) {
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

		String fragment = request.getPathInfo().replace("/", "");
		handleFragment(SAMPLE_PACKAGE + "." + fragment);
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
