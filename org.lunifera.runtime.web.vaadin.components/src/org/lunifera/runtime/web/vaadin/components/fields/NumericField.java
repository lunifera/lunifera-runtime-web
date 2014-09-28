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
package org.lunifera.runtime.web.vaadin.components.fields;

import java.text.DecimalFormatSymbols;

import org.lunifera.runtime.web.vaadin.components.converter.NumberConverter;

/**
 * A numericfield specific for redvoodo.
 */
@SuppressWarnings("serial")
public class NumericField extends TextField {

	private static final String NEGATIVE_VALUE = "lun-negative-value";
	private final NumberConverter converter;
	private boolean markNegative;

	public NumericField() {
		this(null);
	}

	public NumericField(String caption) {
		this(caption, null);
	}

	public NumericField(String caption, NumberConverter converter) {
		super(caption);

		setNullRepresentation("");
		setNullSettingAllowed(false);

		// Important: Is responsible that the Converter is used in the Field
		this.converter = converter != null ? converter : createConverter();
		setConverter(this.converter);
	}

	/**
	 * Creates a default converter.
	 * 
	 * @return
	 */
	protected NumberConverter createConverter() {
		return new NumberConverter();
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

		// try to find out if value is negative
		if (getPropertyDataSource() == null || isBuffered() || isModified()) {
			String value = getInternalValue();
			try {
				double result = converter.convertToModel(value, Double.class,
						getLocale());
				if (result < 0) {
					addStyleName(NEGATIVE_VALUE);
				}
			} catch (Exception e) {
				// nothing to do
			}
		} else {
			Object value = getPropertyDataSource().getValue();
			if (value != null) {
				double result = (Double) value;
				if (result < 0) {
					addStyleName(NEGATIVE_VALUE);
				}
			}
		}
	}
}
