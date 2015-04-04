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

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.lunifera.runtime.web.vaadin.databinding.properties.AbstractVaadinValueProperty;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;

/**
 */
public class SplitPanelMaxSplitPositionProperty extends
		AbstractVaadinValueProperty {
	public String toString() {
		return "SplitPanelMaxSplitPositionProperty"; //$NON-NLS-1$
	}

	public SplitPanelMaxSplitPositionProperty() {
		super();
	}

	public Object getValueType() {
		return Integer.class;
	}

	protected Object doGetValue(Object source) {
		AbstractSplitPanel component = (AbstractSplitPanel) source;
		return component.getMaxSplitPosition();
	}

	protected void doSetValue(Object source, Object value) {
		AbstractSplitPanel component = (AbstractSplitPanel) source;
		Unit unit = component.getMaxSplitPositionUnit();

		int pos = (Integer) value;
		if (unit != Unit.PIXELS && unit != Unit.PERCENTAGE) {
			if (pos <= 100) {
				unit = Unit.PERCENTAGE;
			} else {
				unit = Unit.PIXELS;
			}
		}
		component.setMaxSplitPosition((Integer) value, unit);
	}
}