/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.common.internal;

import org.lunifera.runtime.web.common.context.AbstractWebContext;
import org.lunifera.runtime.web.vaadin.common.IVaadinWebContext;

import com.vaadin.ui.UI;

public class VaadinWebContext extends AbstractWebContext implements
		IVaadinWebContext {

	private UI ui;

	@Override
	public UI getUi() {
		return ui;
	}

	/**
	 * Used to set the reference to the ui.
	 * 
	 * @param ui
	 *            the ui to set
	 */
	public void setUi(UI ui) {
		if (this.ui != null) {
			throw new RuntimeException("Can only set ui once!");
		}
		this.ui = ui;
	}

	@Override
	public void syncExec(Runnable runnable) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void asyncExec(Runnable runnable) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
