/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 *    
 *******************************************************************************/
package org.lunifera.web.vaadin.common;

import org.osgi.service.component.ComponentInstance;

import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public abstract class OSGiUI extends UI {

	private ComponentInstance instance;

	/**
	 * Sets the component instance that can be used to dispose the instance of
	 * that UI
	 * 
	 * @param instance
	 */
	public void setComponentInstance(ComponentInstance instance) {
		if (this.instance != null) {
			throw new IllegalArgumentException(
					"Component instance may only be set onece!");
		}
		this.instance = instance;
	}

	@Override
	public void detach() {
		super.detach();

		dispose();
	}

	/**
	 * Is called to remove the instance as an OSGi service and to cleanup the
	 * OSGi runtime.
	 */
	protected void dispose() {
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}

}
