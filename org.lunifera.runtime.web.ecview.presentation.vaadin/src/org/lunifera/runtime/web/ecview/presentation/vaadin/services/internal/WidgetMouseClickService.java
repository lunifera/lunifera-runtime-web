/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.services.IWidgetAssocationsService;
import org.lunifera.ecview.core.common.tooling.IWidgetMouseClickService;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;

@SuppressWarnings("serial")
public class WidgetMouseClickService implements IWidgetMouseClickService,
		LayoutClickListener {

	private Set<Listener> listeners = Collections
			.synchronizedSet(new HashSet<IWidgetMouseClickService.Listener>());
	private IWidgetAssocationsService associations;
	private IViewContext context;
	private LayoutClickNotifier widget;

	public WidgetMouseClickService(IViewContext context) {
		this.context = context;
		this.associations = context.getService(IWidgetAssocationsService.ID);
	}

	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	@Override
	public void activate() {
		widget = (LayoutEvents.LayoutClickNotifier) context.getViewEditpart()
				.getPresentation().getWidget();
		widget.addLayoutClickListener(this);
	}

	@Override
	public void dactivate() {
		if (widget != null) {
			widget.removeLayoutClickListener(this);
			widget = null;
		}
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		synchronized (listeners) {
			Object model = associations.getModelElement(event
					.getClickedComponent());
			for (Listener listener : listeners) {
				listener.clicked(model);
			}
		}
	}

}
