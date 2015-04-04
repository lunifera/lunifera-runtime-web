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

import java.util.ArrayList;
import java.util.Locale;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.ILayoutEditpart;
import org.lunifera.ecview.core.extension.model.extension.YSplitPanel;
import org.lunifera.runtime.web.ecview.presentation.vaadin.common.AbstractLayoutPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class SplitPanelPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SplitPanelPresentation.class);

	private AbstractSplitPanel splitPanel;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public SplitPanelPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess((YSplitPanel) editpart.getModel());
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
		Util.applyCaptions(getI18nService(), modelAccess.getLabel(),
				modelAccess.getLabelI18nKey(), getLocale(), splitPanel);
	}

	/**
	 * Is called to refresh the UI. The element will be removed from the grid
	 * layout and added to it again afterwards.
	 */
	protected void refreshUI() {
		splitPanel.removeAllComponents();

		// iterate all elements and build the child element
		//
		for (IEmbeddableEditpart child : getChildren()) {
			addChild(child);
		}
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (splitPanel == null) {

			splitPanel = modelAccess.yLayout.isVertical() ? new VerticalSplitPanel()
					: new HorizontalSplitPanel();
			setupComponent(splitPanel, getCastedModel());
			splitPanel.setSplitPosition(modelAccess.yLayout.getSplitPosition(),
					Unit.PERCENTAGE);

			associateWidget(splitPanel, modelAccess.yLayout);
			if (modelAccess.isCssIdValid()) {
				splitPanel.setId(modelAccess.getCssID());
			} else {
				splitPanel.setId(getEditpart().getId());
			}

			if (modelAccess.isCssClassValid()) {
				splitPanel.addStyleName(modelAccess.getCssClass());
			} else {
				splitPanel.addStyleName(CSS_CLASS_CONTROL);
			}

			// creates the binding for the field
			createBindings(modelAccess.yLayout, splitPanel, null);

			// initialize all children
			initializeChildren();

			renderChildren(false);
		}

		return splitPanel;
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
		return splitPanel;
	}

	@Override
	public boolean isRendered() {
		return splitPanel != null;
	}

	@Override
	public void doUnrender() {
		if (splitPanel != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(splitPanel);

			splitPanel = null;
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

	private void addChild(IEmbeddableEditpart editpart) {
		int index = getChildren().indexOf(editpart);
		if (index > 1) {
			LOGGER.warn("More than two elements added to splitpanel.");
			return;
		}

		if (index == 0) {
			splitPanel.setFirstComponent((Component) editpart
					.render(splitPanel));
		} else {
			splitPanel.setSecondComponent((Component) editpart
					.render(splitPanel));
		}
	}

	@Override
	protected void internalRemove(IEmbeddableEditpart child) {
		if (splitPanel != null && child.isRendered()) {
			// will happen during disposal since children already disposed.
			splitPanel.removeComponent((Component) child.getWidget());
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
		private final YSplitPanel yLayout;

		public ModelAccess(YSplitPanel yLayout) {
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
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yLayout.getDatadescription().getLabel();
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yLayout.getDatadescription().getLabelI18nKey();
		}

	}
}
