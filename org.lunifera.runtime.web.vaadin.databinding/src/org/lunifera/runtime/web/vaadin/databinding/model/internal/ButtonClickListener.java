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
package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import java.util.Date;

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 */
@SuppressWarnings("serial")
public class ButtonClickListener extends NativePropertyListener implements
		Button.ClickListener {

	private long lastActivation;

	public ButtonClickListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		if (source instanceof Button) {
			Button notifier = (Button) source;
			notifier.addClickListener(this);
		}
	}

	protected void doRemoveFrom(Object source) {
		if (source instanceof Button) {
			Button notifier = (Button) source;
			notifier.removeClickListener(this);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// on button click, just send the current time in ms to the receiver
		lastActivation = new Date().getTime();
		fireChange(lastActivation, null);
	}

	/**
	 * Returns the time of the last activation.
	 * 
	 * @return
	 */
	public long getLastActivation() {
		return lastActivation;
	}

}