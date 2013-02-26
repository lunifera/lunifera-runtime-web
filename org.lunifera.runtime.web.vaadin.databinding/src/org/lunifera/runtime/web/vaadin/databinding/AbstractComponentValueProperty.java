/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Original source based on org.eclipse.jface.databinding.swt.WidgetValueProperty (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/

package org.lunifera.runtime.web.vaadin.databinding;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentListener;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.VaadinObservableComponentValueDecorator;

import com.vaadin.ui.Component;

/**
 * Abstract value property implementation for {@link Component} properties. This
 * class implements some basic behavior that widget properties are generally
 * expected to have, namely:
 * <ul>
 * <li>Calling {@link #observe(Object)} should create the observable on the UI
 * realm of the widget, rather than the current default realm
 * <li>All <code>observe()</code> methods should return an
 * {@link IVaadinComponentObservableValue}
 * </ul>
 * This class also provides a default widget listener implementation using
 * vaadins event concept. Subclasses may pass one or more vaadin event types
 * (sub class of {@link Component.Event}) to the super constructor to indicate
 * which events signal a property change.
 */
public abstract class AbstractComponentValueProperty extends
		SimpleValueProperty implements IComponentValueProperty {
	private Class<? extends Component.Event>[] changeEvents;
	private Class<? extends Component.Event>[] staleEvents;

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractComponentValueProperty() {
		this(null, null);
	}

	/**
	 * Constructs a ComponentValueProperty with the specified vaadin event type
	 * 
	 * @param changeEvent
	 *            vaadin event type of the event that signifies a property
	 *            change.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractComponentValueProperty(
			Class<? extends Component.Event> changeEvent) {
		this(new Class[] { changeEvent }, null);
	}

	/**
	 * Constructs a ComponentValueProperty with the specified vaadin event
	 * type(s).
	 * 
	 * @param changeEvents
	 *            array of vaadin event constants of the events that signify a
	 *            property change.
	 */
	protected AbstractComponentValueProperty(
			Class<? extends Component.Event>[] changeEvents) {
		this(changeEvents, null);
	}

	/**
	 * Constructs a ComponentValueProperty with the specified vaadin event
	 * types.
	 * 
	 * @param changeEvents
	 *            array of vaadin event types of the events that signify a
	 *            property change.
	 * @param staleEvents
	 *            array of vaadin event types of the events that signify a
	 *            property became stale.
	 */
	public AbstractComponentValueProperty(
			Class<? extends Component.Event>[] changeEvents,
			Class<? extends Component.Event>[] staleEvents) {
		this.changeEvents = changeEvents;
		this.staleEvents = staleEvents;
	}

	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		if (changeEvents == null && staleEvents == null)
			return null;
		return new ComponentListener(this, listener, changeEvents, staleEvents);
	}

	public IVaadinComponentObservableValue observe(Object source) {
		if (source instanceof Component) {
			return observe((Component) source);
		}
		return (IVaadinComponentObservableValue) super.observe(source);
	}

	public IObservableValue observe(Realm realm, Object source) {
		return wrapObservable(super.observe(realm, source), (Component) source);
	}

	protected IVaadinComponentObservableValue wrapObservable(
			IObservableValue observable, Component widget) {
		return new VaadinObservableComponentValueDecorator(observable, widget);
	}

	public IVaadinComponentObservableValue observe(Component widget) {
		return (IVaadinComponentObservableValue) observe(
				VaadinObservables.getRealm(VaadinObservables.getUI(widget)),
				widget);
	}

	public IVaadinComponentObservableValue observeDelayed(int delay,
			Component widget) {
		throw new UnsupportedOperationException("Delayed not allowed");
	}
}
