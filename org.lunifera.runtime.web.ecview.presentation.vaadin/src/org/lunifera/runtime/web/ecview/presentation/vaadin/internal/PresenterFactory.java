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

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.presentation.IPresentationFactory;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IBrowserEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IButtonEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ICheckboxEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IComboBoxEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDateTimeEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IGridLayoutEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IHorizontalLayoutEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ILabelEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IListEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.INumericFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITableEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextAreaEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IVerticalLayoutEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;

/**
 * The presenter factory.
 */
public class PresenterFactory implements IPresentationFactory {

	public PresenterFactory() {

	}

	@Override
	public boolean isFor(IViewContext uiContext, IElementEditpart editpart) {
		String presentationURI = uiContext.getPresentationURI();
		return presentationURI != null
				&& presentationURI.equals(VaadinRenderer.UI_KIT_URI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends IWidgetPresentation<?>> A createPresentation(
			IViewContext uiContext, IElementEditpart editpart) {
		if (editpart instanceof IViewEditpart) {
			return (A) new ViewPresentation((IViewEditpart) editpart);
		} else if (editpart instanceof ITextFieldEditpart) {
			return (A) new TextFieldPresentation(editpart);
		} else if (editpart instanceof ILabelEditpart) {
			return (A) new LabelPresentation(editpart);
		} else if (editpart instanceof ITextAreaEditpart) {
			return (A) new TextAreaPresentation(editpart);
		} else if (editpart instanceof ICheckboxEditpart) {
			return (A) new CheckBoxPresentation(editpart);
		} else if (editpart instanceof IComboBoxEditpart) {
			return (A) new ComboBoxPresentation(editpart);
		} else if (editpart instanceof IListEditpart) {
			return (A) new ListPresentation(editpart);
		} else if (editpart instanceof ITableEditpart) {
			return (A) new TablePresentation(editpart);
		} else if (editpart instanceof IButtonEditpart) {
			return (A) new ButtonPresentation(editpart);
		} else if (editpart instanceof IGridLayoutEditpart) {
			return (A) new GridLayoutPresentation(editpart);
		} else if (editpart instanceof IHorizontalLayoutEditpart) {
			return (A) new HorizontalLayoutPresentation(editpart);
		} else if (editpart instanceof IVerticalLayoutEditpart) {
			return (A) new VerticalLayoutPresentation(editpart);
		} else if (editpart instanceof IDecimalFieldEditpart) {
			return (A) new DecimalFieldPresentation(editpart);
		} else if (editpart instanceof INumericFieldEditpart) {
			return (A) new NumericFieldPresentation(editpart);
		} else if (editpart instanceof IDateTimeEditpart) {
			return (A) new DateTimePresentation(editpart);
		} else if (editpart instanceof IBrowserEditpart) {
			return (A) new BrowserPresentation(editpart);
		}

		throw new IllegalArgumentException(String.format(
				"No presenter available for editpart %s[%s]", editpart
						.getClass().getName(), editpart.getId()));
	}
}
