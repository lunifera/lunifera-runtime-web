/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.concurrent.Future;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.ViewContext;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.CoreModelPackage;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextFieldPresentation;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextFieldPresentation}.
 */
@SuppressWarnings("restriction")
public class ViewPresentationTests {

	private CssLayout rootLayout = new CssLayout();
	private DelegatingEditPartManager editpartManager = DelegatingEditPartManager
			.getInstance();

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 * @throws BundleException
	 */
	@Before
	public void setup() throws ConfigurationException, BundleException {
		Locale.setDefault(Locale.GERMANY);
		UI.setCurrent(new DefaultUI());
		UI.getCurrent().setContent(rootLayout);
	}

	/**
	 * Must be implemented with running server.
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testExec() throws ContextException {
		IViewEditpart viewEditpart = (IViewEditpart) editpartManager
				.createEditpart(CoreModelPackage.eNS_URI, IViewEditpart.class);
		ViewContext context = new ViewContext(viewEditpart);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, (YView) viewEditpart.getModel(), null);

		final boolean[] executed = new boolean[] { false };
		context.exec(new Runnable() {
			@Override
			public void run() {
				executed[0] = true;
			}
		});

		assertTrue(executed[0]);
	}

	/**
	 * Must be implemented with running server.
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testAsncExec() throws InterruptedException, ContextException {
		IViewEditpart viewEditpart = (IViewEditpart) editpartManager
				.createEditpart(CoreModelPackage.eNS_URI, IViewEditpart.class);
		ViewContext context = new ViewContext(viewEditpart);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, (YView) viewEditpart.getModel(), null);

		final boolean[] executed = new boolean[] { false };
		Future<?> future = context.execAsync(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					executed[0] = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		assertFalse(future.isDone());
		Thread.sleep(1000);
		assertTrue(future.isDone());
		assertTrue(executed[0]);
	}
}
