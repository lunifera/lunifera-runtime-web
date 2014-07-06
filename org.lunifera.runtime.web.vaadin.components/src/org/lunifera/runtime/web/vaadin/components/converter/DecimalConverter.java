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
 * A converter used to format and parse Decimal values.
 */
@SuppressWarnings("serial")
public class DecimalConverter extends StringToDoubleConverter {
	private boolean integerInstance;
	private String numberFormatPattern;
	private boolean useGrouping;
	private DecimalFormatSymbols decimalFormatSymbols;
	private boolean customFormatSymbols;
	private int precision;

	public DecimalConverter() {
		this(false);
	}

	public DecimalConverter(boolean integerInstance) {
		this.integerInstance = integerInstance;
		this.numberFormatPattern = getDefaultFormat();
		this.decimalFormatSymbols = getDefaultFormatSymbols();
		this.precision = getDefaultPrecision();
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
	 * Returns the default value for precision.
	 * 
	 * @return
	 */
	protected int getDefaultPrecision() {
		return 2;
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
		return "##,##0.00";
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

	/**
	 * Returns the precision of that decimal field.
	 * 
	 * @return
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Sets the precision of that decimal field.
	 * 
	 * @param precision
	 */
	public void setPrecision(int precision) {
		this.precision = precision;

		updateNumberFormat();
	}

	/**
	 * Sets the number format pattern to be used for formatting.
	 */
	protected void updateNumberFormat() {
		String format = "##,##0";

		if (precision > 0) {
			format = format.concat(".");
		}
		for (int i = 0; i < precision; i++) {
			format = format.concat("0");
		}

		setNumberFormatPattern(format);
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

			if (integerInstance) {
				result.setParseIntegerOnly(true);
				result.setRoundingMode(RoundingMode.HALF_EVEN);
			}
		} else {
			if (integerInstance) {
				result = NumberFormat.getIntegerInstance(locale);
			} else {
				result = NumberFormat.getNumberInstance(locale);
			}
		}

		result.setGroupingUsed(useGrouping);

		return result;
	}

}
