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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.lunifera.ecview.core.common.context.II18nService;

public class EnumOptionBeanHelper {

	/**
	 * Creates a list of enum options for the given enum. The services are used
	 * for internationalization und to find a proper image for the enum literal.
	 * Services and locale may be <code>null</code>.
	 * 
	 * @param enumClass
	 * @param i18nService
	 * @param imageService
	 * @param locale
	 * @return
	 */
	public static List<EnumOptionBean> getBeans(
			Class<? extends Enum<?>> enumClass, II18nService i18nService,
			Locale locale) {

		String enumName = enumClass.getCanonicalName();

		List<EnumOptionBean> result = new ArrayList<EnumOptionBean>();
		for (Enum<?> literal : enumClass.getEnumConstants()) {
			String description = literal.name();
			if (i18nService != null) {
				String temp = i18nService.getValue(
						getI18nKey(enumName, literal), locale);
				if (temp != null && !temp.equals("")) {
					description = temp;
				}
			}

			String imagePath = null;
			if (i18nService != null) {
				imagePath = i18nService.getValue(
						getImageI18nKey(enumName, literal), locale);
			}

			result.add(new EnumOptionBean(literal, description, imagePath));
		}

		return result;
	}

	protected static String getI18nKey(String enumName, Enum<?> literal) {
		return enumName + "." + literal.name();
	}

	protected static String getImageI18nKey(String enumName, Enum<?> literal) {
		return enumName + "." + literal.name() + ".image";
	}

}
