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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.IObservable;
import org.lunifera.ecview.core.common.context.ILocaleChangedService;
import org.lunifera.ecview.core.common.context.IViewContext;
import org.lunifera.ecview.core.common.editpart.IDialogEditpart;
import org.lunifera.ecview.core.common.editpart.IElementEditpart;
import org.lunifera.ecview.core.common.editpart.IEmbeddableEditpart;
import org.lunifera.ecview.core.common.editpart.IViewEditpart;
import org.lunifera.ecview.core.common.editpart.binding.IBindableEndpointEditpart;
import org.lunifera.ecview.core.common.editpart.datatypes.IDatatypeEditpart.DatatypeChangeEvent;
import org.lunifera.ecview.core.common.model.core.YAlignment;
import org.lunifera.ecview.core.common.model.core.YView;
import org.lunifera.ecview.core.common.presentation.IViewPresentation;
import org.lunifera.ecview.core.common.services.IUiKitBasedService;
import org.lunifera.ecview.core.common.services.IWidgetAssocationsService;
import org.lunifera.ecview.core.common.tooling.IWidgetMouseClickService;
import org.lunifera.ecview.core.common.visibility.IVisibilityHandler;
import org.lunifera.ecview.core.util.emf.ModelUtil;
import org.lunifera.runtime.common.dispose.AbstractDisposable;
import org.lunifera.runtime.common.state.ISharedStateContext;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal.WidgetAssocationsService;
import org.lunifera.runtime.web.ecview.presentation.vaadin.services.internal.WidgetMouseClickService;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class ViewPresentation extends AbstractDisposable implements
		IViewPresentation<Component>, ILocaleChangedService.LocaleListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ViewPresentation.class);

	private ModelAccess modelAccess;
	private final IViewEditpart editpart;
	private CssLayout componentBase;
	private GridLayout component;
	private IEmbeddableEditpart content;

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
		this.content = editpart.getContent();
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

		component.removeAllComponents();

		if (content != null) {
			Component contentComponent = (Component) content.render(component);
			component.addComponent(contentComponent);

			applyAlignment(contentComponent,
					modelAccess.yView.getContentAlignment());
		} else {
			LOGGER.warn("Content is null");
		}

		componentBase.setSizeFull();
		component.setSizeFull();

		// if (!isFillVertical(modelAccess.yView.getContentAlignment())) {
		// int packingHelperRowIndex = component.getRows();
		// component.setRows(packingHelperRowIndex + 1);
		// component.setRowExpandRatio(packingHelperRowIndex, 1.0f);
		// } else {
		// componentBase.setHeight("100%");
		// component.setHeight("100%");
		// }
		//
		// if (!isFillHorizontal(modelAccess.yView.getContentAlignment())) {
		// int packingHelperColumnIndex = component.getColumns();
		// component.setColumns(packingHelperColumnIndex + 1);
		// component.setColumnExpandRatio(packingHelperColumnIndex, 1.0f);
		// } else {
		// componentBase.setWidth("100%");
		// component.setWidth("100%");
		// }
	}

	// private boolean isFillVertical(YAlignment alignment) {
	// switch (alignment) {
	// case FILL_CENTER:
	// case FILL_FILL:
	// case FILL_LEFT:
	// case FILL_RIGHT:
	// return true;
	// default:
	// return false;
	// }
	// }
	//
	// private boolean isFillHorizontal(YAlignment contentAlignment) {
	// switch (contentAlignment) {
	// case MIDDLE_FILL:
	// case FILL_FILL:
	// case BOTTOM_FILL:
	// case TOP_FILL:
	// return true;
	// default:
	// return false;
	// }
	// }

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
			componentBase.addStyleName(IConstants.CSS_CLASS_CONTROL_BASE);

			// register shared state
			ISharedStateContext sharedState = getViewContext().getService(
					ISharedStateContext.class.getName());
			if (sharedState != null) {
				Map<Object, Object> viewData = new HashMap<Object, Object>();
				viewData.put(ISharedStateContext.class, sharedState);
				componentBase.setData(viewData);
			}

			ComponentContainer parentContainer = (ComponentContainer) parent;
			parentContainer.addComponent(componentBase);

			// create the component
			component = new GridLayout(1, 1);
			component.addStyleName(IConstants.CSS_CLASS_CONTROL);
			componentBase.addComponent(component);
			component.setSizeFull();

			if (modelAccess.isCssIdValid()) {
				component.setId(modelAccess.getCssID());
			} else {
				component.setId(editpart.getId());
			}

			if (modelAccess.isCssClassValid()) {
				component.addStyleName(modelAccess.getCssClass());
			}

			if (modelAccess.isMargin()) {
				component.addStyleName(IConstants.CSS_CLASS_MARGIN);
			}

			// render the content
			//
			renderContent();

			// register as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yView);
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.addLocaleListener(this);
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

	@Override
	public Set<Binding> getUIBindings() {
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unrender() {
		if (componentBase != null) {
			// unregister as an locale change listener
			IViewContext context = ModelUtil.getViewContext(modelAccess.yView);
			ILocaleChangedService service = context
					.getService(ILocaleChangedService.ID);
			if (service != null) {
				service.removeLocaleListener(this);
			}

			ComponentContainer parent = ((ComponentContainer) componentBase
					.getParent());
			if (parent != null) {
				parent.removeComponent(componentBase);
			}
			componentBase = null;
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
	public void setContent(IEmbeddableEditpart editpart) {
		this.content = editpart;
		renderContent();
	}

	@Override
	public void requestFocus(IElementEditpart toFocus) {
		if (toFocus instanceof IEmbeddableEditpart) {
			Component component = (Component) ((IEmbeddableEditpart) toFocus)
					.getWidget();
			if (component instanceof Focusable) {
				((Focusable) component).focus();
			}
		}
	}

	@Override
	public IEmbeddableEditpart getContent() {
		return content;
	}

	@Override
	public IViewContext getViewContext() {
		return getEditpart().getContext();
	}

	@Override
	public IObservable getObservableValue(Object model) {
		throw new UnsupportedOperationException("Must be overridden!");
	}

	@Override
	public void exec(Runnable runnable) {
		VaadinObservables.activateRealm(componentBase.getUI());
		componentBase.getUI().accessSynchronously(runnable);
	}

	@Override
	public Future<?> execAsync(Runnable runnable) {
		return componentBase.getUI().access(runnable);
	}

	@Override
	public void apply(IVisibilityHandler handler) {

	}

	@Override
	public void resetVisibilityProperties() {

	}

	@Override
	public void notifyDatatypeChanged(DatatypeChangeEvent event) {

	}

	@Override
	public void localeChanged(Locale locale) {
		// pass the locale to the root element
		component.setLocale(locale);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <A extends IUiKitBasedService> A createService(Class<A> serviceClass) {
		if (serviceClass == IWidgetMouseClickService.class) {
			final WidgetMouseClickService service = new WidgetMouseClickService(
					getViewContext());
			service.activate();
			return (A) service;
		} else if (serviceClass == IWidgetAssocationsService.class) {
			WidgetAssocationsService service = new WidgetAssocationsService();
			return (A) service;
		}

		throw new IllegalArgumentException(String.format(
				"%s is not a supported service.", serviceClass.getName()));
	}

	public void click(ClickEvent event) {

	}

	@Override
	public void openDialog(IDialogEditpart dialogEditpart,
			IBindableEndpointEditpart inputData) {
		if (!isRendered()) {
			return;
		}

		// VaadinObservables.activateRealm(navigationView.getUI());
		// set the input data to the child nav page
		dialogEditpart.setInputDataBindingEndpoint(inputData);
		Window dialog = (Window) dialogEditpart.render(null);
		componentBase.getUI().addWindow(dialog);
	}

	@Override
	public void closeDialog(IDialogEditpart dialogEditpart) {
		if (!isRendered()) {
			return;
		}

		Window dialog = (Window) dialogEditpart.getWidget();
		if (dialog != null) {
			componentBase.getUI().removeWindow(dialog);
			dialogEditpart.unrender();
		}
	}

	/**
	 * Sets the alignment to the component.
	 * 
	 * @param child
	 * @param yAlignment
	 */
	protected void applyAlignment(Component child, YAlignment yAlignment) {

		if (yAlignment != null) {
			child.setSizeUndefined();
			switch (yAlignment) {
			case BOTTOM_CENTER:
				component.setComponentAlignment(child, Alignment.BOTTOM_CENTER);
				break;
			case BOTTOM_FILL:
				component.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				child.setWidth("100%");
				break;
			case BOTTOM_LEFT:
				component.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				break;
			case BOTTOM_RIGHT:
				component.setComponentAlignment(child, Alignment.BOTTOM_RIGHT);
				break;
			case MIDDLE_CENTER:
				component.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
				break;
			case MIDDLE_FILL:
				component.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				child.setWidth("100%");
				break;
			case MIDDLE_LEFT:
				component.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				break;
			case MIDDLE_RIGHT:
				component.setComponentAlignment(child, Alignment.MIDDLE_RIGHT);
				break;
			case TOP_CENTER:
				component.setComponentAlignment(child, Alignment.TOP_CENTER);
				break;
			case TOP_FILL:
				component.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				break;
			case TOP_LEFT:
				component.setComponentAlignment(child, Alignment.TOP_LEFT);
				break;
			case TOP_RIGHT:
				component.setComponentAlignment(child, Alignment.TOP_RIGHT);
				break;
			case FILL_CENTER:
				component.setComponentAlignment(child, Alignment.TOP_CENTER);
				child.setHeight("100%");
				break;
			case FILL_FILL:
				component.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setSizeFull();
				break;
			case FILL_LEFT:
				component.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setHeight("100%");
				break;
			case FILL_RIGHT:
				component.setComponentAlignment(child, Alignment.TOP_RIGHT);
				child.setHeight("100%");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Maps the vertical part of the alignment to FILL.
	 * 
	 * @param yAlignment
	 *            the alignment
	 * @return alignment the mapped alignment
	 */
	// BEGIN SUPRESS CATCH EXCEPTION
	protected YAlignment mapToVerticalFill(YAlignment yAlignment) {
		// END SUPRESS CATCH EXCEPTION
		if (yAlignment != null) {
			switch (yAlignment) {
			case BOTTOM_CENTER:
			case MIDDLE_CENTER:
			case TOP_CENTER:
				return YAlignment.FILL_CENTER;
			case BOTTOM_FILL:
			case MIDDLE_FILL:
			case TOP_FILL:
				return YAlignment.FILL_FILL;
			case BOTTOM_LEFT:
			case MIDDLE_LEFT:
			case TOP_LEFT:
				return YAlignment.FILL_LEFT;
			case BOTTOM_RIGHT:
			case MIDDLE_RIGHT:
			case TOP_RIGHT:
				return YAlignment.FILL_RIGHT;
			case FILL_FILL:
			case FILL_LEFT:
			case FILL_RIGHT:
			case FILL_CENTER:
				return YAlignment.FILL_FILL;
			default:
				break;
			}
		}
		return YAlignment.FILL_FILL;
	}

	/**
	 * Maps the horizontal part of the alignment to FILL.
	 * 
	 * @param yAlignment
	 *            the alignment
	 * @return alignment the mapped alignment
	 */
	// BEGIN SUPRESS CATCH EXCEPTION
	protected YAlignment mapToHorizontalFill(YAlignment yAlignment) {
		// END SUPRESS CATCH EXCEPTION
		if (yAlignment != null) {
			switch (yAlignment) {
			case BOTTOM_CENTER:
			case BOTTOM_FILL:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
				return YAlignment.BOTTOM_FILL;
			case MIDDLE_CENTER:
			case MIDDLE_FILL:
			case MIDDLE_LEFT:
			case MIDDLE_RIGHT:
				return YAlignment.MIDDLE_FILL;
			case TOP_CENTER:
			case TOP_FILL:
			case TOP_LEFT:
			case TOP_RIGHT:
				return YAlignment.TOP_FILL;
			case FILL_FILL:
			case FILL_LEFT:
			case FILL_RIGHT:
			case FILL_CENTER:
				return YAlignment.FILL_FILL;
			default:
				break;
			}
		}
		return YAlignment.FILL_FILL;
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
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssClass()
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
		 * @see org.lunifera.ecview.core.ui.core.model.core.YCssAble#getCssID()
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
		 * @see org.lunifera.ecview.core.ui.core.model.extension.YGridLayout#isMargin()
		 */
		public boolean isMargin() {
			return yView.isMargin();
		}
	}
}
