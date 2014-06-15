/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.presentation.ILayoutPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTabSheet;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabSheetEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabPresentation;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabSheetPresentation;

import com.vaadin.ui.ComponentContainer;

/**
 * An abstract base class implementing {@link ILayoutPresentation}.
 */
public abstract class AbstractTabSheetPresenter<A extends ComponentContainer>
		extends AbstractVaadinWidgetPresenter<A> implements
		ITabSheetPresentation<A> {

	private List<ITabPresentation<?>> tabs;

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
	public List<ITabPresentation<?>> getTabs() {
		return tabs != null ? Collections.unmodifiableList(tabs)
				: Collections.<ITabPresentation<?>> emptyList();
	}

	@Override
	public boolean contains(ITabPresentation<?> presentation) {
		return tabs != null && tabs.contains(presentation);
	}

	@Override
	public void add(ITabPresentation<?> presentation) {
		ensureChildren();

		if (!tabs.contains(presentation)) {
			tabs.add(presentation);

			internalAdd(presentation);
		}
	}

	/**
	 * This method is called after the presentation was successfully added to
	 * the children collection.<br>
	 * Subclasses should handle the add of the UI element in that method.
	 * 
	 * @param presentation
	 *            The presentation to be added
	 */
	protected void internalAdd(ITabPresentation<?> presentation) {

	}

	@Override
	public void remove(ITabPresentation<?> presentation) {
		if (tabs == null) {
			return;
		}

		if (tabs.remove(presentation)) {
			internalRemove(presentation);
		}
	}

	/**
	 * This method is called after the presentation was successfully removed
	 * from the children collection.<br>
	 * Subclasses should handle the unrendering of the UI element in that
	 * method.
	 * 
	 * @param presentation
	 *            The presentation to be removed
	 */
	protected void internalRemove(ITabPresentation<?> presentation) {

	}

	@Override
	public void insert(ITabPresentation<?> presentation, int index) {
		ensureChildren();

		int currentIndex = tabs.indexOf(presentation);
		if (currentIndex > -1 && currentIndex != index) {
			throw new RuntimeException(
					String.format(
							"Insert at index %d not possible since presentation already contained at index %d",
							index, currentIndex));
		}

		tabs.add(index, presentation);
		internalInsert(presentation, index);
	}

	/**
	 * This method is called after the presentation was successfully inserted to
	 * the children collection.<br>
	 * Subclasses should handle the insert of the UI element in that method.
	 * 
	 * @param presentation
	 *            The presentation to be inserted
	 * @param index
	 *            The index where the presentation should be inserted
	 */
	protected void internalInsert(ITabPresentation<?> presentation, int index) {

	}

	@Override
	public void move(ITabPresentation<?> presentation, int index) {
		if (tabs == null) {
			throw new RuntimeException(
					"Move not possible. No children present.");
		}

		if (!tabs.contains(presentation)) {
			throw new RuntimeException(
					String.format(
							"Move to index %d not possible since presentation not added yet!",
							index));
		}

		int currentIndex = tabs.indexOf(presentation);
		tabs.remove(presentation);
		tabs.add(index, presentation);

		internalMove(presentation, currentIndex, index);
	}

	/**
	 * This method is called after the presentation was successfully moved
	 * inside the children collection.<br>
	 * Subclasses should handle the move of the UI element in that method.
	 * 
	 * @param presentation
	 *            The presentation to be moved.
	 * @param oldIndex
	 *            The old index where the control was located.
	 * @param newIndex
	 *            The new index where the control should be located after the
	 *            move operation.
	 */
	protected void internalMove(ITabPresentation<?> presentation,
			int oldIndex, int newIndex) {

	}

	/**
	 * Ensures, that the children collection exists.
	 */
	protected void ensureChildren() {
		if (tabs == null) {
			tabs = new ArrayList<ITabPresentation<?>>();
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
