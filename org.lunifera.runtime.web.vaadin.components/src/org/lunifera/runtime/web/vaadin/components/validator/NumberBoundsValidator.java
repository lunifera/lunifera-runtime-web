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
package org.lunifera.runtime.web.vaadin.components.validator;

import java.math.BigDecimal;

import com.vaadin.data.validator.AbstractValidator;

@SuppressWarnings("serial")
public class NumberBoundsValidator extends AbstractValidator<Number> {

	private BigDecimal maxValue;
	private boolean includeEqual_max;
	private BigDecimal minValue;
	private boolean includeEqual_min;

	private BigDecimal exactValue;
	private boolean exact;

	public NumberBoundsValidator(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Only values that are greater than the given value will be accepted by
	 * that validator. Values that do not match that constraint will be declined
	 * and force and error.
	 * 
	 * @param value
	 *            the value to set
	 * @return
	 */
	public NumberBoundsValidator greater(Number value) {
		resetExact();

		this.minValue = new BigDecimal(value.doubleValue());
		includeEqual_min = false;
		return this;
	}

	/**
	 * Only values that are greater equal than the given value will be accepted
	 * by that validator. Values that do not match that constraint will be
	 * declined and force and error.
	 * 
	 * @param value
	 *            the value to set
	 * @return
	 */
	public NumberBoundsValidator greaterEqual(Number value) {
		resetExact();

		this.minValue = new BigDecimal(value.doubleValue());
		includeEqual_min = true;
		return this;
	}

	/**
	 * Only values that are lower than the given value will be accepted by that
	 * validator. Values that do not match that constraint will be declined and
	 * force and error.
	 * 
	 * @param value
	 *            the value to set
	 * @return
	 */
	public NumberBoundsValidator lower(Number value) {
		resetExact();

		this.maxValue = new BigDecimal(value.doubleValue());
		includeEqual_max = false;
		return this;
	}

	/**
	 * Only values that are lower equal than the given value will be accepted by
	 * that validator. Values that do not match that constraint will be declined
	 * and force and error.
	 * 
	 * @param value
	 *            the value to set
	 * @return
	 */
	public NumberBoundsValidator lowerEqual(Number value) {
		resetExact();

		this.maxValue = new BigDecimal(value.doubleValue());
		includeEqual_max = true;
		return this;
	}

	/**
	 * Only values that are equal the given value will be accepted by that
	 * validator. Values that are greater or lower will be declined and force
	 * and error.
	 * 
	 * @param value
	 *            the value to set
	 * @return
	 */
	public NumberBoundsValidator equal(Number value) {
		maxValue = null;
		includeEqual_max = false;
		minValue = null;
		includeEqual_min = false;

		exactValue = new BigDecimal(value.doubleValue());
		exact = true;
		return this;
	}

	/**
	 * Resets the internal "exact" state of that validator
	 */
	private void resetExact() {
		exactValue = null;
		exact = false;
	}

	/**
	 * @return the max
	 */
	public Number getMax() {
		return maxValue;
	}

	/**
	 * @return the min
	 */
	public Number getMin() {
		return minValue;
	}

	@Override
	public boolean isValidValue(Number value) {

		BigDecimal input = new BigDecimal(value.doubleValue());
		boolean result = true;

		if (exact) {
			result = exactValue.compareTo(input) == 0;
		} else {
			if (maxValue != null) {
				int checkResult = maxValue.compareTo(input);
				if (includeEqual_max) {
					result = result && checkResult >= 0;
				} else {
					result &= checkResult == 1;
				}
			}

			if (minValue != null) {
				int checkResult = minValue.compareTo(input);
				if (includeEqual_min) {
					result &= checkResult <= 0;
				} else {
					result &= checkResult == -1;
				}
			}
		}

		return result;
	}

	@Override
	public Class<Number> getType() {
		return Number.class;
	}

}
