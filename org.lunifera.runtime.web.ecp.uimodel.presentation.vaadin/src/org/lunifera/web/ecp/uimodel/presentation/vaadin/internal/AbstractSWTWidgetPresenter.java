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
package org.lunifera.web.ecp.uimodel.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ui.uimodel.core.editparts.IUiEmbeddableEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.IViewContext;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.disposal.AbstractDisposable;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.presentation.IWidgetPresentation;
import org.lunifera.web.ecp.uimodel.presentation.vaadin.IConstants;

import com.vaadin.ui.Component;

/**
 * An abstract implementation of the {@link IWidgetPresentation}.
 */
public abstract class AbstractSWTWidgetPresenter extends AbstractDisposable implements IWidgetPresentation<Component> {

	/**
	 * See {@link IConstants#CSS_CLASS__CONTROL_BASE}.
	 */
	public static final String CSS_CLASS__CONTROL_BASE = IConstants.CSS_CLASS__CONTROL_BASE;

	/**
	 * See {@link IConstants#CSS_CLASS__CONTROL}.
	 */
	public static final String CSS_CLASS__CONTROL = IConstants.CSS_CLASS__CONTROL;

	private final IUiEmbeddableEditpart editpart;

	public AbstractSWTWidgetPresenter(IUiEmbeddableEditpart editpart) {
		this.editpart = editpart;
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return the editpart
	 */
	protected IUiEmbeddableEditpart getEditpart() {
		return editpart;
	}
	
	@Override
	public Object getModel() {
		return getEditpart().getModel();
	}
	
	/**
	 * Returns the view context.
	 * 
	 * @return viewContext
	 */
	protected IViewContext getViewContext() {
		return getEditpart().getView().getContext();
	}
}
