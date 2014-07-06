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
package org.lunifera.runtime.web.vaadin.databinding.values;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SetToListAdapter extends WritableList implements
		IVaadinObservableList, IListChangeListener {

	private final IObservableSet set;
	private final Object source;

	private ISetChangeListener setListener = new ISetChangeListener() {
		public void handleSetChange(SetChangeEvent event) {
			List originalList = new ArrayList(wrappedList);
			for (Object addedElement : event.diff.getAdditions()) {
				if (!wrappedList.contains(addedElement)) {
					wrappedList.add(addedElement);
				}
			}
			for (Object removedElement : event.diff.getRemovals()) {
				wrappedList.remove(removedElement);
			}
			fireListChange(Diffs.computeListDiff(originalList, wrappedList));
		}
	};

	/**
	 * @param notifier
	 * @param list
	 */
	public SetToListAdapter(IObservableSet set, Object source) {
		super(set.getRealm(), new ArrayList(), set.getElementType());
		this.set = set;
		this.source = source;
		wrappedList.addAll(set);
		this.set.addSetChangeListener(setListener);
		this.addListChangeListener(this);
	}

	public synchronized void dispose() {
		super.dispose();

		this.removeListChangeListener(this);

		if (set != null && setListener != null) {
			set.removeSetChangeListener(setListener);
			setListener = null;
		}
	}

	@Override
	public Object getSource() {
		return source;
	}

	/**
	 * Observes changes of this instance of the list and transforms it to
	 * changes for the set. Set listeners are beeing detached during update.
	 */
	@Override
	public void handleListChange(ListChangeEvent event) {

		try {
			Set<Object> addons = new HashSet<Object>();
			Set<Object> removals = new HashSet<Object>();

			set.removeSetChangeListener(setListener);
			ListDiffEntry[] differences = event.diff.getDifferences();
			for (int i = 0; i < differences.length; i++) {
				ListDiffEntry entry = differences[i];
				Object element = entry.getElement();
				if (entry.isAddition()) {
					addons.add(element);
				} else {
					removals.add(element);
				}
			}

			// remove double entries
			addons.removeAll(removals);

			set.addAll(addons);
			set.removeAll(removals);
		} finally {
			set.addSetChangeListener(setListener);
		}
	}

}
