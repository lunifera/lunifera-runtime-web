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
package org.lunifera.runtime.web.ecview.presentation.vaadin;

/**
 * Constants about the swt simple presentation.
 */
// BEGIN SUPRESS CATCH EXCEPTION
public interface IConstants {
	// END SUPRESS CATCH EXCEPTION
	/**
	 * This CSS class is applied to the base composite of presentations. The
	 * base composite is used to enable margins on a control and to allow a
	 * control to rebuild its SWT controls at the "base composite parent".
	 */
	String CSS_CLASS_CONTROL_BASE = "l-controlbase";

	/**
	 * This CSS class is applied to the control of a presentation as far as the
	 * model element does not specify its own CSS class.
	 */
	public static final String CSS_CLASS_CONTROL = "l-control";
	
	/**
	 * This CSS class is applied to searchfields of a presentation as far as the
	 * model element does not specify its own CSS class.
	 */
	public static final String CSS_CLASS_SEARCHFIELD = "l-searchfield";

	/**
	 * This CSS class marks the widget to show a margin.
	 */
	public static final String CSS_CLASS_MARGIN = "margin";

	/**
	 * This CSS class marks the widget to show a spacing.
	 */
	public static final String CSS_CLASS_SPACING = "spacing";

	/**
	 * This CSS class for master-detail master-base component.
	 */
	String CSS_CLASS_MASTER_BASE = "l-masterbase";

	/**
	 * This CSS class for master-detail detail-base component.
	 */
	String CSS_CLASS_DETAIL_BASE = "l-detailbase";

	/**
	 * This CSS class is used to mark the compressor element. Compressor
	 * elements are used to avoid elements from grapping access space during
	 * window size changes.
	 */
	String CSS_CLASS_COMPRESSOR = "l-layout-compressor";

	// css classes for the different layouts
	String CSS_CLASS_FORMLAYOUT = "l-formlayout";
	String CSS_CLASS_GRIDLAYOUT = "l-gridlayout";
	String CSS_CLASS_VERTICALLAYOUT = "l-verticallayout";
	String CSS_CLASS_HORIZONTALLAYOUT = "l-horizontallayout";
	
	
	String I18N_TOOLTIP_TEXTSEARCHFIELD = "org.lunifera.ecview.core.common.i18n.TextSearchFieldTooltip";
	String I18N_TOOLTIP_BOOLEANSEARCHFIELD = "org.lunifera.ecview.core.common.i18n.BooleanSearchFieldTooltip";
	String I18N_TOOLTIP_NUMBERSEARCHFIELD = "org.lunifera.ecview.core.common.i18n.NumberSearchFieldTooltip";
	String I18N_TOOLTIP_REFERENCESEARCHFIELD = "org.lunifera.ecview.core.common.i18n.ReferenceSearchFieldTooltip";
	
}
