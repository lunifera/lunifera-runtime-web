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

package org.lunifera.runtime.web.vaadin.databinding.properties;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentListener;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.values.SimpleVaadinPropertyObservableValue;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableValueDecorator;

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
public abstract class AbstractVaadinValueProperty extends SimpleValueProperty
		implements IVaadinValueProperty {
	private Class<? extends Component.Event>[] changeEvents;
	private Class<? extends Component.Event>[] staleEvents;

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractVaadinValueProperty() {
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
	protected AbstractVaadinValueProperty(
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
	protected AbstractVaadinValueProperty(
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
	public AbstractVaadinValueProperty(
			Class<? extends Component.Event>[] changeEvents,
			Class<? extends Component.Event>[] staleEvents) {
		this.changeEvents = changeEvents;
		this.staleEvents = staleEvents;
	}

	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		if (changeEvents == null && staleEvents == null) {
			return null;
		}
		return new ComponentListener(this, listener, changeEvents, staleEvents);
	}

	public IVaadinObservableValue observe(Object source) {
		if (source instanceof Component) {
			return observe((Component) source);
		}
		return (IVaadinObservableValue) super.observe(source);
	}

	/**
	 * Is used to observe the com.vaadin.data.Property#value attribute.
	 * 
	 * @param realm
	 * @param source
	 * @return
	 */
	public IVaadinObservableValue observeVaadinProperty(Realm realm,
			Object source) {
		if (source instanceof com.vaadin.data.Property) {
			return wrapObservable(new SimpleVaadinPropertyObservableValue(
					realm, source, this));
		}
		return wrapObservable(super.observe(realm, source));
	}

	/**
	 * Is used to observe the com.vaadin.data.Property#value attribute.
	 * 
	 * @param realm
	 * @param source
	 * @return
	 */
	public IVaadinObservableValue observeVaadinProperty(Object source) {
		return observeVaadinProperty(Realm.getDefault(), source);
	}

	public IObservableValue observe(Realm realm, Object source) {
		return wrapObservable(super.observe(realm, source));
	}

	protected IVaadinObservableValue wrapObservable(IObservableValue observable) {
		return new VaadinObservableValueDecorator(observable);
	}

	public IVaadinObservableValue observe(Component widget) {
		return (IVaadinObservableValue) observe(
				VaadinObservables.getRealm(VaadinObservables.getUI(widget)),
				widget);
	}

	public IVaadinObservableValue observeDelayed(int delay, Component widget) {
		throw new UnsupportedOperationException("Delayed not allowed");
	}

	@Override
	public IVaadinObservableValue observeDelayed(int delay, Object component) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
