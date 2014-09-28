/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas from Eclipse Databinding.
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.vaadin.databinding.properties;

import java.util.Arrays;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.list.SimpleListProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentListener;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableList;
import org.lunifera.runtime.web.vaadin.databinding.values.SimpleVaadinPropertyObservableList;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableListDecorator;

import com.vaadin.ui.Component;

public abstract class AbstractVaadinListProperty extends SimpleListProperty {

	private Class<? extends Component.Event>[] changeEvents;
	private Class<? extends Component.Event>[] staleEvents;

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractVaadinListProperty() {
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
	protected AbstractVaadinListProperty(
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
	protected AbstractVaadinListProperty(
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
	public AbstractVaadinListProperty(
			Class<? extends Component.Event>[] changeEvents,
			Class<? extends Component.Event>[] staleEvents) {
		this.changeEvents = changeEvents != null ? Arrays.copyOf(changeEvents,
				changeEvents.length) : changeEvents;
		this.staleEvents = staleEvents != null ? Arrays.copyOf(staleEvents,
				staleEvents.length) : staleEvents;
	}

	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		if (changeEvents == null && staleEvents == null) {
			return null;
		}
		return new ComponentListener(this, listener, changeEvents, staleEvents);
	}

	public IVaadinObservableList observe(Object source) {
		if (source instanceof Component) {
			return observe((Component) source);
		}
		return (IVaadinObservableList) super.observe(source);
	}

	public IObservableList observe(Realm realm, Object source) {
		return wrapObservable(new SimpleVaadinPropertyObservableList(realm,
				source, this), source);
	}

	protected IVaadinObservableList wrapObservable(IObservableList observable,
			Object widget) {
		return new VaadinObservableListDecorator(observable);
	}

	public IVaadinObservableList observe(Component widget) {
		return (IVaadinObservableList) observe(
				VaadinObservables.getRealm(VaadinObservables.getUI(widget)),
				widget);
	}

}
