/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin;

import java.util.Map;

import org.lunifera.ecview.core.common.context.ContextException;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.context.ViewContext;
import org.lunifera.ecview.core.common.editpart.DelegatingEditPartManager;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.presentation.IRenderer;

import com.vaadin.ui.ComponentContainer;

/**
 * A special implementation for ECP that offers convenience methods.
 */
public class VaadinRenderer implements IRenderer {

	/**
	 * URI specifying the simple SWT presentation.
	 */
	public static final String UI_KIT_URI = "http://lunifera.org/ecview/v1/presentation/vaadin";

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
	 * @return viewContext - the rendered view context
	 * @throws ContextException
	 *             e
	 */
	public IViewContext render(ComponentContainer componentContainer,
			YView yView, Map<String, Object> options) throws ContextException {
		IViewEditpart viewEditpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);
		return render(componentContainer, viewEditpart, options);
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
	 * @return viewContext - the rendered view context
	 * @throws ContextException
	 *             e
	 */
	public IViewContext render(ComponentContainer componentContainer,
			IViewEditpart viewEditpart, Map<String, Object> options)
			throws ContextException {
		ViewContext viewContext = new ViewContext(viewEditpart);
		render(viewContext, componentContainer, options);

		return viewContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(IViewContext viewContext, Object componentContainer,
			Map<String, Object> options) throws ContextException {
		viewContext.render(UI_KIT_URI, componentContainer, options);
	}

}
