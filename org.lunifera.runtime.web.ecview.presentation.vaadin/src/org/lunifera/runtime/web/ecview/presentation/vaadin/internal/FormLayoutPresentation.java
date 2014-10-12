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
import java.util.List;
import java.util.Locale;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.ILayoutEditpart;
import org.lunifera.ecview.core.extension.model.extension.YFormLayout;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class FormLayoutPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormLayoutPresentation.class);

	private FormLayout formLayout;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public FormLayoutPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess((YFormLayout) editpart.getModel());
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
	 * Is called to refresh the UI. The element will be removed from the grid
	 * layout and added to it again afterwards.
	 */
	protected void refreshUI() {
		formLayout.removeAllComponents();

		// iterate all elements and build the child element
		//
		List<Cell> cells = new ArrayList<Cell>();
		for (IEmbeddableEditpart child : getChildren()) {
			cells.add(addChild(child));
		}
	}

	/**
	 * Is called to create the child component and apply layouting defaults to
	 * it.
	 * 
	 * @param editpart
	 * @param yStyle
	 * @return
	 */
	protected Cell addChild(IEmbeddableEditpart editpart) {

		Component child = (Component) editpart.render(formLayout);

		child.setSizeUndefined();
		child.setWidth("100%");
		formLayout.addComponent(child);

		return new Cell(child);
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (formLayout == null) {

			formLayout = new FormLayout();
			setupComponent(formLayout, getCastedModel());

			associateWidget(formLayout, modelAccess.yLayout);

			if (modelAccess.isCssIdValid()) {
				formLayout.setId(modelAccess.getCssID());
			} else {
				formLayout.setId(getEditpart().getId());
			}

			if (modelAccess.isMargin()) {
				formLayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				formLayout.setMargin(true);
			}

			if (!modelAccess.isSpacing()) {
				formLayout.setSpacing(false);
			} else {
				formLayout.setData(IConstants.CSS_CLASS_SPACING);
				formLayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				formLayout.addStyleName(modelAccess.getCssClass());
			} else {
				formLayout.addStyleName(CSS_CLASS_CONTROL);
			}
			formLayout.addStyleName(IConstants.CSS_CLASS_FORMLAYOUT);

			// creates the binding for the field
			createBindings(modelAccess.yLayout, formLayout, null);

			// initialize all children
			initializeChildren();

			renderChildren(false);
		}

		return formLayout;
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
		return formLayout;
	}

	@Override
	public boolean isRendered() {
		return formLayout != null;
	}

	@Override
	public void doUnrender() {
		if (formLayout != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(formLayout);

			formLayout = null;
		}
	}

	@Override
	protected void internalDispose() {
		try {
			for (IEmbeddableEditpart child : new ArrayList<IEmbeddableEditpart>(
					getChildren())) {
				child.dispose();
			}
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	@Override
	protected void internalAdd(IEmbeddableEditpart editpart) {
		addChild(editpart);
	}

	@Override
	protected void internalRemove(IEmbeddableEditpart child) {
		if (formLayout != null && child.isRendered()) {
			// will happen during disposal since children already disposed.
			formLayout.removeComponent((Component) child.getWidget());
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
		private final YFormLayout yLayout;

		public ModelAccess(YFormLayout yLayout) {
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YFormLayout#isSpacing()
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YFormLayout#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}
	}

	public static class Cell {
		private final Component component;

		public Cell(Component component) {
			super();
			this.component = component;
		}

		/**
		 * @return the component
		 */
		protected Component getComponent() {
			return component;
		}
	}
}
