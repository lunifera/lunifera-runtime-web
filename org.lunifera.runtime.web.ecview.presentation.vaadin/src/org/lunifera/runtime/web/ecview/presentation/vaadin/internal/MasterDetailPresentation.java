/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Locale;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.emf.ecp.ecview.common.context.II18nService;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddableBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YMasterDetail;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextFieldEditpart;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.presentation.IMasterDetailPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class MasterDetailPresentation extends
		AbstractVaadinWidgetPresenter<ComponentContainer> implements
		IMasterDetailPresentation<ComponentContainer> {

	private final ModelAccess modelAccess;
	private VerticalLayout componentBase;

	private CssLayout masterBase;
	private CssLayout detailBase;
	private IWidgetPresentation<?> masterPresentation;
	private IWidgetPresentation<?> detailPresentation;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presenter
	 */
	public MasterDetailPresentation(IElementEditpart editpart) {
		super((ITextFieldEditpart) editpart);
		this.modelAccess = new ModelAccess((YMasterDetail) editpart.getModel());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComponentContainer doCreateWidget(Object parent) {
		if (componentBase == null) {
			componentBase = new VerticalLayout();
			componentBase.addStyleName(CSS_CLASS__CONTROL_BASE);
			if (modelAccess.isCssIdValid()) {
				componentBase.setId(modelAccess.getCssID());
			} else {
				componentBase.setId(getEditpart().getId());
			}

			// create the container for master an detail
			masterBase = new CssLayout();
			masterBase.setStyleName(IConstants.CSS_CLASS__MASTER_BASE);
			masterBase.setSizeFull();
			componentBase.addComponent(masterBase);

			detailBase = new CssLayout();
			masterBase.setStyleName(IConstants.CSS_CLASS__DETAIL_BASE);
			detailBase.setSizeFull();
			componentBase.addComponent(detailBase);

			// render master an detail
			if (masterPresentation != null) {
				masterPresentation.createWidget(masterBase);
			}

			if (detailPresentation != null) {
				detailPresentation.createWidget(detailBase);
			}

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
			// text.setCaption(service.getValue(modelAccess.getLabelI18nKey(),
			// getLocale()));
		} else {
			if (modelAccess.isLabelValid()) {
				// text.setCaption(modelAccess.getLabel());
			}
		}
	}

	@Override
	protected IObservable internalGetObservableEndpoint(
			YEmbeddableBindingEndpoint bindableValue) {
		if (bindableValue == null) {
			throw new NullPointerException("BindableValue must not be null!");
		}

		// if (bindableValue instanceof YEmbeddableValueEndpoint) {
		// return internalGetValueEndpoint();
		// }
		throw new IllegalArgumentException("Not a valid input: "
				+ bindableValue);
	}

	// /**
	// * Returns the observable to observe value.
	// *
	// * @return
	// */
	// protected IObservableValue internalGetValueEndpoint() {
	// // return the observable value for text
	// return EMFObservables.observeValue(castEObject(getModel()),
	// ExtensionModelPackage.Literals.yMasterDetail_FIELD__VALUE);
	// }

	// /**
	// * Creates the bindings for the given values.
	// *
	// * @param yField
	// * @param field
	// */
	// protected void createBindings(YMasterDetailField yField, TextField field)
	// {
	// // create the model binding from ridget to ECView-model
	// // registerBinding(createBindings_Value(castEObject(getModel()),
	// // ExtensionModelPackage.Literals.yMasterDetail_FIELD__VALUE, text));
	//
	// super.createBindings(yField, field);
	// }

	@Override
	public ComponentContainer getWidget() {
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

			if (masterPresentation != null) {
				masterPresentation.unrender();
				masterBase.removeAllComponents();
			}

			if (detailPresentation != null) {
				detailPresentation.unrender();
				detailBase.removeAllComponents();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDispose() {
		try {
			unrender();

			// do NOT dispose. Will be handled by editpart.
			masterPresentation = null;
			detailPresentation = null;

		} finally {
			super.internalDispose();
		}
	}

	@Override
	public IWidgetPresentation<?> getMaster() {
		return masterPresentation;
	}

	@Override
	public IWidgetPresentation<?> getDetail() {
		return detailPresentation;
	}

	@Override
	public void setMaster(IWidgetPresentation<?> master) {
		if (this.masterPresentation == master) {
			return;
		}

		if (this.masterPresentation != null) {
			this.masterPresentation.dispose();
			masterBase.removeAllComponents();
		}

		this.masterPresentation = master;

		if (this.masterPresentation != null) {
			this.masterPresentation.createWidget(masterBase);
		}
	}

	@Override
	public void setDetail(IWidgetPresentation<?> detail) {
		if (this.detailPresentation == detail) {
			return;
		}

		if (this.detailPresentation != null) {
			this.detailPresentation.dispose();
			detailBase.removeAllComponents();
		}

		this.detailPresentation = detail;

		if (this.detailPresentation != null) {
			this.detailPresentation.createWidget(detailBase);
		}
	}

	/**
	 * A helper class.
	 */
	private static class ModelAccess {
		private final YMasterDetail yMasterDetail;

		public ModelAccess(YMasterDetail yMasterDetail) {
			super();
			this.yMasterDetail = yMasterDetail;
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.core.YCssAble#getCssClass()
		 */
		public String getCssClass() {
			return yMasterDetail.getCssClass();
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
			return yMasterDetail.getCssID();
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
			return yMasterDetail.getDatadescription() != null
					&& yMasterDetail.getDatadescription().getLabel() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabel() {
			return yMasterDetail.getDatadescription().getLabel();
		}

		/**
		 * Returns true, if the label is valid.
		 * 
		 * @return
		 */
		public boolean isLabelI18nKeyValid() {
			return yMasterDetail.getDatadescription() != null
					&& yMasterDetail.getDatadescription().getLabelI18nKey() != null;
		}

		/**
		 * Returns the label.
		 * 
		 * @return
		 */
		public String getLabelI18nKey() {
			return yMasterDetail.getDatadescription().getLabelI18nKey();
		}
	}
}
