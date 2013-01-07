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
package org.lunifera.web.ecp.uimodel.presentation.vaadin;

import java.util.Map;

import org.eclipse.emf.ecp.ui.model.core.uimodel.YUiView;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.IUiViewEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.ContextException;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.IViewContext;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.ViewContext;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.presentation.IRenderer;

import com.vaadin.ui.ComponentContainer;
 
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
	 * @param componentContainer The componentContainer the should be the parent for the rendered UI
	 * @param yView The view model.
	 * @param options rendering options
	 * @throws ContextException e
	 */
	public void render(ComponentContainer componentContainer, YUiView yView, Map<String, Object> options)
		throws ContextException {
		IUiViewEditpart viewEditpart = DelegatingEditPartManager.getInstance().getEditpart(yView);
		render(componentContainer, viewEditpart, options);
	}

	/**
	 * Renders the UI for the given componentContainer and edit part.
	 * 
	 * @param componentContainer The componentContainer the should be the parent for the rendered UI
	 * @param viewEditpart The viewEditPart that should become rendered
	 * @param options rendering options
	 * @throws ContextException e
	 */
	public void render(ComponentContainer componentContainer, IUiViewEditpart viewEditpart, Map<String, Object> options)
		throws ContextException {
		ViewContext viewContext = new ViewContext(viewEditpart);
		render(viewContext, componentContainer, options);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(IViewContext viewContext, Object componentContainer, Map<String, Object> options)
		throws ContextException {
		viewContext.render(UI_KIT_URI, componentContainer, options);
	}

}
