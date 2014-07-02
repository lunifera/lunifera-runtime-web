/**
 * Copyright (c) 2013 COMPEX Systemhaus GmbH Heidelberg. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jose C. Dominguez - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YButton;
import org.eclipse.emf.ecp.ecview.extension.model.extension.listener.YButtonClickListener;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.IButtonEditpart;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class ButtonPresentation extends
		AbstractVaadinWidgetPresenter<Component> {

	private final ModelAccess modelAccess;
	private CssLayout componentBase;
	private Button button;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public ButtonPresentation(IElementEditpart editpart) {
		super((IButtonEditpart) editpart);
		this.modelAccess = new ModelAccess((YButton) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new CssLayout();
			componentBase.addStyleName(CSS_CLASS_CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			button = new Button();
			button.addStyleName(CSS_CLASS_CONTROL);
			button.setImmediate(true);

			// creates the binding for the field
			createBindings(modelAccess.yButton, button);

			componentBase.addComponent(button);

			if (modelAccess.isCssClassValid()) {
				button.addStyleName(modelAccess.getCssClass());
			}

			applyCaptions();

		}
		return componentBase;
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
			button.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
					getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				button.setCaption(modelAccess.getLabel());
			}
		}
	}

	/**
	 * Creates the bindings for the given values.
	 * 
	 * @param yButton
	 * @param button
	 */
	@SuppressWarnings("serial")
	protected void createBindings(final YButton yButton, Button button) {
		super.createBindings(yButton, button);

		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				for (YButtonClickListener listener : new ArrayList<YButtonClickListener>(
						yButton.getClickListeners())) {
					listener.clicked(yButton);
				}
			}
		});

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
			button = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();
		} finally {
			super.internalDispose();
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YButton yButton;

		public ModelAccess(YButton yButton) {
			super();
			this.yButton = yButton;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yButton.getCssClass();
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
			return yButton.getCssID();
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
			return yButton.getDatadescription() != null
					&& yButton.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yButton.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yButton.getDatadescription() != null
					&& yButton.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yButton.getDatadescription().getLabelI18nKey();
		}
	}
}
