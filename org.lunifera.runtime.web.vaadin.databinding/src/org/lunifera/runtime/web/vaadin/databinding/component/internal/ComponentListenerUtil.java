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

import java.lang.reflect.Method;

import com.vaadin.event.MethodEventSource;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 */
public class ComponentListenerUtil {
	/**
	 * @param widget
	 * @param event
	 * @param listener
	 */
	public static void asyncAddListener(final MethodEventSource widget,
			final Class<? extends Component.Event> event,
			final Component.Listener listener, Method listenerMethod) {
		if (widget == null) {
			return;
		}

		UI ui = UI.getCurrent();
		if (ui != null) {
			widget.addListener(event, listener, listenerMethod);
		} else {
			throw new IllegalStateException("Not a valid web transaction");
		}
	}

	/**
	 * @param widget
	 * @param event
	 * @param listener
	 */
	public static void asyncRemoveListener(final MethodEventSource widget,
			final Class<? extends Component.Event> event,
			final Component.Listener listener, Method listenerMethod) {
		if (widget == null) {
			return;
		}

		UI ui = UI.getCurrent();
		if (ui != null) {
			widget.removeListener(event, listener, listenerMethod);
		} else {
			throw new IllegalStateException("Not a valid web transaction");
		}
	}
}
