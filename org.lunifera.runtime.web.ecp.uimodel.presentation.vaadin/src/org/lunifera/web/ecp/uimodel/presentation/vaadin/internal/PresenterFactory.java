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

import org.eclipse.emf.ecp.ui.uimodel.core.editparts.IUiElementEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.IUiViewEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.context.IViewContext;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiCheckboxEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiComboBoxEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiGridLayoutEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiLabelEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiListEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiTableEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiTextAreaEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.extension.IUiTextFieldEditpart;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.presentation.IPresentationFactory;
import org.eclipse.emf.ecp.ui.uimodel.core.editparts.presentation.IWidgetPresentation;
import org.lunifera.web.ecp.uimodel.presentation.vaadin.VaadinRenderer;

/**
 * The presenter factory.
 */
public class PresenterFactory implements IPresentationFactory {

	public PresenterFactory(){
		
	}
	
	@Override
	public boolean isFor(IViewContext uiContext, IUiElementEditpart editpart) {
		String presentationURI = uiContext.getPresentationURI();
		return presentationURI != null && presentationURI.equals(VaadinRenderer.UI_KIT_URI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends IWidgetPresentation<?>> A createPresentation(IViewContext uiContext, IUiElementEditpart editpart) {
		if (editpart instanceof IUiViewEditpart) {
			return (A) new ViewPresentation((IUiViewEditpart) editpart);
		} else if (editpart instanceof IUiTextFieldEditpart) {
			return (A) new TextFieldPresentation(editpart);
		} else if (editpart instanceof IUiLabelEditpart) {
			return (A) new LabelPresentation(editpart);
		} else if (editpart instanceof IUiTextAreaEditpart) {
			return (A) new TextAreaPresentation(editpart);
		} else if (editpart instanceof IUiCheckboxEditpart) {
			return (A) new CheckBoxPresentation(editpart);
		} else if (editpart instanceof IUiComboBoxEditpart) {
			return (A) new ComboBoxPresentation(editpart);
		} else if (editpart instanceof IUiListEditpart) {
			return (A) new ListPresentation(editpart);
		} else if (editpart instanceof IUiTableEditpart) {
			return (A) new TablePresentation(editpart);
		} else if (editpart instanceof IUiGridLayoutEditpart) {
			return (A) new GridLayoutPresentation(editpart);
		}

		throw new IllegalArgumentException(String.format("No presenter available for editpart %s[%s]", editpart
			.getClass().getName(), editpart.getId()));
	}

}
