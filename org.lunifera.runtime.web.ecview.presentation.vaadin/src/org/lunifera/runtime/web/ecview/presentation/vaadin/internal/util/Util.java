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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util;

import java.util.Locale;

import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.model.visibility.VisibilityFactory;
import org.lunifera.ecview.core.common.model.visibility.YColor;
import org.lunifera.ecview.core.common.model.visibility.YVisibilityProperties;
import org.lunifera.ecview.core.common.visibility.Color;
import org.lunifera.ecview.core.common.visibility.IVisibilityHandler;

import com.vaadin.ui.Component;

public class Util {

	/**
	 * Maps the handler properties to the eObject.
	 * 
	 * @param handler
	 * @return
	 */
	public static YVisibilityProperties mapYProperties(
			IVisibilityHandler handler) {
		YVisibilityProperties yProps = VisibilityFactory.eINSTANCE
				.createYVisibilityProperties();
		yProps.setBackgroundColor(mapYColor(handler.getBackgroundColor()));
		yProps.setBackgroundColorCode(handler.getBackgroundColorCode());
		yProps.setBold(handler.isBold());
		yProps.setBorder(handler.isBorder());
		yProps.setEditable(handler.isEditable());
		yProps.setEnabled(handler.isEnabled());
		yProps.setForegroundColor(mapYColor(handler.getForegroundColor()));
		yProps.setForegroundColorCode(handler.getForegroundColorCode());
		yProps.setItalic(handler.isItalic());
		yProps.setStrikethrough(handler.isStrikethrough());
		yProps.setUnderline(handler.isUnderline());
		yProps.setVisible(handler.isVisible());

		return yProps;
	}

	/**
	 * Maps the handler color to the eObject.
	 * 
	 * @param color
	 * @return
	 */
	public static YColor mapYColor(Color color) {
		if (color == null) {
			return YColor.UNDEFINED;
		}
		switch (color) {
		case RED:
			return YColor.RED;
		case BLACK:
			return YColor.BLACK;
		case BLUE:
			return YColor.BLUE;
		case GREEN:
			return YColor.GREEN;
		case WHITE:
			return YColor.WHITE;
		case DARK_GRAY:
			return YColor.DARK_GRAY;
		case LIGHT_GRAY:
			return YColor.LIGHT_GRAY;
		case UNDEFINED:
			return YColor.UNDEFINED;
		case YELLOW:
			return YColor.YELLOW;
		default:
			break;
		}

		return null;
	}

	public static void applyCaptions(II18nService service, String label,
			String i18nLabelKey, Locale locale, Component component) {
		if (service != null && isValid(i18nLabelKey)) {
			component.setCaption(service.getValue(i18nLabelKey, locale));
		} else {
			if (isValid(label)) {
				component.setCaption(label);
			}
		}
	}

	private static boolean isValid(String value) {
		return value != null && !value.equals("");
	}

}
