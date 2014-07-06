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
package org.lunifera.runtime.web.vaadin.components.converter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.StringToDoubleConverter;

/**
 * A converter to format and parse Integer values.
 */
@SuppressWarnings("serial")
public class NumberConverter extends StringToDoubleConverter {
	private String numberFormatPattern;
	private boolean useGrouping;
	private DecimalFormatSymbols decimalFormatSymbols;
	private boolean customFormatSymbols;

	public NumberConverter() {
		this.numberFormatPattern = getDefaultFormat();
		this.decimalFormatSymbols = getDefaultFormatSymbols();
		this.useGrouping = getDefaultUseGrouping();
	}

	/**
	 * Returns the default value for use grouping.
	 * 
	 * @return
	 */
	protected boolean getDefaultUseGrouping() {
		return true;
	}

	/**
	 * Returns the default value for format symbols.
	 * 
	 * @return
	 */
	protected DecimalFormatSymbols getDefaultFormatSymbols() {
		return new DecimalFormatSymbols();
	}

	/**
	 * Returns the default value for default format.
	 * 
	 * @return
	 */
	protected String getDefaultFormat() {
		return "##,##0";
	}

	/**
	 * Sets the number format pattern that should be used to format the number.
	 * 
	 * @param numberFormatPattern
	 *            the numberFormatPattern to set
	 */
	protected void setNumberFormatPattern(String numberFormatPattern) {
		this.numberFormatPattern = numberFormatPattern;
	}

	/**
	 * Sets the {@link DecimalFormatSymbols} that should be used by the
	 * formatter.
	 * 
	 * @param decimalFormatSymbols
	 *            the decimalFormatSymbols to set
	 */
	public void setDecimalFormatSymbols(
			DecimalFormatSymbols decimalFormatSymbols) {
		this.decimalFormatSymbols = decimalFormatSymbols;

		if (decimalFormatSymbols != null) {
			customFormatSymbols = true;
		} else {
			customFormatSymbols = false;
		}
	}

	/**
	 * Returns the currently used number format pattern.
	 * 
	 * @return
	 */
	public String getNumberFormatPattern() {
		return numberFormatPattern;
	}

	/**
	 * Returns the currently used format symbols.
	 * 
	 * @return
	 */
	public DecimalFormatSymbols getDecimalFormatSymbols() {
		return decimalFormatSymbols;
	}

	/**
	 * If true, then grouping should be used. False otherwise. Default is true.
	 * 
	 * @return
	 */
	public boolean isUseGrouping() {
		return useGrouping;
	}

	/**
	 * If true, then grouping should be used. False otherwise. Default is true.
	 * 
	 * @param useGrouping
	 */
	public void setUseGrouping(boolean useGrouping) {
		this.useGrouping = useGrouping;
	}

	protected NumberFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		NumberFormat result = null;
		if (numberFormatPattern != null && !numberFormatPattern.equals("")) {
			if (decimalFormatSymbols != null && customFormatSymbols) {
				result = new DecimalFormat(numberFormatPattern,
						decimalFormatSymbols);
			} else {
				result = new DecimalFormat(numberFormatPattern,
						DecimalFormatSymbols.getInstance(locale));
			}
			result.setParseIntegerOnly(true);
			result.setRoundingMode(RoundingMode.HALF_EVEN);
		} else {
			result = NumberFormat.getIntegerInstance(locale);
		}

		result.setGroupingUsed(useGrouping);

		return result;
	}

}
