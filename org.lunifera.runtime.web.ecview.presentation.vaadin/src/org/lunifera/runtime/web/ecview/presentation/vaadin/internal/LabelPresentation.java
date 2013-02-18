/**
 * Copyright (c) 2012 Florian Pirchner (Vienna, Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YLabel;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ILabelEditpart;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class LabelPresentation extends AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private Label label;

	/**
	 * Constructor.
	 * 
	 * @param editpart The editpart of that presenter
	 */
	public LabelPresentation(IElementEditpart editpart) {
		super((ILabelEditpart) editpart);
		this.modelAccess = new ModelAccess((YLabel) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component createWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS__CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			label = new Label();
			label.addStyleName(CSS_CLASS__CONTROL);
			label.setSizeFull();
			componentBase.addComponent(label);

			if (modelAccess.isCssClassValid()) {
				label.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isLabelValid()) {
				label.setCaption(modelAccess.getLabel());
			}
		}
		return componentBase;
	}

	@Override
	public Component getWidget() {
		return componentBase;
	}

	@Override
	public boolean isRendered() {
		return componentBase != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unrender() {
		if (componentBase != null) {
			ComponentContainer parent = ((ComponentContainer) componentBase.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
			label = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		// unrender the ui component
		unrender();
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YLabel yLabel;

		public ModelAccess(YLabel yLabel) {
			super();
			this.yLabel = yLabel;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yLabel.getCssClass();
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
			return yLabel.getCssID();
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
			return yLabel.getDatadescription() != null && yLabel.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yLabel.getDatadescription().getLabel();
		}
	}
}
