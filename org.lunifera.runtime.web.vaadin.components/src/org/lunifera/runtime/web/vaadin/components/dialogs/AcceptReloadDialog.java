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
package org.lunifera.runtime.web.vaadin.components.dialogs;

import java.util.Locale;

import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.runtime.web.vaadin.common.resource.IResourceProvider;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Dialog to accept the reload of dirty data. Data will be lost if proceeded.
 */
public class AcceptReloadDialog extends OptionsDialog {

	public static void showDialog(II18nService service,
			IResourceProvider resourceProvider, Runnable onDelete,
			Runnable onCancel) {
		if (service == null) {
			throw new NullPointerException("Please pass an i18nService");
		}

		Locale locale = UI.getCurrent().getLocale();
		String dialogTitle = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD_DIALOG_TITLE, locale);
		String dialogMessage = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD_DIALOG_MESSAGE, locale);
		String dialogDescription = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD_DIALOG_DESCRIPTION, locale);
		String dialogIcon = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD_DIALOG_ICON, locale);
		String optionDeleteCaption = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD__DIALOG_OPTION__RELOAD_CAPTION,
				locale);
		String optionDeleteDescription = service
				.getValue(
						IDialogI18nKeys.ACCEPT_RELOAD__DIALOG_OPTION__RELOAD_DESCRIPTION,
						locale);
		String optionDeleteIcon = service.getValue(
				IDialogI18nKeys.ACCEPT_RELOAD__DIALOG_OPTION__RELOAD_ICON,
				locale);
		String optionCancelCaption = service.getValue(
				IDialogI18nKeys.DIALOG_OPTION__CANCEL_CAPTION, locale);
		String optionCancelDescription = service.getValue(
				IDialogI18nKeys.DIALOG_OPTION__CANCEL_DESCRIPTION, locale);
		String optionCancelIcon = service.getValue(
				IDialogI18nKeys.DIALOG_OPTION__CANCEL_ICON, locale);

		DialogConfig config = new DialogConfig(dialogTitle, dialogMessage,
				dialogDescription, createResource(dialogIcon, resourceProvider)) {
			@Override
			public void config(Window window) {
				super.config(window);
				window.setHeight("170px");
				window.setWidth("350px");
				window.center();
			}
		};

		AcceptReloadDialog dialog = new AcceptReloadDialog(config, new Option(
				optionCancelCaption, optionCancelDescription, createResource(
						optionCancelIcon, resourceProvider), onCancel),
				new Option(optionDeleteCaption, optionDeleteDescription,
						createResource(optionDeleteIcon, resourceProvider),
						onDelete));
		dialog.open();
	}

	private AcceptReloadDialog(DialogConfig config, Option... options) {
		super(config, options);
	}

}
