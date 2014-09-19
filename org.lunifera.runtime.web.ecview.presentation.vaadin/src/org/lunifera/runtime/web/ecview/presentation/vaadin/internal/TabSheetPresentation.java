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

import java.util.Locale;

import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.extension.model.extension.YTabSheet;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITabEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITabSheetEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.presentation.ITabSheetPresentation;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

/**
 * This presenter is responsible to render a tab sheet on the given layout.
 */
public class TabSheetPresentation extends
		AbstractTabSheetPresenter<ComponentContainer> implements
		ITabSheetPresentation<ComponentContainer> {

	private CssLayout componentBase;
	private TabSheet tabSheet;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that editpart.
	 */
	public TabSheetPresentation(IElementEditpart editpart) {
		super((ITabSheetEditpart) editpart);
		this.modelAccess = new ModelAccess((YTabSheet) editpart.getModel());
	}

	@Override
	public void add(ITabEditpart editpart) {
		super.add(editpart);

		addTab(editpart);
	}

	@Override
	public void remove(ITabEditpart editpart) {
		super.remove(editpart);

		tabSheet.removeComponent((Component) editpart.getWidget());
	}

	@Override
	public void insert(ITabEditpart editpart, int index) {
		super.insert(editpart, index);

		refreshUI();
	}

	@Override
	public void move(ITabEditpart editpart, int index) {
		super.move(editpart, index);

		refreshUI();
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
		tabSheet.removeAllComponents();

		// iterate all elements and build the tab element
		//
		for (ITabEditpart editPart : getEditpart().getTabs()) {
			addTab(editPart);
		}
	}

	/**
	 * Is called to create the tab component and apply layouting defaults to it.
	 * 
	 * @param editpart
	 */
	protected void addTab(ITabEditpart editpart) {
		editpart.render(tabSheet);
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			setupComponent(componentBase, getCastedModel());
			
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}
			
			associateWidget(componentBase, modelAccess.yLayout);

			tabSheet = new TabSheet();
			tabSheet.setSizeFull();
			componentBase.addComponent(tabSheet);
			
			associateWidget(tabSheet, modelAccess.yLayout);

			if (modelAccess.isCssClassValid()) {
				tabSheet.addStyleName(modelAccess.getCssClass());
			} else {
				tabSheet.addStyleName(CSS_CLASS_CONTROL);
			}

			renderTabs(false);
		}

		return componentBase;
	}

	@Override
	public ComponentContainer getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
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
		if (componentBase != null) {

			// unbind all active bindings
			unbind();

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}

			// remove assocations
			unassociateWidget(componentBase);
			unassociateWidget(tabSheet);

			componentBase = null;
			tabSheet = null;

			// unrender the tabs
			for (ITabEditpart tab : getTabs()) {
				tab.unrender();
			}
		}
	}

	@Override
	public void renderTabs(boolean force) {
		if (force) {
			unrenderTabs();
		}

		refreshUI();
	}

	/**
	 * Will unrender all tabs.
	 */
	protected void unrenderTabs() {
		for (ITabEditpart editpart : getTabs()) {
			if (editpart.isRendered()) {
				editpart.unrender();
			}
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YTabSheet yLayout;

		public ModelAccess(YTabSheet yLayout) {
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
	}
}
