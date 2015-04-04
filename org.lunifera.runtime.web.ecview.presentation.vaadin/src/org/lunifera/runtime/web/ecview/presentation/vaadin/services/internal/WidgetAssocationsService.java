
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


package org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal;

import java.util.Map;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.emf.ElementEditpart;
import org.lunifera.ecview.core.common.model.core.YElement;
import org.lunifera.ecview.core.common.services.AbstractWidgetAssocationsService;

import com.vaadin.ui.Component;

@SuppressWarnings("restriction")
public class WidgetAssocationsService extends
		AbstractWidgetAssocationsService<Component, YElement> {

	@Override
	public YElement getModelElement(String id) {
		synchronized (associations) {
			for (Map.Entry<Component, YElement> entry : associations.entrySet()) {
				YElement yElement = entry.getValue();
				if (yElement.getId() != null && yElement.getId().equals(id)) {
					return yElement;
				}
			}
		}
		return null;
	}

	@Override
	public IElementEditpart getEditpart(String id) {
		YElement yElement = getModelElement(id);
		return yElement != null ? ElementEditpart.getEditpart(yElement) : null;
	}

}
