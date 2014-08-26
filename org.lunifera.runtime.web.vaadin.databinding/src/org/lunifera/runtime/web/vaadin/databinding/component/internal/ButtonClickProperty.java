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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.lunifera.runtime.web.vaadin.databinding.model.internal.ButtonClickListener;
import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

import com.vaadin.ui.Button;

/**
 * Button click property uses the current Date#time value to represent the last
 * time a button was clicked. Using the boolean would not meet the objectives.
 */
public class ButtonClickProperty extends AbstractVaadinValueProperty {

	private ButtonClickListener clickListener;

	public String toString() {
		return "ButtonClickProperty"; //$NON-NLS-1$
	}

	public ButtonClickProperty() {
		super();
	}

	public Object getValueType() {
		return Long.class;
	}

	protected Object doGetValue(Object source) {
		return clickListener.getLastActivation();
	}

	protected void doSetValue(Object source, Object value) {
		Button component = (Button) source;
		component.click();
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		clickListener = new ButtonClickListener(this, listener);
		return clickListener;
	}
}