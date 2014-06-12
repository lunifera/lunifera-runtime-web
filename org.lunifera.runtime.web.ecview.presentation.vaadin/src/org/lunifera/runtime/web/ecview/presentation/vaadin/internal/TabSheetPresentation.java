/*******************************************************************************
 * Copyright (c) 2011 Florian Pirchner
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 *******************************************************************************/
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Locale;

import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTabSheet;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabSheetEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabPresentation;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabSheetPresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory
			.getLogger(TabSheetPresentation.class);

	private CssLayout componentBase;
	private TabSheet tabSheet;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public TabSheetPresentation(IElementEditpart editpart) {
		super((ITabSheetEditpart) editpart);
		this.modelAccess = new ModelAccess((YTabSheet) editpart.getModel());
	}

	@Override
	public void add(ITabPresentation<?> presentation) {
		super.add(presentation);

		addTab(presentation);
	}

	@Override
	public void remove(ITabPresentation<?> presentation) {
		super.remove(presentation);

		tabSheet.removeComponent((Component) presentation.getWidget());
	}

	@Override
	public void insert(ITabPresentation<?> presentation, int index) {
		super.insert(presentation, index);

		refreshUI();
	}

	@Override
	public void move(ITabPresentation<?> presentation, int index) {
		super.move(presentation, index);

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
			ITabPresentation<?> tabPresentation = editPart.getPresentation();
			addTab(tabPresentation);
		}

	}

	/**
	 * Is called to create the tab component and apply layouting defaults to it.
	 * 
	 * @param presentation
	 */
	protected void addTab(ITabPresentation<?> presentation) {
		presentation.createWidget(tabSheet);
	}

	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.setSizeFull();
			componentBase.addStyleName(CSS_CLASS__CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			tabSheet = new TabSheet();
			componentBase.addComponent(tabSheet);

			if (modelAccess.isCssClassValid()) {
				tabSheet.addStyleName(modelAccess.getCssClass());
			} else {
				tabSheet.addStyleName(CSS_CLASS__CONTROL);
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
			componentBase = null;
			tabSheet = null;

			// unrender the tabs
			for (ITabPresentation<?> tab : getTabs()) {
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
		for (ITabPresentation<?> presentation : getTabs()) {
			if (presentation.isRendered()) {
				presentation.unrender();
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
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
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
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssID()
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
