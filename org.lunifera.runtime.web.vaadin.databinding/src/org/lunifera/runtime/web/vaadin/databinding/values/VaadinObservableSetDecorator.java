package org.lunifera.runtime.web.vaadin.databinding.values;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.set.DecoratingObservableSet;
import org.eclipse.core.databinding.observable.set.IObservableSet;

public class VaadinObservableSetDecorator extends DecoratingObservableSet
		implements IVaadinObservableSet {

	/**
	 * @param decorated
	 * @param eStructuralFeature
	 */
	public VaadinObservableSetDecorator(IObservableSet decorated) {
		super(decorated, true);
	}

	public Object getObserved() {
		IObservable decorated = getDecorated();
		if (decorated instanceof IObserving) {
			return ((IObserving) decorated).getObserved();
		}
		return null;
	}

	@Override
	public Object getSource() {
		return getObserved();
	}

}
