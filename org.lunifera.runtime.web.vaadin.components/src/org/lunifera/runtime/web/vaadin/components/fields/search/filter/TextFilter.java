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

public class TextFilter extends Filter {

	private String value;
	private Wildcard wildcard;

	public TextFilter(Locale locale) {
		super(null, locale);
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

	/**
	 * Calculates the wildcard.
	 * 
	 * @param value
	 */
	private void calculateWildcard(String value) {
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
