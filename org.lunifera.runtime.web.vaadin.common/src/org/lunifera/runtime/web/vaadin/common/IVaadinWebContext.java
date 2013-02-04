/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.common;

import org.lunifera.runtime.web.common.IWebContext;

import com.vaadin.ui.UI;

public interface IVaadinWebContext extends IWebContext {

	/**
	 * Returns the root UI element that context is registered with.
	 * 
	 * @return the ui
	 */
	public abstract UI getUi();

	/**
	 * To provide web contexts.
	 */
	public interface Provider {

		/**
		 * Returns the instance of web context.
		 * 
		 * @return
		 */
		IVaadinWebContext getWebContext();
	}

}