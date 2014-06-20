package org.lunifera.runtime.web.vaadin.databinding.properties;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.set.SimpleSetProperty;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.lunifera.runtime.web.vaadin.databinding.component.internal.ComponentListener;
import org.lunifera.runtime.web.vaadin.databinding.values.IVaadinObservableSet;
import org.lunifera.runtime.web.vaadin.databinding.values.VaadinObservableSetDecorator;

import com.vaadin.ui.Component;

public abstract class AbstractVaadinSetProperty extends SimpleSetProperty {

	private Class<? extends Component.Event>[] changeEvents;
	private Class<? extends Component.Event>[] staleEvents;

	/**
	 * Constructs a ComponentValueProperty which does not listen for any vaadin
	 * events.
	 */
	protected AbstractVaadinSetProperty() {
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
	protected AbstractVaadinSetProperty(
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
	protected AbstractVaadinSetProperty(
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
	public AbstractVaadinSetProperty(
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

	public IVaadinObservableSet observe(Object source) {
		if (source instanceof Component) {
			return observe((Component) source);
		}
		return (IVaadinObservableSet) super.observe(source);
	}

	public IObservableSet observe(Realm realm, Object source) {
		return wrapObservable(super.observe(realm, source), (Component) source);
	}

	protected IVaadinObservableSet wrapObservable(IObservableSet observable,
			Object widget) {
		return new VaadinObservableSetDecorator(observable);
	}

	public IVaadinObservableSet observe(Component widget) {
		return (IVaadinObservableSet) observe(
				VaadinObservables.getRealm(VaadinObservables.getUI(widget)),
				widget);
	}

}
