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
package org.lunifera.runtime.web.ecview.presentation.vaadin.common;

import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;

import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;

/**
 * An abstract implementation of the {@link IWidgetPresentation}. Special for
 * {@link Embedded} vaadin elements.
 */
public abstract class AbstractEmbeddedWidgetPresenter<A extends Component>
		extends AbstractVaadinWidgetPresenter<A> implements
		IWidgetPresentation<A> {

	public AbstractEmbeddedWidgetPresenter(IEmbeddableEditpart editpart) {
		super(editpart);
	}

}
