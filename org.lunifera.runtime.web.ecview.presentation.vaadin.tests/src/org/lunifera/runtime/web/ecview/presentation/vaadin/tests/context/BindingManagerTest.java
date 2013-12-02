/**
 * Copyright (c) 2012 Committers of lunifera.org.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextField;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.BindingManager;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("restriction")
public class BindingManagerTest {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
	private BindingManager bindingManager;

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 * @throws BundleException
	 */
	@Before
	public void setup() throws ConfigurationException, BundleException {
		UI.setCurrent(new DefaultUI());
		bindingManager = new BindingManager(new DefaultViewContext(),
				VaadinObservables.getRealm(UI.getCurrent()));
	}

	@Test
	public void test_bindEnabled() {
		TextField text = new TextField();
		YTextField yText = factory.createTextField();
		yText.setEnabled(yText.isInitialEditable());

		bindingManager.bindEnabled(yText, text);
		assertTrue(text.isEnabled());

		yText.setEnabled(false);
		assertFalse(text.isEnabled());

		// test target to model
		text.setEnabled(true);
		// -> no update
		assertFalse(yText.isEnabled());
	}

	@Test
	public void test_bindVisible() {
		TextField text = new TextField();
		YTextField yText = factory.createTextField();
		yText.setVisible(yText.isInitialVisible());

		bindingManager.bindVisible(yText, text);
		assertTrue(text.isVisible());

		yText.setVisible(false);
		assertFalse(text.isVisible());

		// test target to model
		text.setVisible(true);
		// -> no update
		assertFalse(yText.isVisible());
	}

	@Test
	public void test_bindReadonly() {
		TextField text = new TextField();
		YTextField yText = factory.createTextField();
		yText.setEditable(yText.isInitialEditable());

		bindingManager.bindReadonly(yText, text);
		assertFalse(text.isReadOnly());

		yText.setEditable(true);
		assertFalse(text.isReadOnly());

		// test target to model
		text.setReadOnly(false);
		assertTrue(yText.isEditable());
	}
}
