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
package org.lunifera.runtime.web.vaadin.components.fields.search.filter;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.lunifera.runtime.web.vaadin.common.data.filter.Filters;
import org.lunifera.runtime.web.vaadin.components.converter.DecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.converter.Converter.ConversionException;

public class NumericFilterProperty extends FilterProperty {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NumericFilterProperty.class);

	private final DecimalConverter numberConverter = new DecimalConverter();
	private String stringValue;

	private Number number;
	private Wildcard wildcard;

	private Class<? extends Number> type;

	private Filters filterProvider = new Filters();

	public NumericFilterProperty(Class<? extends Number> type,
			Object propertyId, Locale locale) {
		super(propertyId, locale);
		this.type = type;

		numberConverter.setUseGrouping(true);
		numberConverter.setPrecision(4);
	}

	/**
	 * @return the value
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setStringValue(String value) {
		this.stringValue = value;

		parseNumber(value);
	}

	/**
	 * @return the number
	 */
	public Number getNumber() {
		return number;
	}

	public void setLocale(Locale locale) {
		numberConverter.setDecimalFormatSymbols(DecimalFormatSymbols
				.getInstance(locale));
	}

	@Override
	public Filter getFilter() {
		if (filterProvider == null || getNumber() == null) {
			return null;
		}
		if (wildcard == null || wildcard.equals("")) {
			return filterProvider.eq(getPropertyId(), getNumber());
		} else {
			switch (wildcard) {
			case GE:
				return filterProvider.gteq(getPropertyId(), getNumber());
			case GT:
				return filterProvider.gt(getPropertyId(), getNumber());
			case LE:
				return filterProvider.lteq(getPropertyId(), getNumber());
			case LT:
				return filterProvider.lt(getPropertyId(), getNumber());
			case NE:
				return filterProvider.not(filterProvider.eq(getPropertyId(),
						getNumber()));
			}
		}

		throw new IllegalStateException("Not a valid state!");
	}

	/**
	 * Calculates the wildcard.
	 * 
	 * @param value
	 */
	private void parseNumber(String value) {
		wildcard = null;
		number = 0;
		if (value == null) {
			return;
		}
		if (value.startsWith(Wildcard.GE.sequence)) {
			wildcard = Wildcard.GE;
		} else if (value.startsWith(Wildcard.LE.sequence)) {
			wildcard = Wildcard.LE;
		} else if (value.startsWith(Wildcard.GT.sequence)) {
			wildcard = Wildcard.GT;
		} else if (value.startsWith(Wildcard.LT.sequence)) {
			wildcard = Wildcard.LT;
		} else if (value.startsWith(Wildcard.NE.sequence)) {
			wildcard = Wildcard.NE;
		}

		this.stringValue = value.trim();

		String temp = stringValue;
		if (wildcard != null) {
			temp = temp.replaceAll(wildcard.sequence, "").trim();
		}
		try {
			Double convertedDouble = numberConverter.convertToModel(temp,
					Double.class, getLocale());
			number = convertToNumber(convertedDouble);
		} catch (ConversionException e) {
			LOGGER.warn("{}", e);
		}
	}

	private Number convertToNumber(Double value) {
		if (value == null) {
			return null;
		}
		Number result = null;
		if (type == Double.class || type == Double.TYPE) {
			result = value;
		} else if (type == Float.class || type == Float.TYPE) {
			result = value.floatValue();
		} else if (type == Short.class || type == Short.TYPE) {
			result = value.shortValue();
		} else if (type == Integer.class || type == Integer.TYPE) {
			result = value.intValue();
		} else if (type == Byte.class || type == Byte.TYPE) {
			result = value.byteValue();
		} else if (type == Long.class || type == Long.TYPE) {
			result = value.longValue();
		}

		return result;
	}

	/**
	 * @return the wildcard
	 */
	public Wildcard getWildcard() {
		return wildcard;
	}

	public void setUseGrouping(boolean useGrouping) {
		numberConverter.setUseGrouping(useGrouping);
	}

	public boolean isUseGrouping() {
		return numberConverter.isUseGrouping();
	}

	public DecimalFormatSymbols getDecimalFormatSymbols() {
		return numberConverter.getDecimalFormatSymbols();
	}

	public void setDecimalFormatSymbols(
			DecimalFormatSymbols decimalFormatSymbols) {
		numberConverter.setDecimalFormatSymbols(decimalFormatSymbols);
	}

	public static enum Wildcard {
		GT(">"), LT("<"), GE(">="), LE("<="), NE("!=");

		private String sequence;

		Wildcard(String sequence) {
			this.sequence = sequence;
		}
	}

}
