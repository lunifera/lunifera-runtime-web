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
package org.lunifera.runtime.web.vaadin.common.sharedState;

import java.util.Map;

import org.lunifera.runtime.common.state.ISharedStateContext;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Vaadin util to find a shared state if available.
 */
public class SharedStateUtil {

	/**
	 * Tries to find the {@link ISharedStateContext} registered with the Vaadin
	 * component. It also iterates the parents recursively to find it.
	 * 
	 * @param component
	 * @return
	 */
	public static ISharedStateContext getSharedState(Component component) {
		if (component == null) {
			return null;
		}
		if (component instanceof AbstractComponent) {
			AbstractComponent comp = (AbstractComponent) component;
			if (comp.getData() != null && (comp.getData() instanceof Map)) {
				@SuppressWarnings("unchecked")
				Map<Object, Object> map = (Map<Object, Object>) comp.getData();
				ISharedStateContext sharedState = (ISharedStateContext) map
						.get(ISharedStateContext.class);
				if (sharedState != null) {
					return sharedState;
				}
			}
		}
		return getSharedState(component.getParent());
	}

}
