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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.presentation.IFieldPresentation;
import org.eclipse.emf.ecp.ecview.common.validation.IValidator;

import com.vaadin.ui.Component;

/**
 * An abstract implementation of the {@link IFieldPresentation}.
 */
public abstract class AbstractFieldWidgetPresenter<A extends Component> extends
		AbstractVaadinWidgetPresenter<A> implements IFieldPresentation<A> {

	public AbstractFieldWidgetPresenter(IEmbeddableEditpart editpart) {
		super(editpart);
	}

	@Override
	public void addValidator(IValidator validator) {
		
	}

	@Override
	public void removeValidator(IValidator validator) {
		
	}
	
}
