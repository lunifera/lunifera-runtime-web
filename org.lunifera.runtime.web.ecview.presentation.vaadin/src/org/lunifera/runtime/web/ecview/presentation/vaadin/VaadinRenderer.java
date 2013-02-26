/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.context.ViewContext;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IRenderer;
import org.eclipse.emf.ecp.ecview.common.services.IServiceRegistry;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.BindingManager;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * A special implementation for ECP that offers convenience methods.
 */
public class VaadinRenderer implements IRenderer {

	/**
	 * URI specifying the simple SWT presentation.
	 */
	public static final String UI_KIT_URI = "http://lunifera.org/vaadin/v1";

	/**
	 * Renders the UI for the given componentContainer and UI model.
	 * 
	 * @param componentContainer
	 *            The componentContainer the should be the parent for the
	 *            rendered UI
	 * @param yView
	 *            The view model.
	 * @param options
	 *            rendering options
	 * @throws ContextException
	 *             e
	 */
	public void render(ComponentContainer componentContainer, YView yView,
			Map<String, Object> options) throws ContextException {
		IViewEditpart viewEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);
		render(componentContainer, viewEditpart, options);
	}

	/**
	 * Renders the UI for the given componentContainer and edit part.
	 * 
	 * @param componentContainer
	 *            The componentContainer the should be the parent for the
	 *            rendered UI
	 * @param viewEditpart
	 *            The viewEditPart that should become rendered
	 * @param options
	 *            rendering options
	 * @throws ContextException
	 *             e
	 */
	public void render(ComponentContainer componentContainer,
			IViewEditpart viewEditpart, Map<String, Object> options)
			throws ContextException {
		ViewContext viewContext = new ViewContext(viewEditpart);
		render(viewContext, componentContainer, options);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(IViewContext viewContext, Object componentContainer,
			Map<String, Object> options) throws ContextException {
		initializeService(viewContext, (ComponentContainer) componentContainer);
		viewContext.render(UI_KIT_URI, componentContainer, options);
	}

	/**
	 * Initializes all the needed vaadin specific services
	 * 
	 * @param viewContext
	 */
	protected void initializeService(IViewContext viewContext, ComponentContainer componentContainer) {
		viewContext.registerService(IServiceRegistry.SERVICE__BINDING_MANAGER, new BindingManager(viewContext, VaadinObservables.getRealm(UI.getCurrent())));
	}

}
