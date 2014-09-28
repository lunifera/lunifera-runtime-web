/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 * 		Hans Georg Glöckler - Initial implementation
 */
package org.lunifera.runtime.web.vaadin.components.fields.search;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.lunifera.runtime.web.vaadin.common.IFilterProvider;
import org.lunifera.runtime.web.vaadin.components.fields.search.filter.NumericFilterProperty;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.TextField;

/**
 * A numericfield specific for redvoodo.
 */
@SuppressWarnings("serial")
public class NumericSearchField extends TextField {

	private static final String NEGATIVE_VALUE = "lun-negative-value";

	private final StringToPropertyConverter converter;
	private boolean markNegative;
	@SuppressWarnings("unused")
	private String id;
	private Object propertyId;
	private Class<? extends Number> type;

	private NumericFilterProperty property;

	public NumericSearchField(String id, Object propertyId,
			Class<? extends Number> type) {
		this.id = id;
		this.propertyId = propertyId;
		this.type = type;

		setNullRepresentation("");
		setNullSettingAllowed(false);
		setImmediate(true);

		markNegative = true;
		// Important: Is responsible that the Converter is used in the Field
		this.converter = createConverter();
		setConverter(this.converter);
	}

	/**
	 * Creates a default converter.
	 * 
	 * @return
	 */
	protected StringToPropertyConverter createConverter() {
		this.property = new NumericFilterProperty(type, propertyId, getLocale());
		return new StringToPropertyConverter(propertyId, getLocale(), property);
	}

	/**
	 * Sets the Symbols which are used to Format.
	 * 
	 * @param decimalFormatSymbols
	 */
	public void setDecimalFormatSymbols(
			DecimalFormatSymbols decimalFormatSymbols) {
		converter.setDecimalFormatSymbols(decimalFormatSymbols);

		markAsDirty();
	}

	/**
	 * Returns the currently used decimal format symbols.
	 * 
	 * @return
	 */
	public DecimalFormatSymbols getDecimalFormatSymbols() {
		return converter.getDecimalFormatSymbols();
	}

	/**
	 * Returns true, if grouping is used. False otherwise.
	 * 
	 * @return
	 */
	public boolean isUseGrouping() {
		return converter.isUseGrouping();
	}

	/**
	 * Set true, if grouping should be used. False otherwise.
	 * 
	 * @param useGrouping
	 */
	public void setUseGrouping(boolean useGrouping) {
		converter.setUseGrouping(useGrouping);

		markAsDirty();
	}

	/**
	 * True, if negative values should become marked.
	 * 
	 * @param markNegative
	 */
	public void setMarkNegative(boolean markNegative) {
		this.markNegative = markNegative;

		handleNegative();
	}

	/**
	 * Returns true, if negative values should become marked.
	 * 
	 * @return
	 */
	public boolean isMarkNegative() {
		return markNegative;
	}

	protected void setInternalValue(String newValue) {
		super.setInternalValue(newValue);

		handleNegative();
	}

	/**
	 * Is called to handle the negative marker.
	 */
	protected void handleNegative() {
		removeStyleName(NEGATIVE_VALUE);

		if (!isMarkNegative()) {
			return;
		}

		if (property.getNumber() != null && property.getNumber().intValue() < 0) {
			addStyleName(NEGATIVE_VALUE);
		}
	}

	public void setFilterProvider(IFilterProvider filterProvider) {
		property.setFilterProvider(filterProvider);
	}
	
	public void setLocale(Locale locale){
		super.setLocale(locale);
		
		property.setLocale(locale);
	}

	public Object getFilter() {
		return property.getFilter();
	}

	public static class StringToPropertyConverter implements
			Converter<String, NumericFilterProperty> {

		private final NumericFilterProperty property;

		public StringToPropertyConverter(Object propertyId, Locale locale,
				NumericFilterProperty property) {
			this.property = property;
		}

		public void setUseGrouping(boolean useGrouping) {
			property.setUseGrouping(useGrouping);
		}

		public boolean isUseGrouping() {
			return property.isUseGrouping();
		}

		public DecimalFormatSymbols getDecimalFormatSymbols() {
			return property.getDecimalFormatSymbols();
		}

		public void setDecimalFormatSymbols(
				DecimalFormatSymbols decimalFormatSymbols) {
			property.setDecimalFormatSymbols(decimalFormatSymbols);
		}

		@Override
		public NumericFilterProperty convertToModel(String value,
				Class<? extends NumericFilterProperty> targetType, Locale locale)
				throws ConversionException {
			property.setStringValue(value);
			return property;
		}

		@Override
		public Class<NumericFilterProperty> getModelType() {
			return NumericFilterProperty.class;
		}

		@Override
		public String convertToPresentation(NumericFilterProperty value,
				Class<? extends String> targetType, Locale locale)
				throws ConversionException {
			if (value == null) {
				return null;
			}

			return value.getStringValue();
		}

		@Override
		public Class<String> getPresentationType() {
			return String.class;
		}

	}
}
