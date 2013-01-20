/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas of org.eclipse.jface.databinding.swt (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding.component.internal;

import org.lunifera.runtime.web.vaadin.databinding.AbstractComponentValueProperty;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;

/**
 */
public class SplitPanelMaxSplitPositionUnitProperty extends
		AbstractComponentValueProperty {
	public SplitPanelMaxSplitPositionUnitProperty() {
		super();
	}

	public Object getValueType() {
		return Unit.class;
	}

	protected Object doGetValue(Object source) {
		AbstractSplitPanel component = (AbstractSplitPanel) source;
		return component.getMaxSplitPositionUnit();
	}

	protected void doSetValue(Object source, Object value) {
		AbstractSplitPanel component = (AbstractSplitPanel) source;
		component.setMaxSplitPosition(component.getMaxSplitPosition(),
				(Unit) value);
	}
}