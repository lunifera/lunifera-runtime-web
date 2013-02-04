/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.common;

import com.vaadin.ui.UI;

public class ContextUtil {

	/**
	 * Returns the vaadin webcontext if it is available. Otherwise
	 * <code>null</code> is returned.
	 * 
	 * @return
	 */
	public static IVaadinWebContext getCurrentContext() {
		UI ui = UI.getCurrent();
		if (ui instanceof IVaadinWebContext.Provider) {
			return ((IVaadinWebContext.Provider) ui).getWebContext();
		}
		return null;
	}

}
