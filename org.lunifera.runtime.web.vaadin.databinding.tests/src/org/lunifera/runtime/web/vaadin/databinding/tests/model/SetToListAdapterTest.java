/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * 		Florian Pirchner - Initial implementation
 */

package org.lunifera.runtime.web.vaadin.databinding.tests.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.vaadin.databinding.values.SetToListAdapter;

public class SetToListAdapterTest {

	@Before
	public void setup() {
		new TestRealm();
	}

	@Test
	public void test() {
		WritableSet set = new WritableSet(TestRealm.getDefault());
		SetToListAdapter adapter = new SetToListAdapter(set, null);

		ListChangeListener listener = new ListChangeListener();
		adapter.addListChangeListener(listener);

		set.add("Huhu");
		ListDiffEntry e1 = listener.event.diff.getDifferences()[0];
		assertTrue(e1.isAddition());
		assertEquals("Huhu", e1.getElement());
		assertEquals(1, adapter.size());

		set.add("Haha");
		e1 = listener.event.diff.getDifferences()[0];
		assertTrue(e1.isAddition());
		assertEquals("Haha", e1.getElement());
		assertEquals(1, e1.getPosition());
		assertEquals(2, adapter.size());

		set.add("Blabla");
		e1 = listener.event.diff.getDifferences()[0];
		assertTrue(e1.isAddition());
		assertEquals("Blabla", e1.getElement());
		assertEquals(2, e1.getPosition());
		assertEquals(3, adapter.size());

		List<String> added = Arrays.asList("Blublu", "Foo", "Bar");
		set.addAll(added);
		e1 = listener.event.diff.getDifferences()[0];
		ListDiffEntry e2 = listener.event.diff.getDifferences()[1];
		ListDiffEntry e3 = listener.event.diff.getDifferences()[2];
		assertEquals(3, listener.event.diff.getDifferences().length);
		assertEquals(6, adapter.size());
		assertTrue(e1.isAddition());
		assertTrue(e2.isAddition());
		assertTrue(e3.isAddition());
		assertTrue(added.contains(e1.getElement()));
		assertTrue(added.contains(e2.getElement()));
		assertTrue(added.contains(e3.getElement()));
		assertTrue(e1.getPosition() >= 3 && e1.getPosition() <= 5);
		assertTrue(e2.getPosition() >= 3 && e1.getPosition() <= 5);
		assertTrue(e3.getPosition() >= 3 && e1.getPosition() <= 5);

		set.remove("Haha");
		e1 = listener.event.diff.getDifferences()[0];
		assertTrue(!e1.isAddition());
		assertEquals("Haha", e1.getElement());
		assertEquals(1, e1.getPosition());
		assertEquals(5, adapter.size());

		set.removeAll(added);
		e1 = listener.event.diff.getDifferences()[0];
		e2 = listener.event.diff.getDifferences()[1];
		e3 = listener.event.diff.getDifferences()[2];
		assertEquals(3, listener.event.diff.getDifferences().length);
		assertEquals(2, adapter.size());
		assertTrue(!e1.isAddition());
		assertTrue(!e2.isAddition());
		assertTrue(!e3.isAddition());
		assertTrue(added.contains(e1.getElement()));
		assertTrue(added.contains(e2.getElement()));
		assertTrue(added.contains(e3.getElement()));
		assertTrue(e1.getPosition() >= 2 && e1.getPosition() <= 4);
		assertTrue(e2.getPosition() >= 2 && e1.getPosition() <= 4);
		assertTrue(e3.getPosition() >= 2 && e1.getPosition() <= 4);

	}

	private static class ListChangeListener implements IListChangeListener {

		private ListChangeEvent event;

		@Override
		public void handleListChange(ListChangeEvent event) {
			this.event = event;
		}

	}

	private static class TestRealm extends Realm {

		private TestRealm() {
			setDefault(this);
		}

		public boolean isCurrent() {
			return true;
		}

		public void asyncExec(final Runnable runnable) {
			throw new UnsupportedOperationException("Not a valid call!");
		}

		public void timerExec(int milliseconds, final Runnable runnable) {
			throw new UnsupportedOperationException("Not a valid call!");
		}
	}
}
