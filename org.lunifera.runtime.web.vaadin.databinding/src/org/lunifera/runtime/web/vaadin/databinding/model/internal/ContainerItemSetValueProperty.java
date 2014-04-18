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

package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.ListDiff;
import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.ISimplePropertyListener;

/**
 */
public class ContainerItemSetValueProperty extends AbstractModelListProperty {

	public ContainerItemSetValueProperty() {

	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new ContainerItemSetChangeListener(this, listener);
	}

	public Object getElementType() {
		return Object.class;
	}

	@Override
	protected List<?> doGetList(Object source) {
		// final Container. eObject = (EObject)source;
		// if (FeatureMapUtil.isMany(eObject, eStructuralFeature))
		// {
		// return (List< ? >)eObject.eGet(eStructuralFeature);
		// }
		// else
		// {
		// return
		// new AbstractSequentialList<Object>()
		// {
		// @Override
		// public ListIterator<Object> listIterator(int index)
		// {
		// ListIterator<Object> result =
		// new ListIterator<Object>()
		// {
		// protected int position = 0;
		// protected boolean setOrRemoveAllowed;
		// public void add(Object o)
		// {
		// if (position != 0)
		// {
		// throw new IllegalStateException();
		// }
		// eObject.eSet(eStructuralFeature, o);
		// position = 1;
		// setOrRemoveAllowed = false;
		// }
		//
		// public boolean hasNext()
		// {
		// return position == 0 && size() == 1;
		// }
		//
		// public boolean hasPrevious()
		// {
		// return position == 1;
		// }
		//
		// public Object next()
		// {
		// if (!hasNext())
		// {
		// throw new IllegalStateException();
		// }
		// ++position;
		// setOrRemoveAllowed = true;
		// return eObject.eGet(eStructuralFeature);
		// }
		//
		// public int nextIndex()
		// {
		// return position;
		// }
		//
		// public Object previous()
		// {
		// if (!hasPrevious())
		// {
		// throw new IllegalStateException();
		// }
		// else
		// {
		// --position;
		// setOrRemoveAllowed = true;
		// return eObject.eGet(eStructuralFeature);
		// }
		// }
		//
		// public int previousIndex()
		// {
		// return position - 1;
		// }
		//
		// public void remove()
		// {
		// if (!setOrRemoveAllowed)
		// {
		// throw new IllegalStateException();
		// }
		// else
		// {
		// setOrRemoveAllowed = false;
		// eObject.eUnset(eStructuralFeature);
		// }
		// }
		//
		// public void set(Object o)
		// {
		// if (!setOrRemoveAllowed)
		// {
		// throw new IllegalStateException();
		// }
		// else
		// {
		// setOrRemoveAllowed = false;
		// eObject.eSet(eStructuralFeature, o);
		// }
		// }
		// };
		// for (int i = 0; i < index; ++i)
		// {
		// result.next();
		// }
		// return result;
		return new ArrayList();
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void doSetList(Object source, List list, ListDiff diff) {
		List<?> currentList = doGetList(source);
		diff.applyTo(currentList);
	}
}