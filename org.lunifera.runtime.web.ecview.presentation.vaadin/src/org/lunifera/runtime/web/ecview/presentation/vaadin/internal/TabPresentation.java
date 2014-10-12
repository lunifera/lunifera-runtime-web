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

import org.lunifera.ecview.core.common.context.II18nService;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.emf.ElementEditpart;
import org.lunifera.ecview.core.common.presentation.IWidgetPresentation;
import org.lunifera.ecview.core.extension.model.extension.YTab;
import org.lunifera.ecview.core.ui.core.editparts.extension.ITabEditpart;
import org.lunifera.ecview.core.ui.core.editparts.extension.presentation.ITabPresentation;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * This presenter is responsible to render a tab sheet on the given layout.
 */
@SuppressWarnings("restriction")
public class TabPresentation extends AbstractTabPresenter<Component> implements
		ITabPresentation<Component> {

	private Tab tab;
	private CssLayout tabContent;
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
		if (tab == null) {
			TabSheet tabSheet = (TabSheet) parent;

			YTab yTab = (YTab) getModel();
			IEmbeddableEditpart childEditpart = ElementEditpart
					.getEditpart(yTab.getEmbeddable());

			CssLayout childLayout = new CssLayout();
			if (childEditpart == null) {
				tab = tabSheet.addTab(childLayout, "content missing");
				return childLayout;
			}
			IWidgetPresentation<Component> childPresentation = childEditpart
					.getPresentation();

			Component childContent = childPresentation
					.createWidget(childLayout);
			childLayout.addComponent(childContent);

			tab = tabSheet.addTab(childLayout);

			if (modelAccess.isCssIdValid()) {
				tab.setId(modelAccess.getCssID());
			} else {
				tab.setId(getEditpart().getId());
			}

			applyCaptions();
		}
		return tab.getComponent();
	}

	@Override
	protected void doUpdateLocale(Locale locale) {
		// update the captions
		applyCaptions();
	}

	/**
	 * Applies the labels to the widgets.
	 */
	protected void applyCaptions() {
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			tab.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				tab.setCaption(modelAccess.getLabel());
			}
		}
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
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
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

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yTab.getDatadescription() != null
					&& yTab.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yTab.getDatadescription().getLabelI18nKey();
		}
	}
}
