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

import java.util.Locale;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;

/**
 * This converter is used to support enums in vaadin combobox, optionsgroup,...
 */
public class EnumConverter implements Converter {

	private Class<Enum<?>> type;
	private Container datasource;

	public EnumConverter(Class<Enum<?>> type, Container datasource) {
		this.type = type;
		this.datasource = datasource;
	}

	@Override
	public Class<Enum<?>> getModelType() {
		return type;
	}

	@Override
	public Class<EnumOptionBean> getPresentationType() {
		return EnumOptionBean.class;
	}

	@Override
	public Object convertToModel(Object value, Class targetType, Locale locale)
			throws ConversionException {
		if(value == null){
			return null;
		}
		EnumOptionBean casted = (EnumOptionBean) value;
		return casted != null ? casted.getEnumx() : null;
	}

	@Override
	public Object convertToPresentation(Object value, Class targetType,
			Locale locale) throws ConversionException {
		if(value == null){
			return null;
		}
		Enum<?> casted = (Enum<?>) value;
		for (Object itemId : datasource.getItemIds()) {
			EnumOptionBean castedId = (EnumOptionBean) itemId;
			if (castedId.getEnumx() == casted) {
				return castedId;
			}
		}
		return null;
	}

}