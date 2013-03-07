/*******************************************************************************
 * Copyright (c) 2011 Florian Pirchner
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 *******************************************************************************/
package org.lunifera.runtime.web.ecview.presentation.vaadin;

import org.eclipse.emf.ecp.ecview.common.model.core.YEditable;
import org.eclipse.emf.ecp.ecview.common.model.core.YEnable;
import org.eclipse.emf.ecp.ecview.common.model.core.YVisibleable;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;

/**
 * A manager that is responsible for binding data for one view instance. Each
 * binding manager has to be connected to exactly one view. All the bindings
 * contained are bindings related to the associated view.
 */
public interface IBindingManager extends
		org.eclipse.emf.ecp.ecview.common.binding.IBindingManager {

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yVisibleAble
	 * @param field
	 */
	public abstract void bindVisible(YVisibleable yVisibleAble, AbstractComponent abstractComponent);

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yEnable
	 * @param field
	 */
	public abstract void bindEnabled(YEnable yEnable, AbstractComponent abstractComponent);

	/**
	 * Binds the visible option.
	 * 
	 * @param viewContext
	 * @param yEditable
	 * @param field
	 */
	public abstract void bindReadonly(YEditable yEditable,
			Property.ReadOnlyStatusChangeNotifier field);

}