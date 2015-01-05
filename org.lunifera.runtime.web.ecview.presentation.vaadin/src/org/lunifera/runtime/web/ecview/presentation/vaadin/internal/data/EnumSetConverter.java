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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;

/**
 * This converter is used to support enums in vaadin combobox, optionsgroup,...
 */
public class EnumSetConverter implements Converter {

	private Container datasource;

	public EnumSetConverter(Class<Enum<?>> type, Container datasource) {
		this.datasource = datasource;
	}

	@Override
	public Class<?> getModelType() {
		return Set.class;
	}

	@Override
	public Class<?> getPresentationType() {
		return Set.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object convertToModel(Object value, Class targetType, Locale locale)
			throws ConversionException {
		if (value == null) {
			return null;
		}

		Set<EnumOptionBean> values = (Set<EnumOptionBean>) value;
		if (values.size() == 0) {
			return Collections.emptySet();
		}

		Set result = new HashSet();
		for (EnumOptionBean casted : values) {
			result.add(casted.getEnumx());
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object convertToPresentation(Object value, Class targetType,
			Locale locale) throws ConversionException {
		if (value == null) {
			return null;
		}

		Set<Enum<?>> values = (Set<Enum<?>>) value;
		if (values.size() == 0) {
			return Collections.emptySet();
		}

		Set<EnumOptionBean> result = new HashSet<EnumOptionBean>();
		for (Enum<?> casted : values) {
			for (Object itemId : datasource.getItemIds()) {
				EnumOptionBean castedId = (EnumOptionBean) itemId;
				if (castedId.getEnumx() == casted) {
					result.add(castedId);
				}
			}
		}

		return result;
	}

}