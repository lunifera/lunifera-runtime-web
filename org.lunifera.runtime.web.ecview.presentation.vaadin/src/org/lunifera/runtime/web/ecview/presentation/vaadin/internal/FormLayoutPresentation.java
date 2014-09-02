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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.ILayoutEditpart;
import org.eclipse.emf.ecp.ecview.common.model.core.YEmbeddable;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YAlignment;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YFormLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YFormLayoutCellStyle;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YHorizontalLayoutCellStyle;
import org.lunifera.runtime.web.ecview.presentation.vaadin.IConstants;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.HorizontalLayoutPresentation.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;

/**
 * This presenter is responsible to render a text field on the given layout.
 */
public class FormLayoutPresentation extends
		AbstractLayoutPresenter<ComponentContainer> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormLayoutPresentation.class);

	private CssLayout componentBase;
	private FormLayout formLayout;
	private ModelAccess modelAccess;

	private CssLayout fillerLayout;

	/**
	 * The constructor.
	 * 
	 * @param editpart
	 *            The editpart of that presentation.
	 */
	public FormLayoutPresentation(IElementEditpart editpart) {
		super((ILayoutEditpart) editpart);
		this.modelAccess = new ModelAccess((YFormLayout) editpart.getModel());
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
		formLayout.removeAllComponents();

		// create a map containing the style for the embeddable
		//
		Map<YEmbeddable, YFormLayoutCellStyle> yStyles = new HashMap<YEmbeddable, YFormLayoutCellStyle>();
		for (YFormLayoutCellStyle style : modelAccess.getCellStyles()) {
			if (yStyles.containsKey(style.getTarget())) {
				LOGGER.warn("Multiple style for element {}", style.getTarget());
			}
			yStyles.put(style.getTarget(), style);
		}

		// iterate all elements and build the child element
				//
				for (IEmbeddableEditpart child : getChildren()) {
					YEmbeddable yChild = (YEmbeddable) child.getModel();
					addChild(child, yStyles.get(yChild));
				}

		if (!modelAccess.isFillVertical()) {
			fillerLayout = new CssLayout();
			fillerLayout.setSizeFull();
			formLayout.addComponent(fillerLayout);
			// formLayout.setExpandRatio(fillerLayout, 1.0f);
		}

	}

	/**
	 * Is called to create the child component and apply layouting defaults to
	 * it.
	 * 
	 * @param editpart
	 * @param yStyle
	 * @return
	 */
	protected Cell addChild(IEmbeddableEditpart editpart,
			YFormLayoutCellStyle yStyle) {

		Component child = (Component) editpart.render(formLayout);

		// calculate and apply the alignment to be used
		//
		YAlignment yAlignment = yStyle != null && yStyle.getAlignment() != null ? yStyle
				.getAlignment() : null;
		if (yAlignment == null) {
			// use default
			yAlignment = YAlignment.TOP_LEFT;

			if (modelAccess.isFillVertical()) {
				// ensure that vertical alignment is FILL
				yAlignment = mapToVerticalFill(yAlignment);
			}
		}

		formLayout.addComponent(child);
		applyAlignment(child, yAlignment);

		return new Cell(child, yAlignment);
	}

	/**
	 * Sets the alignment to the component.
	 * 
	 * @param child
	 * @param yAlignment
	 */
	protected void applyAlignment(Component child, YAlignment yAlignment) {

		if (yAlignment != null) {
			child.setWidth("-1%");
			child.setHeight("-1%");
			switch (yAlignment) {
			case BOTTOM_CENTER:
				formLayout
						.setComponentAlignment(child, Alignment.BOTTOM_CENTER);
				break;
			case BOTTOM_FILL:
				formLayout.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				child.setWidth("100%");
				break;
			case BOTTOM_LEFT:
				formLayout.setComponentAlignment(child, Alignment.BOTTOM_LEFT);
				break;
			case BOTTOM_RIGHT:
				formLayout.setComponentAlignment(child, Alignment.BOTTOM_RIGHT);
				break;
			case MIDDLE_CENTER:
				formLayout
						.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
				break;
			case MIDDLE_FILL:
				formLayout.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				child.setWidth("100%");
				break;
			case MIDDLE_LEFT:
				formLayout.setComponentAlignment(child, Alignment.MIDDLE_LEFT);
				break;
			case MIDDLE_RIGHT:
				formLayout.setComponentAlignment(child, Alignment.MIDDLE_RIGHT);
				break;
			case TOP_CENTER:
				formLayout.setComponentAlignment(child, Alignment.TOP_CENTER);
				break;
			case TOP_FILL:
				formLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				break;
			case TOP_LEFT:
				formLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				break;
			case TOP_RIGHT:
				formLayout.setComponentAlignment(child, Alignment.TOP_RIGHT);
				break;
			case FILL_CENTER:
				formLayout.setComponentAlignment(child, Alignment.TOP_CENTER);
				child.setHeight("100%");
				break;
			case FILL_FILL:
				formLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setWidth("100%");
				child.setHeight("100%");
				break;
			case FILL_LEFT:
				formLayout.setComponentAlignment(child, Alignment.TOP_LEFT);
				child.setHeight("100%");
				break;
			case FILL_RIGHT:
				formLayout.setComponentAlignment(child, Alignment.TOP_RIGHT);
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

			formLayout = new FormLayout();
			formLayout.setSizeFull();
			componentBase.addComponent(formLayout);

			associateWidget(formLayout, modelAccess.yLayout);

			if (modelAccess.isMargin()) {
				formLayout.addStyleName(IConstants.CSS_CLASS_MARGIN);
				formLayout.setMargin(true);
			}

			if (!modelAccess.isSpacing()) {
				formLayout.setSpacing(false);
			} else {
				formLayout.setData(IConstants.CSS_CLASS_SPACING);
				formLayout.setSpacing(true);
			}

			if (modelAccess.isCssClassValid()) {
				formLayout.addStyleName(modelAccess.getCssClass());
			} else {
				formLayout.addStyleName(CSS_CLASS_CONTROL);
			}

			// creates the binding for the field
			createBindings(modelAccess.yLayout, formLayout, componentBase);

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
			unassociateWidget(formLayout);

			componentBase = null;
			formLayout = null;
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
		YEmbeddable yChild = (YEmbeddable) editpart.getModel();
		addChild(editpart, modelAccess.getCellStyle(yChild));
	}

	@Override
	protected void internalRemove(IEmbeddableEditpart child) {
		if (formLayout != null && child.isRendered()) {
			// will happen during disposal since children already disposed.
			formLayout.removeComponent((Component) child.getWidget());
		}
		
		child.unrender();
	}

	@Override
	protected void internalInsert(IEmbeddableEditpart editpart, int index) {
		refreshUI();
	}

	@Override
	protected void internalMove(IEmbeddableEditpart editpart,
			int oldIndex, int newIndex) {
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
		private final YFormLayout yLayout;

		public ModelAccess(YFormLayout yLayout) {
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
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YFormLayout#isSpacing()
		 */
		public boolean isSpacing() {
			return yLayout.isSpacing();
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
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YFormLayout#isMargin()
		 */
		public boolean isMargin() {
			return yLayout.isMargin();
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YFormLayout#getCellStyles()
		 */
		public EList<YFormLayoutCellStyle> getCellStyles() {
			return yLayout.getCellStyles();
		}
		
		public YFormLayoutCellStyle getCellStyle(YEmbeddable element) {
			return yLayout.getCellStyle(element);
		}

		/**
		 * @return
		 * @see org.eclipse.emf.ecp.ecview.ui.core.model.extension.YFormLayout#isFillVertical()
		 */
		public boolean isFillVertical() {
			return yLayout.isFillVertical();
		}
	}

	public static class Cell {
		private final Component component;
		private final YAlignment alignment;

		public Cell(Component component, YAlignment alignment) {
			super();
			this.component = component;
			this.alignment = alignment;
		}

		/**
		 * @return the component
		 */
		protected Component getComponent() {
			return component;
		}

		/**
		 * @return the alignment
		 */
		protected YAlignment getAlignment() {
			return alignment;
		}

	}
}
