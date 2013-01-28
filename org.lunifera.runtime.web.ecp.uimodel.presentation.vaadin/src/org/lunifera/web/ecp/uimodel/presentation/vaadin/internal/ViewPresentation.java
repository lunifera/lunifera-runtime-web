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
package org.lunifera.web.ecp.uimodel.presentation.vaadin.internal;

import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.disposal.AbstractDisposable;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IViewPresentation;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.lunifera.web.ecp.uimodel.presentation.vaadin.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class ViewPresentation extends AbstractDisposable implements
		IViewPresentation<Component> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ViewPresentation.class);

	private ModelAccess modelAccess;
	private final IViewEditpart editpart;
	private CssLayout componentBase;
	private CssLayout component;
	private IWidgetPresentation<?> contentPresentation;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart for that presentation.
	 */
	public ViewPresentation(IViewEditpart editpart) {
		this.editpart = editpart;
		this.modelAccess = new ModelAccess((YView) editpart.getModel());
	}

	@Override
	public Object getModel() {
		return editpart.getModel();
	}

	/**
	 * Returns the editpart the presenter will render for.
	 * 
	 * @return editpart
	 */
	public IViewEditpart getEditpart() {
		checkDisposed();
		return editpart;
	}

	@Override
	public void render(Map<String, Object> options) {
		checkDisposed();

		if (editpart.getContent() != null) {
			contentPresentation = editpart.getContent().getPresentation();
		}
		ComponentContainer parent = (ComponentContainer) editpart.getContext()
				.getRootLayout();
		createWidget(parent);

	}

	/**
	 * Is called to render the content.
	 */
	protected void renderContent() {
		if (!isRendered()) {
			return;
		}

		if (contentPresentation != null) {
			Component contentComponent = (Component) contentPresentation
					.createWidget(component);
			component.addComponent(contentComponent);
		} else {
			LOGGER.warn("Content is null");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component createWidget(Object parent) {
		if (componentBase == null) {
			// create component base with grid layout to enable margins
			//
			componentBase = new CssLayout();
			componentBase.setSizeFull();
			componentBase.addStyleName(IConstants.CSS_CLASS__CONTROL_BASE);

			ComponentContainer parentContainer = (ComponentContainer) parent;
			parentContainer.addComponent(componentBase);

			// create the component
			component = new CssLayout();
			component.setSizeFull();
			component.addStyleName(IConstants.CSS_CLASS__CONTROL);
			componentBase.addComponent(component);

			if (modelAccess.isCssIdValid()) {
				component.setId(modelAccess.getCssID());
			} else {
				component.setId(editpart.getId());
			}

			if (modelAccess.isCssClassValid()) {
				component.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isMargin()) {
				component.addStyleName(IConstants.CSS_CLASS__MARGIN);
			}

			// render the content
			//
			renderContent();
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
			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;

			IWidgetPresentation<?> childPresentation = getContent();
			if (childPresentation != null) {
				childPresentation.unrender();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		if (componentBase != null) {
			unrender();
		}
	}

	@Override
	public void setContent(IWidgetPresentation<?> presentation) {
		IWidgetPresentation<?> oldPresentation = this.contentPresentation;

		this.contentPresentation = presentation;

		if (oldPresentation != null) {
			oldPresentation.unrender();
		}

		renderContent();
	}

	@Override
	public IWidgetPresentation<?> getContent() {
		return contentPresentation;
	}

	@Override
	public IViewContext getViewContext() {
		return getEditpart().getContext();
	}

	/**
	 * An internal helper class.
	 */
	private static class ModelAccess {
		private final YView yView;

		public ModelAccess(YView yView) {
			super();
			this.yView = yView;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yView.getCssClass();
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
			return yView.getCssID();
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
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YGridLayout#isMargin()
		 */
		public boolean isMargin() {
			return yView.isMargin();
		}
	}
}
