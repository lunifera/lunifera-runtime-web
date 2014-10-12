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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.editpart.IDialogEditpart;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.presentation.IPresentationFactory;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBeanReferenceFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBooleanSearchFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IBrowserEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IButtonEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ICheckboxEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IComboBoxEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IDateTimeEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IDecimalFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IEnumComboBoxEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IEnumListEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IEnumOptionsGroupEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IFormLayoutEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IGridLayoutEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IHorizontalLayoutEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IImageEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ILabelEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IListEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IMasterDetailEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.INumericFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.INumericSearchFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IOptionsGroupEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IPanelEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IProgressBarEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ISearchPanelEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ISplitPanelEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITabEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITabSheetEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITableEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextAreaEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITextSearchFieldEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITreeEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.IVerticalLayoutEditpart;
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
			IViewContext uiContext, IElementEditpart editpart)
			throws IllegalArgumentException {
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
		} else if (editpart instanceof ITreeEditpart) {
			return (A) new TreePresentation(editpart);
		} else if (editpart instanceof IOptionsGroupEditpart) {
			return (A) new OptionsGroupPresentation(editpart);
		} else if (editpart instanceof IButtonEditpart) {
			return (A) new ButtonPresentation(editpart);
		} else if (editpart instanceof IGridLayoutEditpart) {
			return (A) new GridLayoutPresentation(editpart);
		} else if (editpart instanceof IHorizontalLayoutEditpart) {
			return (A) new HorizontalLayoutPresentation(editpart);
		} else if (editpart instanceof IVerticalLayoutEditpart) {
			return (A) new VerticalLayoutPresentation(editpart);
		} else if (editpart instanceof IFormLayoutEditpart) {
			return (A) new FormLayoutPresentation(editpart);
		} else if (editpart instanceof IDecimalFieldEditpart) {
			return (A) new DecimalFieldPresentation(editpart);
		} else if (editpart instanceof INumericFieldEditpart) {
			return (A) new NumericFieldPresentation(editpart);
		} else if (editpart instanceof IDateTimeEditpart) {
			return (A) new DateTimePresentation(editpart);
		} else if (editpart instanceof IBrowserEditpart) {
			return (A) new BrowserPresentation(editpart);
		} else if (editpart instanceof IProgressBarEditpart) {
			return (A) new ProgressBarPresentation(editpart);
		} else if (editpart instanceof ITabSheetEditpart) {
			return (A) new TabSheetPresentation(editpart);
		} else if (editpart instanceof ITabEditpart) {
			return (A) new TabPresentation(editpart);
		} else if (editpart instanceof IMasterDetailEditpart) {
			return (A) new MasterDetailPresentation(editpart);
		} else if (editpart instanceof IImageEditpart) {
			return (A) new ImagePresentation(editpart);
		} else if (editpart instanceof IDialogEditpart) {
			return (A) new DialogPresentation(editpart);
		} else if (editpart instanceof ITextSearchFieldEditpart) {
			return (A) new TextSearchFieldPresentation(editpart);
		} else if (editpart instanceof INumericSearchFieldEditpart) {
			return (A) new NumericSearchFieldPresentation(editpart);
		} else if (editpart instanceof IBooleanSearchFieldEditpart) {
			return (A) new BooleanSearchFieldPresentation(editpart);
		} else if (editpart instanceof ISplitPanelEditpart) {
			return (A) new SplitPanelPresentation(editpart);
		} else if (editpart instanceof IPanelEditpart) {
			return (A) new PanelPresentation(editpart);
		} else if (editpart instanceof ISearchPanelEditpart) {
			return (A) new SearchPanelPresentation(editpart);
		} else if (editpart instanceof IBeanReferenceFieldEditpart) {
			return (A) new BeanReferenceFieldPresentation(editpart);
		} else if (editpart instanceof IEnumComboBoxEditpart) {
			return (A) new EnumComboBoxPresentation(editpart);
		} else if (editpart instanceof IEnumListEditpart) {
			return (A) new EnumListPresentation(editpart);
		} else if (editpart instanceof IEnumOptionsGroupEditpart) {
			return (A) new EnumOptionsGroupPresentation(editpart);
		}

		throw new IllegalArgumentException(String.format(
				"No presenter available for editpart %s[%s]", editpart
						.getClass().getName(), editpart.getId()));
	}
}
