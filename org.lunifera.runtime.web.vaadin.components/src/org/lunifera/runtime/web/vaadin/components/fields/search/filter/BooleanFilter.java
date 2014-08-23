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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.vaadin.ui.Component;

public class BooleanFilter extends Filter {

	private Option selection;
	private List<OptionBean> options;
	private OptionBean defaultOption;

	public BooleanFilter(Locale locale) {
		super(null, locale);
	}

	/**
	 * @return the selection
	 */
	public Option getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setSelection(Option selection) {
		this.selection = selection;
	}

	/**
	 * Returns all available options.
	 * 
	 * @param locale
	 * @return
	 */
	public List<OptionBean> getOptions() {
		if (options == null) {
			options = Collections
					.unmodifiableList(new ArrayList<BooleanFilter.OptionBean>());
			options.add(new OptionBean(Option.TRUE, getOptionsName(Option.TRUE,
					getLocale())));
			options.add(new OptionBean(Option.FALSE, getOptionsName(
					Option.FALSE, getLocale())));
			defaultOption = new OptionBean(Option.IGNORE, getOptionsName(
					Option.IGNORE, getLocale()));
			options.add(defaultOption);
		}
		return options;
	}

	private static String getOptionsName(Option option, Locale locale) {
		return option.name();
	}

	/**
	 * Returns the default selection.
	 * 
	 * @return
	 */
	public OptionBean getDefaultOption() {
		return defaultOption;
	}

	public static enum Option {
		TRUE, FALSE, IGNORE;
	}

	public static class OptionBean {

		private final Option option;
		private final String description;

		public OptionBean(Option option, String description) {
			super();
			this.option = option;
			this.description = description;
		}

		public Option getOption() {
			return option;
		}

		public String getDescription() {
			return description;
		}

	}

}
