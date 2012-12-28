/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.web.ecp.uimodel.presentation.vaadin;

/**
 * Constants about the swt simple presentation.
 */
// BEGIN SUPRESS CATCH EXCEPTION
public interface IConstants {
	// END SUPRESS CATCH EXCEPTION
	/**
	 * This CSS class is applied to the base composite of presentations. The base composite is used to enable
	 * margins on a control and to allow a control to rebuild its SWT controls at the "base composite parent".
	 */
	String CSS_CLASS__CONTROL_BASE = "controlbase";

	/**
	 * This CSS class is applied to the control of a presentation as far as the model element does not specify its own
	 * CSS class.
	 */
	public static final String CSS_CLASS__CONTROL = "control";

	/**
	 * This CSS class marks the widget to show a margin.
	 */
	public static final String CSS_CLASS__MARGIN = "margin";
	
	/**
	 * This CSS class marks the widget to show a spacing.
	 */
	public static final String CSS_CLASS__SPACING = "spacing";
}
