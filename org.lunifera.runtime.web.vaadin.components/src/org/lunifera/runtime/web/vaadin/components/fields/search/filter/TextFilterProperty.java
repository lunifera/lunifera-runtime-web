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

public class TextFilterProperty extends FilterProperty {

	private String value;
	private Wildcard wildcard;

	public TextFilterProperty(Object propertyId, Locale locale) {
		super(propertyId, locale);
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
		if (filterProvider == null || getValue() == null
				|| getValue().equals("")) {
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
			case ANY:
				return filterProvider.like(getPropertyId(), getValue(), true);
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
		} else if (value.contains(Wildcard.ANY.sequence)) {
			wildcard = Wildcard.ANY;
		}

		if (wildcard != null) {
			// remove the wildcard from the value and change * to %
			if (wildcard == Wildcard.ANY) {
				this.value = value.replaceAll("\\*", "%").trim();
			} else {
				this.value = value.replaceAll(wildcard.sequence, "").trim();
			}
		}
	}

	/**
	 * @return the wildcard
	 */
	public Wildcard getWildcard() {
		return wildcard;
	}

	public static enum Wildcard {
		GT(">"), LT("<"), GE(">="), LE("<="), NE("!="), ANY("*");

		private String sequence;

		Wildcard(String sequence) {
			this.sequence = sequence;
		}
	}

}
