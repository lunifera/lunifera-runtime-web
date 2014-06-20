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

import java.lang.reflect.Method;
import java.util.Arrays;

import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.event.MethodEventSource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.util.ReflectTools;

/**
 */
@SuppressWarnings("serial")
public class ComponentListener extends NativePropertyListener implements
		Component.Listener {

	public static final Method COMPONENT_EVENT_METHOD = ReflectTools
			.findMethod(ComponentListener.class, "componentEvent",
					Component.Event.class);

	private final Class<? extends Component.Event>[] changeEvents;
	private final Class<? extends Component.Event>[] staleEvents;

	/**
	 * @param property
	 * @param listener
	 * @param changeEvents
	 * @param staleEvents
	 */
	public ComponentListener(IProperty property,
			ISimplePropertyListener listener,
			Class<? extends Component.Event>[] changeEvents,
			Class<? extends Component.Event>[] staleEvents) {
		super(property, listener);
		this.changeEvents = Arrays.copyOf(changeEvents, changeEvents.length);
		this.staleEvents = Arrays.copyOf(staleEvents, staleEvents.length);
	}

	@Override
	public void componentEvent(com.vaadin.ui.Component.Event event) {
		if (staleEvents != null) {
			for (int i = 0; i < staleEvents.length; i++) {
				if (event.getClass().isAssignableFrom(staleEvents[i])) {
					fireStale(event.getComponent());
					break;
				}
			}
		}
		if (changeEvents != null) {
			for (int i = 0; i < changeEvents.length; i++) {
				if (event.getClass().isAssignableFrom(changeEvents[i])) {
					fireChange(event.getComponent(), null);
					break;
				}
			}
		}
	}

	protected void doAddTo(Object source) {
		Component widget = (Component) source;
		if (changeEvents != null) {
			for (int i = 0; i < changeEvents.length; i++) {
				Class<? extends Event> event = changeEvents[i];
				if (event != null) {
					ComponentListenerUtil.asyncAddListener(
							(MethodEventSource) widget, event, this,
							COMPONENT_EVENT_METHOD);
				}
			}
		}
		if (staleEvents != null) {
			for (int i = 0; i < staleEvents.length; i++) {
				Class<? extends Event> event = staleEvents[i];
				if (event != null) {
					ComponentListenerUtil.asyncAddListener(
							(MethodEventSource) widget, event, this,
							COMPONENT_EVENT_METHOD);
				}
			}
		}
	}

	protected void doRemoveFrom(Object source) {
		Component widget = (Component) source;
		if (changeEvents != null) {
			for (int i = 0; i < changeEvents.length; i++) {
				Class<? extends Event> event = changeEvents[i];
				if (event != null) {
					ComponentListenerUtil.asyncRemoveListener(
							(MethodEventSource) widget, event, this,
							COMPONENT_EVENT_METHOD);
				}
			}
		}
		if (staleEvents != null) {
			for (int i = 0; i < staleEvents.length; i++) {
				Class<? extends Event> event = staleEvents[i];
				if (event != null) {
					ComponentListenerUtil.asyncRemoveListener(
							(MethodEventSource) widget, event, this,
							COMPONENT_EVENT_METHOD);
				}
			}
		}
	}

}