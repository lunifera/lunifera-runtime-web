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

import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.emf.ElementEditpart;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTab;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITabEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.ITabPresentation;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * This presenter is responsible to render a tab sheet on the given layout.
 */
public class TabPresentation extends AbstractTabPresenter<Component> implements
		ITabPresentation<Component> {

	private CssLayout tabContent;
	private Tab tab;
	private ModelAccess modelAccess;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public TabPresentation(IElementEditpart editpart) {
		super((ITabEditpart) editpart);
		this.modelAccess = new ModelAccess((YTab) editpart.getModel());
	}

	@Override
	public Component createWidget(Object parent) {
		TabSheet tabSheet = (TabSheet) parent;

		YTab yTab = (YTab) getModel();
		@SuppressWarnings("restriction")
		IEmbeddableEditpart childEditpart = ElementEditpart.getEditpart(yTab
				.getEmbeddable());

		CssLayout childLayout = new CssLayout();
		if (childEditpart == null) {
			tab = tabSheet.addTab(childLayout, "content missing");
			return childLayout;
		}
		IWidgetPresentation<Component> childPresentation = childEditpart
				.getPresentation();

		Component childContent = childPresentation.createWidget(childLayout);
		childLayout.addComponent(childContent);

		tab = tabSheet.addTab(childLayout);
		if (modelAccess.isLabelValid()) {
			tab.setCaption(modelAccess.getLabel());
		}

		return null;
	}

	@Override
	public ComponentContainer getWidget() {
		return tabContent;
	}

	@Override
	public boolean isRendered() {
		return tabContent != null;
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
	public void unrender() {
		if (tab != null) {

			// unbind all active bindings
			unbind();

			YTab yTab = (YTab) getModel();
			@SuppressWarnings("restriction")
			IEmbeddableEditpart editpart = ElementEditpart.getEditpart(yTab
					.getEmbeddable());

			IWidgetPresentation<Component> childPresentation = editpart
					.getPresentation();
			childPresentation.unrender();
		}
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YTab yTab;

		public ModelAccess(YTab yTab) {
			super();
			this.yTab = yTab;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yTab.getCssClass();
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
			return yTab.getCssID();
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
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelValid() {
			return yTab.getDatadescription() != null
					&& yTab.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yTab.getDatadescription().getLabel();
		}
	}
}
