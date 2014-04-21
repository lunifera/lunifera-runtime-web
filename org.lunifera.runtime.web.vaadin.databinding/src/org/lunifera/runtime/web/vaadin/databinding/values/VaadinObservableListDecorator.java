package org.lunifera.runtime.web.vaadin.databinding.values;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.list.DecoratingObservableList;
import org.eclipse.core.databinding.observable.list.IObservableList;

public class VaadinObservableListDecorator extends DecoratingObservableList
		implements IVaadinObservableList {

	/**
	 * @param decorated
	 * @param eStructuralFeature
	 */
	public VaadinObservableListDecorator(IObservableList decorated) {
		super(decorated, true);
	}

	public Object getObserved() {
		IObservable decorated = getDecorated();
		if (decorated instanceof IObserving)
			return ((IObserving) decorated).getObserved();
		return null;
	}

	@Override
	public Object getSource() {
		return getObserved();
	}

}
