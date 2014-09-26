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

import java.util.Locale;

import org.lunifera.runtime.web.vaadin.common.IFilterProvider;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Component;

public class NumericFilterProperty extends FilterProperty {

	private String value;
	private Wildcard wildcard;

	public NumericFilterProperty(Component filterField, Object propertyId,
			Locale locale) {
		super(filterField, propertyId, locale);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;

		calculateWildcard(value);
	}

	@Override
	public Filter getFilter() {
		IFilterProvider filterProvider = getFilterProvider();
		if (filterProvider == null || getValue() == null) {
			return null;
		}
		if (wildcard == null || wildcard.equals("")) {
			return filterProvider.eq(getPropertyId(), getValue());
		} else {
			switch (wildcard) {
			case GE:
				return filterProvider.gteq(getPropertyId(), getValue());
			case GT:
				return filterProvider.gt(getPropertyId(), getValue());
			case LE:
				return filterProvider.lteq(getPropertyId(), getValue());
			case LT:
				return filterProvider.lt(getPropertyId(), getValue());
			case NE:
				return filterProvider.not(filterProvider.eq(getPropertyId(),
						getValue()));
			}
		}

		throw new IllegalStateException("Not a valid state!");
	}

	/**
	 * Calculates the wildcard.
	 * 
	 * @param value
	 */
	private void calculateWildcard(String value) {
		wildcard = null;
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

		if (wildcard != null) {
			this.value = value.replaceAll(wildcard.sequence, "");
		}
	}

	/**
	 * @return the wildcard
	 */
	public Wildcard getWildcard() {
		return wildcard;
	}

	public static enum Wildcard {
		GT(">"), LT("<"), GE(">="), LE("<="), NE("!=");

		private String sequence;

		Wildcard(String sequence) {
			this.sequence = sequence;
		}
	}

}
