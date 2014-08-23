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
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.presentation.ILayoutPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTabSheet;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabSheetEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabSheetPresentation;

import com.vaadin.ui.ComponentContainer;

/**
 * An abstract base class implementing {@link ILayoutPresentation}.
 */
public abstract class AbstractTabSheetPresenter<A extends ComponentContainer>
		extends AbstractVaadinWidgetPresenter<A> implements
		ITabSheetPresentation<A> {

	private List<ITabEditpart> tabs;

	public AbstractTabSheetPresenter(ITabSheetEditpart editpart) {
		super(editpart);
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return the editpart
	 */
	protected ITabSheetEditpart getEditpart() {
		return (ITabSheetEditpart) super.getEditpart();
	}

	@Override
	public YTabSheet getModel() {
		return (YTabSheet) getEditpart().getModel();
	}

	/**
	 * Returns the view context.
	 * 
	 * @return viewContext
	 */
	public IViewContext getViewContext() {
		return getEditpart().getView().getContext();
	}

	@Override
	public List<ITabEditpart> getTabs() {
		return tabs != null ? Collections.unmodifiableList(tabs) : Collections
				.<ITabEditpart> emptyList();
	}

	@Override
	public boolean contains(ITabEditpart editpart) {
		return tabs != null && tabs.contains(editpart);
	}

	@Override
	public void add(ITabEditpart editpart) {
		ensureChildren();

		if (!tabs.contains(editpart)) {
			tabs.add(editpart);

			internalAdd(editpart);
		}
	}

	/**
	 * This method is called after the editpart was successfully added to the
	 * children collection.<br>
	 * Subclasses should handle the add of the UI element in that method.
	 * 
	 * @param editpart
	 *            The editpart to be added
	 */
	protected void internalAdd(ITabEditpart editpart) {

	}

	@Override
	public void remove(ITabEditpart editpart) {
		if (tabs == null) {
			return;
		}

		if (tabs.remove(editpart)) {
			internalRemove(editpart);
		}
	}

	/**
	 * This method is called after the editpart was successfully removed from
	 * the children collection.<br>
	 * Subclasses should handle the unrendering of the UI element in that
	 * method.
	 * 
	 * @param editpart
	 *            The editpart to be removed
	 */
	protected void internalRemove(ITabEditpart editpart) {

	}

	@Override
	public void insert(ITabEditpart editpart, int index) {
		ensureChildren();

		int currentIndex = tabs.indexOf(editpart);
		if (currentIndex > -1 && currentIndex != index) {
			throw new RuntimeException(
					String.format(
							"Insert at index %d not possible since editpart already contained at index %d",
							index, currentIndex));
		}

		tabs.add(index, editpart);
		internalInsert(editpart, index);
	}

	/**
	 * This method is called after the editpart was successfully inserted to the
	 * children collection.<br>
	 * Subclasses should handle the insert of the UI element in that method.
	 * 
	 * @param editpart
	 *            The editpart to be inserted
	 * @param index
	 *            The index where the editpart should be inserted
	 */
	protected void internalInsert(ITabEditpart editpart, int index) {

	}

	@Override
	public void move(ITabEditpart editpart, int index) {
		if (tabs == null) {
			throw new RuntimeException(
					"Move not possible. No children present.");
		}

		if (!tabs.contains(editpart)) {
			throw new RuntimeException(
					String.format(
							"Move to index %d not possible since editpart not added yet!",
							index));
		}

		int currentIndex = tabs.indexOf(editpart);
		tabs.remove(editpart);
		tabs.add(index, editpart);

		internalMove(editpart, currentIndex, index);
	}

	/**
	 * This method is called after the editpart was successfully moved inside
	 * the children collection.<br>
	 * Subclasses should handle the move of the UI element in that method.
	 * 
	 * @param editpart
	 *            The editpart to be moved.
	 * @param oldIndex
	 *            The old index where the control was located.
	 * @param newIndex
	 *            The new index where the control should be located after the
	 *            move operation.
	 */
	protected void internalMove(ITabEditpart editpart, int oldIndex,
			int newIndex) {

	}

	/**
	 * Ensures, that the children collection exists.
	 */
	protected void ensureChildren() {
		if (tabs == null) {
			tabs = new ArrayList<ITabEditpart>();
		}
	}

	@Override
	protected void internalDispose() {
		try {
			if (tabs != null) {
				tabs.clear();
				tabs = null;
			}
		} finally {

		}
	}
}
