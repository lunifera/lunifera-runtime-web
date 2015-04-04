/**
 * Copyright (c) 2011 - 2015, Lunifera GmbH (Gross Enzersdorf), Loetz KG (Heidelberg)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *         Florian Pirchner - Initial implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.ILayoutEditpart;
import org.lunifera.ecview.core.common.filter.IFilterProvidingPresentation;
import org.lunifera.ecview.core.extension.model.extension.YSearchPanel;
import org.lunifera.ecview.core.ui.core.editparts.extension.ISearchFieldEditpart;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.vaadin.common.data.filter.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class SearchPanelPresentation extends
		AbstractLayoutPresenter<ComponentContainer> implements
		IFilterProvidingPresentation {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SearchPanelPresentation.class);

	private HorizontalLayout horizontalLayout;
	private FormLayout leftForm;
	private FormLayout rightForm;
	private ModelAccess modelAccess;

	private int currentChildIndex;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that editpart.
	 */
	public SearchPanelPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess((YSearchPanel) editpart.getModel());
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// no need to set the locale to the ui elements. Is handled by vaadin
		// internally.

		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
	}

	/**
	 * Is called to refresh the UI. The element will be removed from the layout
	 * and added to it again afterwards.
	 */
	protected void refreshUI() {
		leftForm.removeAllComponents();
		rightForm.removeAllComponents();

		// iterate all elements and build the child element
		//
		currentChildIndex = 0;
		for (IEmbeddableEditpart child : getChildren()) {
			addChild(child);
		}
	}

	private void addChild(IEmbeddableEditpart child) {
		currentChildIndex++;
		if (currentChildIndex % 2 == 1) {
			leftForm.addComponent((Component) child.render(leftForm));
		} else {
			rightForm.addComponent((Component) child.render(rightForm));
		}
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (horizontalLayout == null) {

			horizontalLayout = new HorizontalLayout();
			setupComponent(horizontalLayout, getCastedModel());

			associateWidget(horizontalLayout, modelAccess.yLayout);

			if (modelAccess.isCssIdValid()) {
				horizontalLayout.setId(modelAccess.getCssID());
			} else {
				horizontalLayout.setId(getEditpart().getId());
			}

			if (modelAccess.isMargin()) {
				horizontalLayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				horizontalLayout.setMargin(true);
			}

			if (!modelAccess.isSpacing()) {
				horizontalLayout.setSpacing(false);
			} else {
				horizontalLayout.addStyleName(IConstants.CSS_CLASS_SPACING);
				horizontalLayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				horizontalLayout.addStyleName(modelAccess.getCssClass());
			} else {
				horizontalLayout.addStyleName(CSS_CLASS_CONTROL);
			}
			horizontalLayout
					.addStyleName(IConstants.CSS_CLASS_HORIZONTALLAYOUT);

			leftForm = new FormLayout();
			leftForm.setSizeFull();
			horizontalLayout.addComponent(leftForm);

			rightForm = new FormLayout();
			rightForm.setSizeFull();
			horizontalLayout.addComponent(rightForm);

			// creates the binding for the field
			createBindings(modelAccess.yLayout, horizontalLayout, null);

			// initialize all children
			initializeChildren();

			// and now render children
			renderChildren(false);
		}

		return horizontalLayout;
	}

	@Override
	public Object getFilter() {

		Set<Filter> filters = new HashSet<Filter>();
		for (IEmbeddableEditpart editpart : getChildren()) {
			ISearchFieldEditpart temp = (ISearchFieldEditpart) editpart;
			Filter filter = (Filter) temp.getFilter();
			if (filter != null) {
				filters.add(filter);
			}
		}

		if (filters.size() > 0) {
			return new Filters()
					.and(filters.toArray(new Filter[filters.size()]));
		} else {
			return null;
		}
	}

	/**
	 * Adds the children to the superclass and prevents rendering.
	 */
	private void initializeChildren() {
		setRenderLock(true);
		try {
			for (IEmbeddableEditpart editPart : getEditpart().getElements()) {
				super.add(editPart);
			}
		} finally {
			setRenderLock(false);
		}
	}

	@Override
	public ComponentContainer getWidget() {
		return horizontalLayout;
	}

	@Override
	public boolean isRendered() {
		return horizontalLayout != null;
	}

	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	@Override
	public void doUnrender() {
		if (horizontalLayout != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(horizontalLayout);

			horizontalLayout.removeAllComponents();
			horizontalLayout = null;
			leftForm.removeAllComponents();
			leftForm = null;
			rightForm.removeAllComponents();
			rightForm = null;
		}
	}

	@Override
	protected void internalAdd(IEmbeddableEditpart editpart) {
		addChild(editpart);
	}

	@Override
	protected void internalRemove(IEmbeddableEditpart child) {
		if (horizontalLayout != null && child.isRendered()) {
			// will happen during disposal since children already disposed.
			horizontalLayout.removeComponent((Component) child.getWidget());
		}

		child.unrender();
	}

	@Override
	protected void internalInsert(IEmbeddableEditpart editpart, int index) {
		refreshUI();
	}

	@Override
	protected void internalMove(IEmbeddableEditpart editpart, int oldIndex,
			int newIndex) {
		refreshUI();
	}

	@Override
	public void renderChildren(boolean force) {
		if (force) {
			unrenderChildren();
		}

		refreshUI();
	}

	/**
	 * Will unrender all children.
	 */
	protected void unrenderChildren() {
		for (IEmbeddableEditpart editpart : getChildren()) {
			if (editpart.isRendered()) {
				editpart.unrender();
			}
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YSearchPanel yLayout;

		public ModelAccess(YSearchPanel yLayout) {
			super();
			this.yLayout = yLayout;
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yLayout.getCssClass();
		}

		/**
		 * Returns true, if the css class is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssClassValid() {
			return getCssClass() != null && !getCssClass().equals("");
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YSearchPanel#isSpacing()
		 */
		public boolean isSpacing() {
			return yLayout.isSpacing();
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
		 */
		public String getCssID() {
			return yLayout.getCssID();
		}

		/**
		 * Returns true, if the css id is not null and not empty.
		 * 
		 * @return
		 */
		public boolean isCssIdValid() {
			return getCssID() != null && !getCssID().equals("");
		}

		/**
		 * @return
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YSearchPanel#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}

	}
}
