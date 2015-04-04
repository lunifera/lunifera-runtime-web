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

package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding;

import org.lunifera.ecview.core.common.context.IContext;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.disposal.IDisposable;
import org.lunifera.ecview.core.common.services.IServiceProvider;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Component;

public class BindingManagerProvider implements IServiceProvider {

	@Override
	public boolean isFor(String selector, IContext context) {
		if (context instanceof IViewContext) {
			if (!org.lunifera.ecview.core.common.binding.IECViewBindingManager.class
					.getName().equals(selector)) {
				return false;
			}
			IViewContext viewContext = (IViewContext) context;
			if (!VaadinRenderer.UI_KIT_URI.equals(viewContext
					.getPresentationURI())) {
				return false;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> A createService(String selector, IContext context) {
		IViewContext viewContext = (IViewContext) context;
		final BindingManager bindingManager = new BindingManager(viewContext,
				VaadinObservables.getRealm(VaadinObservables
						.getUI((Component) viewContext.getRootLayout())));
		viewContext.addDisposeListener(new IDisposable.Listener() {
			@Override
			public void notifyDisposed(IDisposable notifier) {
				bindingManager.dispose();
			}
		});
		return (A) bindingManager;
	}

}
