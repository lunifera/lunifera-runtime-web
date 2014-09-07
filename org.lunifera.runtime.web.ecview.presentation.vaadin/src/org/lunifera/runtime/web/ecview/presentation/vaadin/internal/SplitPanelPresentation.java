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
import java.util.Locale;

import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.ILayoutEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YSplitPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class SplitPanelPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SplitPanelPresentation.class);

	private CssLayout componentBase;
	private AbstractSplitPanel splitPanel;
	private ModelAccess modelAccess;

	private CssLayout fillerLayout;

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
		II18nService service = getI18nService();
		if (service != null && modelAccess.isLabelI18nKeyValid()) {
			componentBase.setCaption(service.getValue(
					modelAccess.getLabelI18nKey(), getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				componentBase.setCaption(modelAccess.getLabel());
			}
		}
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
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);

			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			associateWidget(componentBase, modelAccess.yLayout);

			splitPanel = modelAccess.yLayout.isVertical() ? new VerticalSplitPanel()
					: new HorizontalSplitPanel();
			splitPanel.setSizeFull();
			splitPanel.setSplitPosition(modelAccess.yLayout.getSplitPosition(),
					Unit.PERCENTAGE);
			componentBase.addComponent(splitPanel);

			associateWidget(splitPanel, modelAccess.yLayout);

			if (modelAccess.isCssClassValid()) {
				splitPanel.addStyleName(modelAccess.getCssClass());
			} else {
				splitPanel.addStyleName(CSS_CLASS_CONTROL);
			}

			// creates the binding for the field
			createBindings(modelAccess.yLayout, splitPanel, componentBase);

			// initialize all children
			initializeChildren();

			renderChildren(false);
		}

		return componentBase;
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
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	@Override
	public void doUnrender() {
		if (componentBase != null) {

			// unbind all active bindings
			unbind();

			// remove assocations
			unassociateWidget(componentBase);
			unassociateWidget(splitPanel);

			componentBase = null;
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

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelValid() {
			return yLayout.getDatadescription() != null
					&& yLayout.getDatadescription().getLabel() != null;
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
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yLayout.getDatadescription() != null
					&& yLayout.getDatadescription().getLabelI18nKey() != null;
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